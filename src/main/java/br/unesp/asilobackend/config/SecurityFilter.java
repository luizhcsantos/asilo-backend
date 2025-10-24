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

@Component // Anota como um componente Spring para poder ser injetado
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = this.recoverToken(request);

        if (token != null) {
            // Esta é uma lógica simples para "decodificar" os tokens
            // (ex: "token-doador-1" ou "token-admin-2")
            try {
                String[] parts = token.split("-");
                String role = parts[1]; // 'doador' ou 'admin'
                String id = parts[2];   // '1' ou '2'

                List<GrantedAuthority> authorities = new ArrayList<>();

                // Adicionamos a "ROLE_" que o Spring Security espera
                if ("admin".equals(role)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_DOADOR"));
                }

                // Criamos o objeto de autenticação
                // O 'id' do usuário será o "principal" (identificador)
                var authentication = new UsernamePasswordAuthenticationToken(id, null, authorities);

                // Colocamos o usuário no Contexto de Segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Se o token for inválido (ex: "Bearer null" ou mal formatado)
                SecurityContextHolder.clearContext();
            }
        }

        // Continua para o próximo filtro
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token do cabeçalho
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        // Remove o prefixo "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}