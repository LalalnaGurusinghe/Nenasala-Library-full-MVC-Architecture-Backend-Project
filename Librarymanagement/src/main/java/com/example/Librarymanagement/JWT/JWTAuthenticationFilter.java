package com.example.Librarymanagement.JWT;

import com.example.Librarymanagement.repo.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepo userRepo;

    public JWTAuthenticationFilter(JWTService jwtService, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        //check if Authorization header is present and starts with "Bearer "
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extract JWT token from the header
        jwtToken = authHeader.substring(7);
        username = jwtService.extractUsername(jwtToken);

        //check if we have a username and no authentication exist yet
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //get the details from data base for particular user
            var userDetails = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found with username: " + username));

            //Validate the token
            if(jwtService.isTokenValid(jwtToken , userDetails)){

                //create the authentication with user roles
                List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );

                //set authentication details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //update security context with authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request, response);
    }

}
