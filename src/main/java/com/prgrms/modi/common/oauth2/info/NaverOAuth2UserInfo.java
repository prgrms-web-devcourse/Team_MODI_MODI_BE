package com.prgrms.modi.common.oauth2.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getBirthyear() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("birthyear");
    }

    @Override
    public String getBirthDay() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return ((String) response.get("birthday"));
    }

}