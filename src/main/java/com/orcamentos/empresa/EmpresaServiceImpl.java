package com.orcamentos.empresa;

import com.orcamentos.comum.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    @Override
    public Empresa buscarPorId(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa não encontrada."));
    }

    @Override
    public Empresa atualizar(Long empresaId, EmpresaRequest request) {
        Empresa empresa = buscarPorId(empresaId);
        empresa.setNome(request.nome());
        empresa.setDocumento(request.documento());
        empresa.setTelefone(request.telefone());
        empresa.setEmail(request.email());
        empresa.setEndereco(request.endereco());
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa atualizarLogotipo(Long empresaId, String logotipoUrl) {
        Empresa empresa = buscarPorId(empresaId);
        empresa.setLogotipoUrl(logotipoUrl);
        return empresaRepository.save(empresa);
    }

}
