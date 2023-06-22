package com.example.project.entity;

import com.example.project.dto.MemberDto;
import com.example.project.enums.RoleType;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String userId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column
    private String userName;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String zipCode;

    @Column
    private String addr;

    @Column
    private String addrDetail;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String provider;
    private String providerId;

    public void update(MemberDto memberDto) {
        this.userName = memberDto.getUserName();
        this.email = memberDto.getEmail();
        this.phone = memberDto.getPhone();
        this.zipCode = memberDto.getZipCode();
        this.addr = memberDto.getAddr();
        this.addrDetail = memberDto.getAddrDetail();
    }


    public static Member toMemberEntity(MemberDto memberDto, BCryptPasswordEncoder bcEncoder) {
        return Member.builder()
                .userId(memberDto.getUserId())
                .password(bcEncoder.encode(memberDto.getPassword()))
                .userName(memberDto.getUserName())
                .email(memberDto.getEmail())
                .phone(memberDto.getPhone())
                .zipCode(memberDto.getZipCode())
                .addr(memberDto.getAddr())
                .addrDetail(memberDto.getAddrDetail())
                .role(RoleType.ADMIN)
                .build();
    }
}
