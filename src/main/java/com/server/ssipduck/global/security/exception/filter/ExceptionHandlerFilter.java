package com.server.ssipduck.global.security.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.ssipduck.global.exception.ExceptionCode;
import com.server.ssipduck.global.exception.response.ExceptionResponseEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.debug("caused at 'ExceptionHandlerFilter.class' throw ExpiredJwtException");
            setErrorResponse(ExceptionCode.TOKEN_EXPIRED, response);
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("caused at 'ExceptionHandlerFilter.class' throw JwtException");
            setErrorResponse(ExceptionCode.TOKEN_INVALID, response);
        } catch (Exception ex) {
            log.debug("caused at 'ExceptionHandlerFilter.class' throw Exception");
            setErrorResponse(ExceptionCode.UNKNOWN_ERROR, response);
        }
    }

    public void setErrorResponse(ExceptionCode exceptionCode, HttpServletResponse response) throws IOException {
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType("application/json; charset=utf-8");

        ExceptionResponseEntity errorResponse = ExceptionResponseEntity.of(exceptionCode);
        String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseEntityToJson);
    }

}