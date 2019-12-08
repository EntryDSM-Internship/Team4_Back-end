package com.squeaker.entry.service;

import com.squeaker.entry.domain.entitys.User;
import com.squeaker.entry.domain.payload.request.UserSignUp;
import com.squeaker.entry.domain.payload.response.AuthCodeResponse;
import com.squeaker.entry.domain.payload.response.user.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    AuthCodeResponse authEmail(String email);
    void validEmail(String code);
    UserResponse getUser(Integer uuid);
    void signUp(UserSignUp userSignUp);
    void changeUser(User user, MultipartFile file);
    void deleteUser(String token);
}
