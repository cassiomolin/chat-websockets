package com.cassiomolin.example.chat.security;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * Authentication filter for the chat endpoint. Only HTTP Basic Authentication is supported.
 *
 * @author cassiomolin
 */
@WebFilter("/chat/*")
public class AuthenticationFilter implements Filter {

    @Inject
    private Authenticator authenticator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getParameter("token");
        if (token == null || token.trim().isEmpty()) {
            forbidden(response, "An access token is required to connect");
            return;
        }

        try {
            String username = authenticator.validateAccessToken(token);
            filterChain.doFilter(new AuthenticatedRequest(request, username), servletResponse);
        } catch (Exception e) {
            forbidden(response, e.getMessage());
        }
    }

    private void forbidden(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }

    @Override
    public void destroy() {

    }

    /**
     * Wrapper for a {@link HttpServletRequest} which sets the authenticated user.
     *
     * @author cassiomolin
     */
    private static class AuthenticatedRequest extends HttpServletRequestWrapper {

        private String username;

        public AuthenticatedRequest(HttpServletRequest request, String username) {
            super(request);
            this.username = username;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> username;
        }
    }
}