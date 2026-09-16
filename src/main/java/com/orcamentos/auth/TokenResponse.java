package com.orcamentos.auth;

public record TokenResponse(
        String accessToken,
        Long usuarioId,
        Long empresaId,
        String nome
) {
}
