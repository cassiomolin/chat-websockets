package com.cassiomolin.example.chat.security;


import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

/**
 * Authentication filter for the chat endpoint. Only HTTP Basic Authentication is supported.
 *
 * @author cassiomolin
 */
@WebFilter("/chat/*")
public class AuthenticationFilter implements Filter {

    @Inject
    private Authenticator authenticator;

    private static final String REALM = "chat";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            unauthorized(response, "Authorization header not found");
            return;
        }

        String[] authenticationToken = authorizationHeader.split(" ");
        if (authenticationToken.length != 2) {
            unauthorized(response, "Invalid Authorization header");
            return;
        }

        if (!"Basic".equalsIgnoreCase(authenticationToken[0])) {
            unauthorized(response, "Unsupported authentication schema");
            return;
        }

        String credentialsToken = new String(Base64.getDecoder().decode(authenticationToken[1]));
        String[] credentials = credentialsToken.split(":");
        if (credentials.length != 2) {
            unauthorized(response, "Invalid credentials token");
            return;
        }

        if (!authenticator.checkCredentials(credentials[0], credentials[1])) {
            unauthorized(response, "Bad credentials");
            return;
        }

        filterChain.doFilter(new AuthenticatedRequest(request, credentials[0]), servletResponse);
    }


    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + REALM + "\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
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