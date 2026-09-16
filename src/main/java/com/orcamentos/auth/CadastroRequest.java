package com.orcamentos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroRequest(

        @NotBlank(message = "Informe seu nome")
        String nome,

        @NotBlank(message = "Informe um e-mail")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Informe uma senha")
        @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres")
        String senha,

        @NotBlank(message = "Informe o nome da empresa")
        String nomeEmpresa
) {
}
