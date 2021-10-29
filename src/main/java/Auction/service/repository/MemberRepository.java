package Auction.service.repository;

import Auction.service.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByPhone(String phone);
    boolean existsByPhone(String phone);
}
