package com.prgrms.modi.common.oauth2.info;

import com.prgrms.modi.error.exception.NotEnoughUserInformationException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private static final String AGE_RANGE_UNDER10 = "0~9";

    private static final String AGE_RANGE_UNDER20 = "10~";

    private final Logger log = LoggerFactory.getLogger(getClass());

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public boolean isAdult() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");

        log.info("[*] properties: {}", properties);

        if (properties.get("age_range") == null) {
            throw new NotEnoughUserInformationException("유저의 필수 정보가 부족합니다");
        }
        String ageRange = (String) properties.get("age_range");
        return !ageRange.startsWith(AGE_RANGE_UNDER10) && !ageRange.startsWith(AGE_RANGE_UNDER20);
    }

}
