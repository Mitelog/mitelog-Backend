package kr.co.ync.projectA.domain.member.mapper;

import kr.co.ync.projectA.domain.member.dto.request.MemberRegisterRequest;
import kr.co.ync.projectA.domain.member.dto.response.MemberResponse;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;

public class MemberMapper {

    // 🔹 1) 회원가입 요청 DTO → Entity
    public static MemberEntity toEntity(MemberRegisterRequest req, String encodedPassword) {
        return MemberEntity.builder()
                .email(req.getEmail())
                .password(encodedPassword)
                .name(req.getName())
                .phone(req.getPhone())
                .build();
    }

    // 🔹 2) Entity → 응답 DTO
    public static MemberResponse toResponse(MemberEntity e) {
        return MemberResponse.builder()
                .id(e.getId())
                .email(e.getEmail())
                .name(e.getName())
                .phone(e.getPhone())
                .role(e.getRole())
                .build();
    }
}
