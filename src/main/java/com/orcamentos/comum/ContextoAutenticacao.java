package com.orcamentos.comum;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Le os dados do usuario autenticado (empresaId e usuarioId) a partir
 * do token JWT decodificado pelo filtro de seguranca.
 */
public class ContextoAutenticacao {

    private ContextoAutenticacao() {
    }

    public static UsuarioAutenticado usuarioAtual() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UsuarioAutenticado usuarioAutenticado) {
            return usuarioAutenticado;
        }
        throw new IllegalStateException("Nenhum usuario autenticado no contexto atual.");
    }

    public static Long empresaId() {
        return usuarioAtual().empresaId();
    }

    public record UsuarioAutenticado(Long usuarioId, Long empresaId, String email) {
    }

}
