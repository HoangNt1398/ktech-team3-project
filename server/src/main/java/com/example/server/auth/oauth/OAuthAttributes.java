package com.example.server.auth.oauth;

import com.example.server.auth.oauth.userinfo.GoogleUserInfo;
import com.example.server.auth.oauth.userinfo.NaverUserInfo;
import com.example.server.auth.oauth.userinfo.OAuth2UserInfo;
import com.example.server.auth.oauth.userinfo.ProviderType;
import com.example.server.exception.BusinessLogicException;
import com.example.server.exception.ExceptionCode;
import com.example.server.member.entity.Member;
import com.example.server.member.entity.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {

    private String providerId;

    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuthAttributes(String proverId, OAuth2UserInfo oAuth2UserInfo) {
        this.providerId = proverId;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(ProviderType providerType,
                                     String providerId,
                                     Map<String, Object> attributes) {
        if (providerType == ProviderType.GOOGLE) {
            return ofGoogle(providerId, attributes);
        }

        if (providerType == ProviderType.NAVER) {
            return ofNaver(providerId, attributes);
        }

        throw new BusinessLogicException(ExceptionCode.PROVIDER_NOT_FOUND);
    }

    private static OAuthAttributes ofGoogle(String providerId, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .proverId(providerId)
                .oAuth2UserInfo(new GoogleUserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofNaver(String providerId, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .proverId(providerId)
                .oAuth2UserInfo(new NaverUserInfo(attributes))
                .build();
    }

    public Member toEntity(ProviderType providerType, OAuth2UserInfo oAuth2UserInfo, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setProvider(providerType.getProvider());
        member.setProviderId(oAuth2UserInfo.getProverId());

        member.setImageUrl(oAuth2UserInfo.getImageUrl());
        member.setEmail(oAuth2UserInfo.getEmail());
        member.setNickname(oAuth2UserInfo.getEmail());
        member.setRoles(Collections.singletonList(Role.USER.getRole()));
        member.setPassword(passwordEncoder.encode("NO_PASS" + UUID.randomUUID()));

        return member;
    }
}
