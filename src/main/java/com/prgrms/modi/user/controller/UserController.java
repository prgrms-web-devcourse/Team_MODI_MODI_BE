package com.prgrms.modi.user.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/me")
    @Operation(summary = "유저 개인 정보 조회", description = "파라미터는 필요없고 토큰 Authorize만 필요, 다른 사용자 접근 불가")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<UserResponse> getUserDetail(
        @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        return ResponseEntity.ok(
            userService.getUserDetail(authentication.userId));
    }

}
