package com.financial_tracker.source_managment;


import com.financial_tracker.auth.CustomUserDetails;
import com.financial_tracker.shared.dto.PageRequest;
import com.financial_tracker.shared.dto.PageResponse;
import com.financial_tracker.source_managment.dto.request.CreateSource;
import com.financial_tracker.source_managment.dto.request.UpdateSource;
import com.financial_tracker.source_managment.dto.response.SourceResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("source")
public class SourceController {

    private final SourceService sourceService;
    private static final Logger log = LoggerFactory.getLogger(SourceController.class);

    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<SourceResponse>> getAllSources(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid PageRequest pageRequest
    ) {

        log.info("Getting all sources by account {}", customUserDetails.getAccountId());

        return ResponseEntity.status(HttpStatus.OK).body(sourceService.findAll(pageRequest, customUserDetails.getAccountId()));


    }

    @GetMapping("/{sourceId}")
    public ResponseEntity<SourceResponse> getSourceById(@PathVariable("sourceId") UUID sourceId,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails
                                                        ) {

        log.info("Getting source by id: {},  account {}", sourceId, customUserDetails.getAccountId());

        return ResponseEntity.status(HttpStatus.OK).body(sourceService.getSourceById(sourceId, customUserDetails.getAccountId()));


    }

    @PostMapping()
    public ResponseEntity<SourceResponse> createSource(
            @RequestBody @Valid CreateSource createSource,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        log.info("Creating source: {},  account {}", createSource.name(), customUserDetails.getAccountId());

        return ResponseEntity.status(HttpStatus.CREATED).body(sourceService.createSource(createSource, customUserDetails.getAccountId()));


    }

    @PutMapping("{sourceId}")
    public ResponseEntity<SourceResponse> updateSource(
            @PathVariable("sourceId") UUID sourceId,
            @RequestBody @Valid UpdateSource updateSource,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        log.info("Updating source id: {}, name: {},  account {}", sourceId, updateSource.name(), customUserDetails.getAccountId());

        return ResponseEntity.status(HttpStatus.OK).body(sourceService.updateSource(updateSource, sourceId, customUserDetails.getAccountId()));


    }

    @DeleteMapping("{sourceId}")
    public ResponseEntity<Void> deleteSource(
            @PathVariable("sourceId") UUID sourceId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        log.info("Deleting source by id: {},  account {}", sourceId, customUserDetails.getAccountId());

        sourceService.deleteSource(sourceId, customUserDetails.getAccountId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


    }

}
