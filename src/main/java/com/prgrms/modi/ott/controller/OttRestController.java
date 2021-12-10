package com.prgrms.modi.ott.controller;

import com.prgrms.modi.ott.dto.CarouselListResponse;
import com.prgrms.modi.ott.dto.OttListResponse;
import com.prgrms.modi.ott.dto.OttResponse;
import com.prgrms.modi.ott.service.OttService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otts")
public class OttRestController {

    private final OttService ottService;

    public OttRestController(OttService ottService) {
        this.ottService = ottService;
    }

    @GetMapping
    @Operation(summary = "전체 OTT 서비스 조회", description = "전체 OTT 서비스를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "전체 OTT 서비스 조회")
    })
    public ResponseEntity<OttListResponse> getAll() {
        return ResponseEntity.ok(ottService.getAll());
    }

    @GetMapping("/{ottId}")
    @Operation(summary = "단일 OTT 서비스 조회", description = "단일 OTT 서비스를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "단일 OTT 서비스 조회"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 OTT 서비스 조회 요청(NOTFOUND)")
    })
    public ResponseEntity<OttResponse> getOtt(
        @Parameter(description = "OTT의 ID") @PathVariable Long ottId
    ) {
        return ResponseEntity.ok(ottService.getOtt(ottId));
    }

    @Operation(summary = "전체 캐루셀 목록 조회", description = "전체 캐루셀 목록을 조회합니다. ")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "전체 캐루셀 목록 조회")
    })
    @GetMapping(path = "/waitings")
    public ResponseEntity<CarouselListResponse> getAllCarousels() {
        return ResponseEntity.ok(ottService.getCarouselList());
    }

}
