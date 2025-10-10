package kr.co.ync.projectA.domain.restaurant.repository;

import jakarta.transaction.Transactional;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
//@Transactional // 테스트 후 자동 롤백
class RestaurantRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void testInsertRestaurant() {
        // 1️⃣ 멤버 더미 데이터 30명 생성
        IntStream.rangeClosed(1, 30).forEach(i -> {
            String email = "user" + i + "@aaa.com";
            memberRepository.save(MemberEntity.builder()
                    .name("user" + i)
                    .password("6974")
                    .phone("010-1234-" + String.format("%04d", i))
                    .email(email)
                    .role(MemberRole.USER)
                    .build());
        });

        // 2️⃣ 각 멤버에게 식당 1개씩 연결
        IntStream.rangeClosed(1, 30).forEach(i -> {
            String email = "user" + i + "@aaa.com";

            // MemberEntity 조회
            MemberEntity owner = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("회원 없음: " + email));

            // 식당 생성 및 저장
            restaurantRepository.save(RestaurantEntity.builder()
                    .owner(owner) // ✅ 올바른 관계 설정
                    .name("재환식당" + i)
                    .address("재환시 재환동 재환로69길 " + i)
                    .area("재환시")
                    .phone("010-0000-" + String.format("%04d", i))
                    .image("image" + i + ".jpg")
                    .build());
        });

        System.out.println("✅ 테스트용 식당 30개가 정상적으로 저장되었습니다.");
    }
}
