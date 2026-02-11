package com.project.backend.controller.user;

import com.project.backend.dto.auth.response.AuthResponse;
import com.project.backend.entity.User;
import com.project.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService service;

    @GetMapping("/me")
    public ResponseEntity<User> displayUserInformation(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.displayUserInfo(email));
    }
}
