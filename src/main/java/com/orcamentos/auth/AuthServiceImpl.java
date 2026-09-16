package com.orcamentos.auth;

import com.orcamentos.comum.CredenciaisInvalidasException;
import com.orcamentos.comum.RegraDeNegocioException;
import com.orcamentos.config.JwtService;
import com.orcamentos.empresa.Empresa;
import com.orcamentos.empresa.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public TokenResponse cadastrar(CadastroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraDeNegocioException("Já existe uma conta com este e-mail.");
        }

        Empresa empresa = empresaRepository.save(new Empresa(request.nomeEmpresa()));

        Usuario usuario = new Usuario();
        usuario.setEmpresaId(empresa.getId());
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario = usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario.getId(), empresa.getId(), usuario.getEmail());
        return new TokenResponse(token, usuario.getId(), empresa.getId(), usuario.getNome());
    }

    @Override
    public TokenResponse autenticar(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getEmpresaId(), usuario.getEmail());
        return new TokenResponse(token, usuario.getId(), usuario.getEmpresaId(), usuario.getNome());
    }

}
