package Auction;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.annotation.*;

/**
 * SpringBoot 2.2 이후 MediaType.APPLICATION_JSON_UTF8이 deprecated 되었다.
 * 그로 인해 response header의 content-type:charset=UTF-8이 제거되어 한글이 깨지는데,
 * MockMvc build 시 CharacterEncodingFilter를 빈으로 등록해 주면 해결된다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoConfigureMockMvc
@Import(EnableMockMvc.Config.class)
public @interface EnableMockMvc {
    class Config {
        @Bean
        public CharacterEncodingFilter characterEncodingFilter() {
            return new CharacterEncodingFilter("UTF-8", true);
        }
    }
}