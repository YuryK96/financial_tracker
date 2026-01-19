package com.financial_tracker.core.google_token.account;

import com.financial_tracker.google_scheet.GoogleSheetsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/google-sheets")
public class GoogleSheetsController {


    private GoogleSheetsService googleSheetsService;

    public GoogleSheetsController(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }


    @GetMapping("/connect")
    public ResponseEntity<?> getConnectUrl() {
        try {
            String authUrl = googleSheetsService.getAuthorizationUrl();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "authUrl", authUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }


    @GetMapping("/oauth-callback")
    public ResponseEntity<?> handleOAuthCallback(
            @RequestParam String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error) {

        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Authorization failed: " + error
            ));
        }

        try {
            UUID accountId = UUID.fromString(state);
            googleSheetsService.exchangeCodeForTokens(code, accountId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Google Sheets successfully connected"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Failed to connect: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getConnectionStatus() {
        try {
            boolean isConnected = googleSheetsService.isGoogleConnected();
            var tokens = googleSheetsService.getTokensForCurrentUser();

            Map<String, Object> response = Map.of(
                    "connected", isConnected,
                    "hasSpreadsheet", tokens.map(t -> t.getSpreadsheetId() != null).orElse(false)
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/spreadsheet")
    public ResponseEntity<?> saveSpreadsheetId(@RequestBody Map<String, String> request) {
        try {
            String spreadsheetId = request.get("spreadsheetId");
            if (spreadsheetId == null || spreadsheetId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Spreadsheet ID is required"
                ));
            }

            googleSheetsService.saveSpreadsheetId(spreadsheetId.trim());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Spreadsheet ID saved"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/disconnect")
    public ResponseEntity<?> disconnect() {
        try {
            // TODO: Реализовать метод удаления в репозитории
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Google Sheets disconnected"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}