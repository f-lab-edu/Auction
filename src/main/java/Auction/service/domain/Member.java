package Auction.service.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "member_id")
    private String memberId;

    @NotNull
    @Column(name = "password")
    private String memberPassword;

    @Setter
    private String name;

    @Setter
    private String address;

    @Setter
    private String phone;

}
