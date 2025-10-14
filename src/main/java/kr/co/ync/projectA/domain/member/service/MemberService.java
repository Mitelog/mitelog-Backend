package kr.co.ync.projectA.domain.member.service;

import kr.co.ync.projectA.domain.follow.reposiroty.FollowRepository;
import kr.co.ync.projectA.domain.member.dto.request.MemberLoginRequest;
import kr.co.ync.projectA.domain.member.dto.request.MemberRegisterRequest;
import kr.co.ync.projectA.domain.member.dto.response.MemberPublicResponse;
import kr.co.ync.projectA.domain.member.dto.response.MemberResponse;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.mapper.MemberMapper;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;

    /**
     * ✅ 회원가입
     * Request : MemberRegisterRequest
     * Response: MemberResponse
     */
    public MemberResponse register(MemberRegisterRequest request) {
        // 1️⃣ 이메일 중복 검사
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 2️⃣ 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3️⃣ DTO → Entity 변환
        MemberEntity entity = MemberMapper.toEntity(request, encodedPassword);

        // 4️⃣ DB 저장
        MemberEntity saved = memberRepository.save(entity);

        // 5️⃣ Entity → Response DTO 변환 후 반환
        return MemberMapper.toResponse(saved);
    }

    /**
     * ✅ 로그인
     * Request : MemberLoginRequest
     * Response: MemberResponse (JWT 발급 시 TokenResponse로 교체 가능)
     */
    public MemberResponse login(MemberLoginRequest request) {
        // 1️⃣ 이메일로 회원 조회
        MemberEntity member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 2️⃣ 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3️⃣ Entity → Response DTO 변환
        return MemberMapper.toResponse(member);
    }

    /**
     * ✅ 단일 회원 조회 (id 기반)
     * Response : MemberResponse
     */
    public Optional<MemberResponse> getMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberMapper::toResponse);
    }

    public MemberPublicResponse getPublicProfile(Long targetId, Long viewerId) {
        MemberEntity member = memberRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        int reviewCount = reviewRepository.countByMember(member);
        int followerCount = followRepository.countByFollowingId(targetId);
        int followingCount = followRepository.countByFollowerId(targetId);

        boolean isFollowed = false;

        // ✅ 로그인한 유저(viewer)가 프로필 주인(target)을 팔로우했는지 확인
        if (viewerId != null && !viewerId.equals(targetId)) {
            isFollowed = followRepository.existsByFollowerIdAndFollowingId(viewerId, targetId);
        }

        // ✅ isFollowed 값을 포함해 응답
        return new MemberPublicResponse(member, reviewCount, followerCount, followingCount, isFollowed);
    }



}
