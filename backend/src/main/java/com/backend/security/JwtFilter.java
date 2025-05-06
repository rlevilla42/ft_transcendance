package com.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.repository.UserRepository;
import com.backend.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;


@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

@Override
protected void doFilterInternal(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull FilterChain filterChain)
                                throws ServletException, IOException {

    // 🔍 Récupère le header "Authorization" envoyé avec la requête HTTP
    String authHeader = request.getHeader("Authorization");

    // ✅ Si le header commence par "Bearer ", alors on suppose qu'un token est présent
    if (authHeader != null && authHeader.startsWith("Bearer ")) {

        // 🔪 On enlève "Bearer " pour ne garder que le token
        String jwt = authHeader.substring(7);

        // 🧠 On extrait le username à partir du token (JWT contient ça dans son payload)
        String username = jwtUtil.extractUsername(jwt);

        // 👮 Si l'utilisateur n'est pas déjà authentifié dans ce contexte
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 📦 On va chercher l'utilisateur correspondant dans la BDD
            User user = userRepository.findByUsername(username).orElseThrow(()
            -> new UsernameNotFoundException("User not found"));

            // 🔐 Si le token est bien valide et correspond à cet utilisateur
            if (jwtUtil.validateToken(jwt, user)) {

                // ✅ Crée un "jeton d'identité" que Spring Security pourra reconnaître
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, null);

                // 🔗 Ajoute les infos de la requête HTTP (comme l'IP, User-Agent, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 📌 Place ce jeton dans le contexte global de Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }

    // 🚦 On laisse passer la requête vers la suite (autres filtres / contrôleurs)
    filterChain.doFilter(request, response);
};

}
