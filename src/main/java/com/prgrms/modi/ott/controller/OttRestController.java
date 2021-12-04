package com.prgrms.modi.ott.controller;

import com.prgrms.modi.ott.dto.AllOttListResponse;
import com.prgrms.modi.ott.service.OttService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otts")
public class OttRestController {

    private final OttService ottService;

    public OttRestController(OttService ottService) {
        this.ottService = ottService;
    }

    @GetMapping()
    public ResponseEntity<AllOttListResponse> getAllOtts() {
        return ResponseEntity.ok(ottService.getAllOtts());
    }

}
