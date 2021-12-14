package com.prgrms.modi.common.oauth2.info;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getBirthyear() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");

        if (properties == null) {
            return null;
        }
        return (String) properties.get("birthyear");
    }

    @Override
    public String getBirthDay() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");

        if (properties == null) {
            return null;
        }
        return (String) properties.get("birthday");
    }

}
