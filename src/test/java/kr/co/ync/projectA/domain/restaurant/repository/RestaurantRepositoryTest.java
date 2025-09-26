package kr.co.ync.projectA.domain.restaurant.repository;

import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

import java.util.stream.IntStream;

@SpringBootTest
class RestaurantRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void testInsertRestaurant() {
        // 멤버 더미 데이터 먼저 넣기 (test DB가 초기화되므로)
        IntStream.rangeClosed(1, 30).forEach(i -> {
            String email = "user" + i + "@aaa.com";
            memberRepository.save(MemberEntity.builder()
                    .email(email)
                    .name("김재환" + i)
                    .password("6974")
                    .phone("123" + i)
                    .role(MemberRole.USER)
                    .build());
        });

        // 이제 레스토랑 더미 데이터 넣기
        IntStream.rangeClosed(1, 30).forEach(i -> {
            String email = "user" + i + "@aaa.com";
            MemberEntity memberEntity = memberRepository.findById(email)
                    .orElseThrow();
            restaurantRepository.save(RestaurantEntity.builder()
                    .email(memberEntity)
                    .name("재환식당" + i)
                    .address("재환시 재환동 재환로69길 7" + i)
                    .area("재환시")
                    .phone("0123" + i)
                    .reserAvail(true)
                    .build());
        });
    }
}