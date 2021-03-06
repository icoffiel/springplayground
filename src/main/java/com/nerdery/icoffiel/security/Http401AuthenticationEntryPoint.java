package com.nerdery.icoffiel.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized).
 */
@Component
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        log.debug("Pre-authenticated entry point called. Rejecting access.");

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}
