package com.prgrms.modi.notification.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.notification.dto.NotificationsResponse;
import com.prgrms.modi.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(path = "/subscribe/{userId}", produces = "text/event-stream;charset=UTF-8")
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

    @GetMapping
    @Operation(summary = "로그인한 유저의 모든 알림 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "모든 알림 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증 할 수 없는 경우 (UNAUTHORIZED)"),
    })
    public ResponseEntity<NotificationsResponse> getNotifications(
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        NotificationsResponse resp = notificationService.findAllById(authentication.userId);
        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "알림 읽음(삭제)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "알림 읽음 성공 (NO CONTENT)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증 할 수 없는 경우 (UNAUTHORIZED)"),
        @ApiResponse(responseCode = "404", description = "없는 알림인 경우 (NOT FOUND)")
    })
    public ResponseEntity<Void> readNotification(
        @PathVariable Long id,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        notificationService.readNotification(id);
        return ResponseEntity.noContent().build();
    }

}
