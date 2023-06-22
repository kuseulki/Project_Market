package com.example.project.dto;

import com.example.project.entity.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDto {

    private Long id;

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;

//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "이름은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private String userName;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "^[0-9]{11}$", message = "숫자만 입력해 주세요.")
    private String phone;

    private String zipCode;

    private String addr;

    @NotBlank(message = "상세주소를 입력해 주세요")
    private String addrDetail;

    private String provider;

    public static MemberDto toMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .password(member.getPassword())
                .userName(member.getUserName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .zipCode(member.getZipCode())
                .addr(member.getAddr())
                .addrDetail(member.getAddrDetail())
                .provider(member.getProvider())
                .build();
    }
}