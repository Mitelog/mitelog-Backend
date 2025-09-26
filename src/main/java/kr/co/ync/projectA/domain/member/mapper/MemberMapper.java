package kr.co.ync.projectA.domain.member.mapper;

import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;

public class MemberMapper {
<<<<<<< HEAD

    public static Member toDTO(MemberEntity entity) {
        if (entity == null) return null;
=======
    public static Member toDTO(MemberEntity entity){
>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)
        return Member.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .name(entity.getName())
                .phone(entity.getPhone())
                .role(entity.getRole())
                .build();
    }

<<<<<<< HEAD
    public static MemberEntity toEntity(Member member) {
        if (member == null) return null;
=======
    public static MemberEntity toEntity(Member member){
>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)
        return MemberEntity.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .phone(member.getPhone())
                .role(member.getRole())
                .build();
    }
}
