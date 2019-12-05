package com.squeaker.entry.service;

import com.squeaker.entry.domain.entitys.User;
import com.squeaker.entry.domain.payload.response.TokenResponse;
import com.squeaker.entry.domain.repository.AuthRepository;
import com.squeaker.entry.exception.InvalidTokenException;
import com.squeaker.entry.exception.UserNotFoundException;
import com.squeaker.entry.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthRepository authRepository;

    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public TokenResponse login(String userId, String userPw) {

        User user = authRepository.findByUserIdAndUserPw(userId, userPw);
        if(user == null) throw new UserNotFoundException();

        return new TokenResponse(user.getUserId());
    }

    @Override
    public TokenResponse refreshToken(String token) {
        int uuid = Integer.parseInt(JwtUtil.parseToken(token));
        User user = authRepository.findByUuidAndUserRefreshToken(uuid, token);
        if(user == null || !user.getUserRefreshToken().equals(token)) throw new InvalidTokenException();

        return new TokenResponse(uuid);
    }
}
