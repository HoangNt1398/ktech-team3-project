package com.example.server.auth.oauth.userinfo;

import lombok.Getter;

@Getter
public enum ProviderType {

    GOOGLE("google"),
    NAVER("naver");

    private String provider;
    ProviderType(String provider) {
        this.provider = provider;
    }
}
