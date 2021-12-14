package com.prgrms.modi.common.oauth2.info;

import com.prgrms.modi.error.exception.NotEnoughUserInformationException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    private static final int ADULT_AGE = 19;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public boolean isAdult() {
        String userBirthDate = getBirthDate();
        LocalDate bod = LocalDate.parse(userBirthDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        return Period.between(bod, LocalDate.now()).getYears() >= ADULT_AGE;
    }

    private String getBirthDate() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            throw new NotEnoughUserInformationException("유저의 필수 정보가 부족합니다");
        }

        return response.get("birthyear")
            + ((String) response.get("birthday")).replaceAll("[^0-9]", "");
    }

}
