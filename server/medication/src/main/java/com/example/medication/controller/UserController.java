package com.example.medication.controller;

import lombok.RequiredArgsConstructor;
import com.example.medication.dto.UserRegisterRequest;
import com.example.medication.entity.User;
import com.example.medication.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.example.medication.dto.UserLoginRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(
            @RequestBody UserRegisterRequest request
    ) {
        return userService.register(request);
    }
    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest request) {
        return userService.login(request);
    }
    @GetMapping("/admin/check")
    public String checkAdmin(
            @RequestParam Long adminId
    ) {
        userService.checkAdmin(adminId);

        return "관리자 권한 확인 성공";
    }
    @GetMapping("/admin/list")
    public List<User> getUserList(
            @RequestParam Long adminId
    ) {
        return userService.getUserList(adminId);
    }
    @PatchMapping("/admin/{targetUserId}/role")
    public User updateUserRole(
            @RequestParam Long adminId,
            @PathVariable Long targetUserId,
            @RequestParam String role
    ) {
        return userService.updateUserRole(
                adminId,
                targetUserId,
                role
        );
    }
    @DeleteMapping("/admin/{targetUserId}")
    public String deleteUserByAdmin(
            @RequestParam Long adminId,
            @PathVariable Long targetUserId
    ) {
        userService.deleteUserByAdmin(adminId, targetUserId);

        return "사용자 삭제 성공";
    }
    @PatchMapping("/admin/{targetUserId}/review-block")
    public User updateReviewBlocked(
            @RequestParam Long adminId,
            @PathVariable Long targetUserId,
            @RequestParam Boolean blocked
    ) {
        return userService.updateReviewBlocked(
                adminId,
                targetUserId,
                blocked
        );
    }

}