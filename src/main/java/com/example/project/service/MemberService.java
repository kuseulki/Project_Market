package com.example.project.service;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Member;
import com.example.project.enums.RoleType;
import com.example.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**   아이디 중복 체크    */
    public String idCheck(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        if (optionalMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        return "ok";
    }
    
    /** 회원가입 */
    public void save(MemberDto memberDto, String userId) {
       idCheck(userId);
        Member member = Member.toMemberEntity(memberDto, passwordEncoder);
        memberRepository.save(member);
    }

    /** 회원 조회 */
    public List<MemberDto> findAll(){
        List<Member> memberEntity = memberRepository.findAll();
        List<MemberDto> memberDtoList = new ArrayList<>();
        for (Member member : memberEntity) {
            memberDtoList.add(MemberDto.toMemberDto(member));
        }
        return memberDtoList;
    }

    /** 회원 삭제 */
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }


    /** 회원 한 건 조회 */
    public MemberDto findById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()){
            return MemberDto.toMemberDto(optionalMember.get());
        } else {
            return null;
        }
    }

    /** 회원 수정 */
    @Transactional
    public void updateMember(Long id, MemberDto memberDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        member.update(memberDto);
    }

    /** 카카오 회원 가입 -> 찾기 */
    public Member findUserId(String userId){
        Member member = memberRepository.findByUserId(userId).orElseGet(()-> {
            return new Member();
        });
        return member;
    }

    /** 카카오 회원가입 */
    @Transactional
    public void kakaoSave(Member member) {

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(RoleType.USER);

        memberRepository.save(member);
    }
 }
