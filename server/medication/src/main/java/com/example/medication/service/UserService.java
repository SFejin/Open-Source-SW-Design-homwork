package com.example.medication.service;

import lombok.RequiredArgsConstructor;
import com.example.medication.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.medication.dto.UserRegisterRequest;
import com.example.medication.entity.User;
import com.example.medication.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.medication.dto.UserLoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    public User register(UserRegisterRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .reviewBlocked(false)
                .build();

        return userRepository.save(user);
    }

    public User login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
    public void checkAdmin(Long adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Admin permission required");
        }
    }
    public List<User> getUserList(Long adminId) {

        checkAdmin(adminId);

        return userRepository.findAll();
    }
    public User updateUserRole(
            Long adminId,
            Long targetUserId,
            String role
    ) {
        checkAdmin(adminId);

        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Invalid role");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        targetUser.setRole(role);

        return userRepository.save(targetUser);
    }
    @Transactional
    public void deleteUserByAdmin(
            Long adminId,
            Long targetUserId
    ) {
        checkAdmin(adminId);

        if (adminId.equals(targetUserId)) {
            throw new RuntimeException("Admin cannot delete own account");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if ("ADMIN".equals(targetUser.getRole())) {
            throw new RuntimeException("Admin account cannot be deleted");
        }

        reviewRepository.deleteByUser_UserId(targetUserId);

        userRepository.delete(targetUser);
    }
    public User updateReviewBlocked(
            Long adminId,
            Long targetUserId,
            Boolean blocked
    ) {
        checkAdmin(adminId);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        targetUser.setReviewBlocked(blocked);

        return userRepository.save(targetUser);
    }

}

