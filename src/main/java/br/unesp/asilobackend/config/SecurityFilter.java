package br.unesp.asilobackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component // Anota como um componente Spring para ser injetado no SecurityConfig
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = this.recoverToken(request);

        if (token != null) {
            // Lógica para "decodificar" os tokens (ex: "token-doador-1")
            try {
                String[] parts = token.split("-");
                String role = parts[1]; // 'doador' ou 'admin'
                String id = parts[2];   // '1', '2', etc.

                List<GrantedAuthority> authorities = new ArrayList<>();

                // Adicionamos a "ROLE_" que o Spring Security espera
                if ("admin".equals(role)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_DOADOR"));
                }

                var authentication = new UsernamePasswordAuthenticationToken(id, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Token mal formatado
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token do cabeçalho "Authorization"
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        // Remove o prefixo "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}