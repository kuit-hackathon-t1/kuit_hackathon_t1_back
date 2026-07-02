package com.example.kuit_hackathon_back.domain.user.controller;

import com.example.kuit_hackathon_back.domain.user.dto.LoginRequest;
import com.example.kuit_hackathon_back.domain.user.dto.LoginResponse;
import com.example.kuit_hackathon_back.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UserService.LoginResult result = userService.login(request.nickname());
        LoginResponse response = LoginResponse.from(result.user());
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
}
