package com.prgrms.modi.user.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.ForbiddenException;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "유저 정보 조회", description = "유저의 개인 정보를 조회하는 메서드")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<UserResponse> getUserDetail(
        @PathVariable @Parameter(description = "조회 대상자 PK (본인)", required = true, example = "1") Long id,
        @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (id.equals(authentication.userId)) {
            return ResponseEntity.ok(
                userService.findById(authentication.userId));
        } else {
            throw new ForbiddenException("접근할 수 없는 정보입니다.");
        }
    }

}
