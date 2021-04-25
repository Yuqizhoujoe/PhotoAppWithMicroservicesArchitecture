package com.appsdeveloperblog.photoapp.api.gatewayc.security;

import com.appsdeveloperblog.photoapp.api.gatewayc.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

public class AuthenticationFilter extends BasicAuthenticationFilter {

    private Environment env;

    private JwtUtil jwtUtil = new JwtUtil();

    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env) {
        super(authenticationManager); // this authenticationManager comes from spring security
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = req.getHeader(env.getProperty("authorization.token.header.name"));

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
            // if the authorizationHeader is null or authorizationHeader does not start with Bearer
            // we just simply call the next filter in the chain
            chain.doFilter(req,res);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(req,res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {

        String authorizationHeader = req.getHeader(env.getProperty("authorization.token.header.name"));

        String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "");

        String userId = Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        if (userId == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
    }
}
