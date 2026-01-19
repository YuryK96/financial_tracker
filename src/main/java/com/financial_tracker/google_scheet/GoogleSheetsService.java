package com.financial_tracker.google_scheet;

import com.financial_tracker.auth.CustomUserDetails;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.account.AccountRepository;
import com.financial_tracker.core.google_token.GoogleTokenEntity;
import com.financial_tracker.core.google_token.GoogleTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoogleSheetsService {

    @Value("${app.google.client_id}")
    private String clientId;

    @Value("${app.google.client_secret}")
    private String clientSecret;

    @Value("${app.google.redirect_uri}")
    private String redirectUri;


    private GoogleTokenRepository googleTokenRepository;


    private AccountRepository accountRepository;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public static class GoogleTokens {
        private final String accessToken;
        private final String refreshToken;
        private final Instant expiresAt;

        public GoogleTokens(String accessToken, String refreshToken, Instant expiresAt) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresAt = expiresAt;
        }

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public Instant getExpiresAt() { return expiresAt; }
    }

    private UUID getCurrentAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getAccountId();
        }
        throw new IllegalStateException("User not authenticated");
    }

    public String getAuthorizationUrl() {
        try {
            UUID accountId = getCurrentAccountId();
            return "https://accounts.google.com/o/oauth2/auth?" +
                    "client_id=" + clientId +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name()) +
                    "&response_type=code" +
                    "&scope=" + URLEncoder.encode("https://www.googleapis.com/auth/spreadsheets", StandardCharsets.UTF_8.name()) +
                    "&access_type=offline" +
                    "&prompt=consent" +
                    "&state=" + accountId.toString();
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }

    @Transactional
    public GoogleTokens exchangeCodeForTokens(String authorizationCode, UUID accountId)
            throws IOException, InterruptedException {

        String tokenUrl = "https://oauth2.googleapis.com/token";

        try {
            String params = "code=" + URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8.name()) +
                    "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8.name()) +
                    "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8.name()) +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name()) +
                    "&grant_type=authorization_code";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("Failed to exchange code: " + response.body());
            }

            // Парсинг JSON
            String responseBody = response.body();
            String accessToken = extractJsonValue(responseBody, "access_token");
            String refreshToken = extractJsonValue(responseBody, "refresh_token");
            String expiresInStr = extractJsonValue(responseBody, "expires_in");

            int expiresIn = expiresInStr != null ? Integer.parseInt(expiresInStr) : 3600;
            Instant expiresAt = Instant.now().plusSeconds(expiresIn);

            // Сохраняем токены в БД
            saveTokensToDatabase(accountId, accessToken, refreshToken, expiresAt);

            return new GoogleTokens(accessToken, refreshToken, expiresAt);

        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }

    @Transactional
    protected void saveTokensToDatabase(UUID accountId, String accessToken, String refreshToken, Instant expiresAt) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        Optional<GoogleTokenEntity> existingToken = googleTokenRepository.findByAccountId(accountId);

        GoogleTokenEntity tokenEntity;
        if (existingToken.isPresent()) {
            tokenEntity = existingToken.get();
        } else {
            tokenEntity = new GoogleTokenEntity();
            tokenEntity.setAccount(account);
        }

        tokenEntity.setAccessToken(accessToken);
        if (refreshToken != null) {
            tokenEntity.setRefreshToken(refreshToken);
        }
        tokenEntity.setTokenExpiry(LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault()));

        googleTokenRepository.save(tokenEntity);
    }


    @Transactional(readOnly = true)
    public Optional<GoogleTokenEntity> getTokensForCurrentUser() {
        UUID accountId = getCurrentAccountId();
        return googleTokenRepository.findByAccountId(accountId);
    }

    public boolean isGoogleConnected() {
        UUID accountId = getCurrentAccountId();
        return googleTokenRepository.existsByAccountId(accountId);
    }

    @Transactional
    public String refreshAccessTokenIfNeeded() throws IOException, InterruptedException {
        UUID accountId = getCurrentAccountId();
        GoogleTokenEntity tokenEntity = googleTokenRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IOException("No Google tokens found for user"));

        if (tokenEntity.getTokenExpiry() == null ||
                tokenEntity.getTokenExpiry().isBefore(LocalDateTime.now())) {

            return refreshAccessToken(tokenEntity);
        }

        return tokenEntity.getAccessToken();
    }

    @Transactional
    protected String refreshAccessToken(GoogleTokenEntity tokenEntity)
            throws IOException, InterruptedException {

        if (tokenEntity.getRefreshToken() == null) {
            throw new IOException("No refresh token available");
        }

        String tokenUrl = "https://oauth2.googleapis.com/token";

        try {
            String params = "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8.name()) +
                    "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8.name()) +
                    "&refresh_token=" + URLEncoder.encode(tokenEntity.getRefreshToken(), StandardCharsets.UTF_8.name()) +
                    "&grant_type=refresh_token";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                googleTokenRepository.delete(tokenEntity);
                throw new IOException("Failed to refresh token. User needs to re-authenticate.");
            }

            String responseBody = response.body();
            String newAccessToken = extractJsonValue(responseBody, "access_token");
            String expiresInStr = extractJsonValue(responseBody, "expires_in");

            int expiresIn = expiresInStr != null ? Integer.parseInt(expiresInStr) : 3600;

            tokenEntity.setAccessToken(newAccessToken);
            tokenEntity.setTokenExpiry(LocalDateTime.now().plusSeconds(expiresIn));
            googleTokenRepository.save(tokenEntity);

            return newAccessToken;

        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }

    @Transactional
    public void saveSpreadsheetId(String spreadsheetId) {
        UUID accountId = getCurrentAccountId();
        GoogleTokenEntity tokenEntity = googleTokenRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalStateException("Google not connected"));

        tokenEntity.setSpreadsheetId(spreadsheetId);
        googleTokenRepository.save(tokenEntity);
    }


    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return null;

            startIndex += searchKey.length();
            while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
                startIndex++;
            }

            char firstChar = json.charAt(startIndex);
            int endIndex;

            if (firstChar == '"') {
                startIndex++;
                endIndex = json.indexOf('"', startIndex);
            } else {
                endIndex = startIndex;
                while (endIndex < json.length() &&
                        (Character.isDigit(json.charAt(endIndex)) || json.charAt(endIndex) == '.')) {
                    endIndex++;
                }
            }

            if (endIndex > startIndex && endIndex <= json.length()) {
                return json.substring(startIndex, endIndex);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}