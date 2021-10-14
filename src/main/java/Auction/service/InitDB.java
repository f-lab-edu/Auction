package Auction.service;

import Auction.service.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit() {
            Member member = Member.builder()
                            .memberId("test")
                            // "test1234" 암호화 값
                            .memberPassword("$2a$10$BinW.Y75dieUuYHkNdeGROUpgykN/za7wbGbU6sN9RjrnFhv0aXGC")
                            .build();
            em.persist(member);
        }
    }
}
