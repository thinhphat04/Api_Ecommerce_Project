//package com.phat.api_flutter.service;
//
//import com.phat.api_flutter.models.Token;
//import com.phat.api_flutter.repository.TokenRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private TokenRepository tokenRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//        final String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        final String accessToken = authHeader.replace("Bearer", "").trim();
//        Token token = tokenRepository.findByAccessToken(accessToken).orElse(null);
//
//        if (token == null) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        Claims claims = Jwts.parser()
//                .setSigningKey("your_secret_key")
//                .parseClaimsJws(accessToken)
//                .getBody();
//
//        if (claims.get("isAdmin", Boolean.class) && request.getRequestURI().startsWith("/api/v1/admin/")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Set authentication in context
//        // ...
//
//        chain.doFilter(request, response);
//    }
//}
