package com.nextcloudlab.kickytime.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class CognitoUserInfoClient {
    private final RestClient restClient;
    private final String userInfoUri;

    public CognitoUserInfoClient(@Value("${app.cognito.user-info-uri}") String userInfoUri) {
        this.restClient = RestClient.builder().build();
        this.userInfoUri = userInfoUri;
    }

    public UserInfo fetch(String accessToken) {
        return restClient
                .get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(UserInfo.class);
    }

    public record UserInfo(
            String email, String nickname, @JsonProperty("email_verified") Boolean emailVerified) {}
}
