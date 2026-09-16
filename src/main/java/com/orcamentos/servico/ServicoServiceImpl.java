package com.orcamentos.servico;

import com.orcamentos.comum.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoServiceImpl implements ServicoService {

    private final ServicoRepository servicoRepository;

    @Override
    public Servico criar(Long empresaId, ServicoRequest request) {
        Servico servico = new Servico();
        servico.setEmpresaId(empresaId);
        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPreco(request.preco());
        servico.setUnidade(request.unidade());
        return servicoRepository.save(servico);
    }

    @Override
    public Servico atualizar(Long empresaId, Long servicoId, ServicoRequest request) {
        Servico servico = servicoRepository.findByIdAndEmpresaId(servicoId, empresaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado."));
        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPreco(request.preco());
        servico.setUnidade(request.unidade());
        return servicoRepository.save(servico);
    }

    @Override
    public List<Servico> listar(Long empresaId, String filtroNome) {
        if (filtroNome == null || filtroNome.isBlank()) {
            return servicoRepository.findByEmpresaIdOrderByNomeAsc(empresaId);
        }
        return servicoRepository.findByEmpresaIdAndNomeContainingIgnoreCaseOrderByNomeAsc(empresaId, filtroNome);
    }

    @Override
    public void excluir(Long empresaId, Long servicoId) {
        servicoRepository.findByIdAndEmpresaId(servicoId, empresaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado."));
        servicoRepository.deleteByIdAndEmpresaId(servicoId, empresaId);
    }

}
