package kr.co.ync.projectA.domain.member.mapper;

import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;

public class MemberMapper {
    public static Member toDTO(MemberEntity entity){
        return Member.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .name(entity.getName())
                .phone(entity.getPhone())
                .role(entity.getRole())
                .build();
    }

    public static MemberEntity toEntity(Member member){
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
