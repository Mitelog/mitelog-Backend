package kr.co.ync.projectA.domain.member.service;

import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.mapper.MemberMapper;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member register(Member dto) {
        System.out.println("📩 Register Request DTO: " + dto);
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        //비밀번호 암호화
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        MemberEntity entity = MemberMapper.toEntity(dto);
        MemberEntity saved = memberRepository.save(entity);
        return MemberMapper.toDTO(saved);
    }

    public Optional<Member> getMember(Long id) {
        return memberRepository.findById(id).map(MemberMapper::toDTO);
    }
}
