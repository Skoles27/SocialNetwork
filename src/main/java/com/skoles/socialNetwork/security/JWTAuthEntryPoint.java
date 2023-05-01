package com.skoles.socialNetwork.security;

import com.google.gson.Gson;
import com.skoles.socialNetwork.payload.response.InvalidSignInResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        InvalidSignInResponse signInResponse = new InvalidSignInResponse();
        String jsonSignInResponse = new Gson().toJson(signInResponse);
        response.setContentType(SecurityConst.CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(jsonSignInResponse);
    }
}
