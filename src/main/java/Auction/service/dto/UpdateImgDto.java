package Auction.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateImgDto {

    public enum Status{INSERT, UPDATE, ORIGINAL}

    Long product_img_id;

    @NotNull(message = "status가 없습니다")
    String status;
}
