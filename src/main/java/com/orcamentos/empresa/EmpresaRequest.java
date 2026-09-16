package com.orcamentos.empresa;

import jakarta.validation.constraints.NotBlank;

public record EmpresaRequest(
        @NotBlank(message = "Informe o nome da empresa")
        String nome,
        String documento,
        String telefone,
        String email,
        String endereco
) {
}
