package com.prgrms.modi.ott.controller;

import com.prgrms.modi.ott.dto.CarouselListResponse;
import com.prgrms.modi.ott.dto.OttListResponse;
import com.prgrms.modi.ott.dto.OttResponse;
import com.prgrms.modi.ott.service.OttService;
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
    public ResponseEntity<OttListResponse> getAll() {
        return ResponseEntity.ok(ottService.getAll());
    }

    @GetMapping("/{ottId}")
    public ResponseEntity<OttResponse> getOtt(@PathVariable Long ottId) {
        return ResponseEntity.ok(ottService.getOtt(ottId));
    }

    @GetMapping(path = "/waitings")
    public ResponseEntity<CarouselListResponse> getAllCarousels() {
        return ResponseEntity.ok(ottService.getCarouselList());
    }

}
