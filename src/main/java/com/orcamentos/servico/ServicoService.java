package com.orcamentos.servico;

import java.util.List;

public interface ServicoService {

    Servico criar(Long empresaId, ServicoRequest request);

    Servico atualizar(Long empresaId, Long servicoId, ServicoRequest request);

    List<Servico> listar(Long empresaId, String filtroNome);

    void excluir(Long empresaId, Long servicoId);

}
