package com.squeaker.entry.dao;

import com.squeaker.entry.body.Token;
import com.squeaker.entry.body.UserSignIn;
import com.squeaker.entry.exception.InvalidTokenException;
import com.squeaker.entry.exception.UserNotFoundException;
import com.squeaker.entry.utils.JwtUtil;
import com.squeaker.entry.utils.Query;

import java.sql.SQLException;

public class AuthDAO extends Query {

    public Token signIn(UserSignIn userSignIn) {

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query(
                    "select uuid from user where userId='%s' and userPw='%s'",
                    userSignIn.getUserId(),
                    userSignIn.getUserPw()
            ));

            if (resultSet.next()) {
                int uuid = resultSet.getInt("uuid");
                String accessToken = JwtUtil.getAccessToken(uuid);
                String refreshToken = JwtUtil.getRefreshToken(uuid);

                statement.executeUpdate(query("update user set userRefreshToken = '%s' where uuid=%d", refreshToken, uuid));

                return new Token(accessToken, refreshToken);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new UserNotFoundException();
    }

    public Token refreshToken(String token) {

        try {
            String uuid = JwtUtil.parseToken(token);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query(
                    "select * from user where uuid=%s and userRefreshToken='%s'",
                    uuid,
                    token
            ));
            if(resultSet.next()) {

                String accessToken = JwtUtil.getAccessToken(uuid);
                String refreshToken = JwtUtil.getRefreshToken(uuid);

                return new Token(accessToken, refreshToken);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new InvalidTokenException();
    }
}
