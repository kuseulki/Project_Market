package com.example.project.config.oauth;

import com.example.project.config.auth.PrincipalDetails;
import com.example.project.config.oauth.provider.GoogleUserInfo;
import com.example.project.config.oauth.provider.NaverUserInfo;
import com.example.project.config.oauth.provider.OAuth2UserInfo;
import com.example.project.entity.Member;
import com.example.project.enums.RoleType;
import com.example.project.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 강제로 회원가입 진행

        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println(" 구글 로그인 요청 ");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println(" 네이버 로그인 요청 ");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("구글과 네이버 지원함");
        }

        // 기존 회원 인지 확인
        Optional<Member> memberOptional = memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        Member member;

        if(memberOptional.isPresent()) {
            member = memberOptional.get();      // member 존재 시 update
            member.setUserName(oAuth2UserInfo.getName());
            member.setEmail(oAuth2UserInfo.getEmail());
            memberRepository.save(member);
        } else {
            member = Member.builder()
                    .userId(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .userName(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .role(RoleType.SNS)
                    .password("null")
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            memberRepository.save(member);
        }
        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
