package com.orcamentos.auth;

public interface AuthService {

    TokenResponse cadastrar(CadastroRequest request);

    TokenResponse autenticar(LoginRequest request);

}
