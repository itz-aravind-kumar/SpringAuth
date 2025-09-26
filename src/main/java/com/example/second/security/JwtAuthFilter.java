package com.example.second.security;
import com.example.second.model.User;
import com.example.second.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired private JwtService jwtService;
    @Autowired private UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
                                    throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null, subject = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            subject = jwtService.extractSubject(jwt);
        }

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findByEmail(subject); // or findById for UUID subject
            if (user != null && jwtService.isTokenValid(jwt, subject)) {
                String role = user.getRole();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.replace("ROLE_", ""));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, Collections.singletonList(authority));
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
