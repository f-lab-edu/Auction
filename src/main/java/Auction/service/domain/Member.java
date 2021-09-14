package Auction.service.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String userId;
    @NotNull
    private String userPassword;

    public Member(String userId, String userPassword){
        this.userId = userId;
        this.userPassword = userPassword;
    }

    @Setter
    private String userName;
    @Setter
    private String address;
    @Setter
    private String phone;
}
