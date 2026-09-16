package com.orcamentos.cliente;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank(message = "Informe o nome do cliente")
        String nome,
        String telefone,
        String email
) {
}
