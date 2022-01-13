package com.prgrms.modi.notification.controller;

import com.prgrms.modi.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping(path = "/subscribe/{userId}", produces = "text/event-stream")
    @Operation(summary = "로그인한 유저 sse 연결")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 sse 연결 성공 (OK)")
    })
    public SseEmitter subscribe(
        @Parameter(description = "유저의 ID") @PathVariable Long userId,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        return notificationService.subscribe(userId, lastEventId);
    }

}
