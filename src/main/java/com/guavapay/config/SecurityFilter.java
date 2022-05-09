package com.guavapay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guavapay.cache.TokenStorage;
import com.guavapay.config.i18n.Translator;
import com.guavapay.constant.Constants;
import com.guavapay.model.dto.RestBaseError;
import com.guavapay.model.dto.TokenPair;
import com.guavapay.security.Principal;
import com.guavapay.security.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;
    private final TokenStorage tokenStorage;
    private final Translator translator;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filter) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);

        if (StringUtils.hasText(jwt)) {
            try {
                tokenProvider.validateToken(jwt);
            } catch (ExpiredJwtException e) {
                returnResponse(HttpStatus.NOT_ACCEPTABLE, servletResponse);
                return;
            } catch (Exception e) {
                returnResponse(HttpStatus.UNAUTHORIZED, servletResponse);
                return;
            }
            try {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                TokenPair tokenPair = tokenStorage.read((Principal) authentication.getPrincipal());
                if (Objects.isNull(tokenPair.getAccessToken())) returnResponse(HttpStatus.NOT_ACCEPTABLE, servletResponse);
            } catch (Exception e) {
                returnResponse(HttpStatus.NOT_ACCEPTABLE, servletResponse);
                return;
            }
        }
        filter.doFilter(servletRequest, servletResponse);
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ?
                bearerToken.substring(7) : null;
    }

    private void returnResponse(HttpStatus status, ServletResponse response) throws IOException {
        RestBaseError errorResponse = new RestBaseError(Constants.INVALID_TOKEN, translator.toLocale(Constants.INVALID_TOKEN));
        byte[] responseToSend = new ObjectMapper().writeValueAsString(errorResponse).getBytes(StandardCharsets.UTF_8);
        ((HttpServletResponse) response).setHeader("Content-Type", "application/json; charset=UTF-8");
        ((HttpServletResponse) response).setStatus(status.value());
        response.getOutputStream().write(responseToSend);
    }
}
