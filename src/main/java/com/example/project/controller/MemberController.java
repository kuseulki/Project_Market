package com.example.project.controller;

import com.example.project.config.auth.PrincipalDetails;
import com.example.project.config.auth.PrincipalDetailsService;
import com.example.project.dto.MemberDto;
import com.example.project.entity.KakaoProfile;
import com.example.project.entity.Member;
import com.example.project.entity.OAuthToken;
import com.example.project.repository.MemberRepository;
import com.example.project.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PrincipalDetailsService principalDetailsService;

    @Value("${cos.key}")
    private String cosKey;

    /**   카카오 로그인   */
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code){

        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "10ef4383058ba6de6bb30e00a0fc66b4");
        params.add("redirect_uri", "http://localhost/auth/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 : Post 방식, response 변수의 응답 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // json 데이터를 자바에서 처리하기 위해서 자바오브젝트로 바꿈
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        RestTemplate rt2 = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Member kakaoMember = Member.builder()
                .userId(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                .userName(kakaoProfile.getKakao_account().getProfile().getNickname())
                .password(cosKey)
                .email( kakaoProfile.getKakao_account().getEmail())
                .build();

        Member originMember = memberService.findUserId(kakaoMember.getUserId());

        if(originMember.getUserId() == null){
            memberService.kakaoSave(kakaoMember);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoMember.getUserId(), cosKey);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }


    /**   회원가입   */
    @GetMapping("/join")
    public String joinForm(Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Validated MemberDto memberDto, BindingResult bindingResult, Model model, String userId){

        if(bindingResult.hasErrors()){
            return "member/join";
        }
        try {
            memberService.save(memberDto, userId);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }
        return "redirect:/login";
    }

    /**   아이디 중복 체크   */
    @PostMapping("/member/id-check")
    public @ResponseBody String idCheck(@RequestParam("userId") String userId){
        String checkResult = memberService.idCheck(userId);
        return checkResult;
    }

    /**   로그인   */
    @GetMapping("/login")
    public String loginForm(){
        return "member/login";
    }

    /**   로그인 오류   */
    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }

    /**   회원 목록   */
    @GetMapping("/member")
    public String findAll(Model model){
        List<MemberDto> MemberDtoList = memberService.findAll();
        model.addAttribute("memberList", MemberDtoList);
        return "member/memberList";
    }

    /**   회원 조회   */
    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        MemberDto memberDto = memberService.findById(id);
        model.addAttribute("member", memberDto);
        return "member/memberDetail";
    }

    /**   회원 탈퇴 - 삭제   */
    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id, HttpSession session){
        memberService.deleteById(id);
        session.invalidate(); // 세션 무효화
        return "redirect:/";
    }

    /**   회원 수정  */

    @GetMapping("/member/{id}/edit")
    public String editMemberForm(@PathVariable Long id, Model model) {
        MemberDto memberDto = memberService.findById(id);
        model.addAttribute("memberUpdate", memberDto);
        return "member/memberUpdate";
    }

    @PostMapping("/member/{id}/edit")
    public String updateMember(@PathVariable Long id, @ModelAttribute MemberDto memberDto) {
        memberService.updateMember(id, memberDto);
        return "redirect:/member";
    }

    /** 마이페이지 */
    @GetMapping("/member/page")
    public String adminPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetail){
        model.addAttribute("member", principalDetail.getMember());
        return "member/memberPage";
    }
}