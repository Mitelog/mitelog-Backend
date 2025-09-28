package kr.co.ync.projectA.domain.member.service;

import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.mapper.MemberMapper;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    public Member register(Member dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        MemberEntity entity = MemberMapper.toEntity(dto);
        MemberEntity saved = memberRepository.save(entity);
        return MemberMapper.toDTO(saved);
    }

    // 로그인
    public Member login(String email, String password) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return MemberMapper.toDTO(member);
    }

    // 회원 조회
    public Optional<Member> getMember(Long id) {
        return memberRepository.findById(id).map(MemberMapper::toDTO);
    }
}
