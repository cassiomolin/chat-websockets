package com.cassiomolin.example.chat.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {

    @Inject
    private Authenticator authenticator;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Credentials credentials = mapper.readValue(req.getReader(), Credentials.class);
        if (authenticator.checkCredentials(credentials.getUsername(), credentials.getPassword())) {

            String token = authenticator.issuesAccessToken(credentials.getUsername());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), new WebSocketToken(token));

        } else {

            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("text/plain");
            resp.getWriter().write("Invalid credentials");

        }
    }

    private static class Credentials {

        private String username;

        private String password;

        public Credentials() {

        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private static class WebSocketToken {

        private String token;

        public WebSocketToken() {

        }

        public WebSocketToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}