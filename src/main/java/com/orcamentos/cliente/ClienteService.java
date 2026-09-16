package com.orcamentos.cliente;

import java.util.List;

public interface ClienteService {

    Cliente criar(Long empresaId, ClienteRequest request);

    Cliente atualizar(Long empresaId, Long clienteId, ClienteRequest request);

    Cliente buscarPorId(Long empresaId, Long clienteId);

    List<Cliente> listar(Long empresaId, String filtroNome);

    void excluir(Long empresaId, Long clienteId);

}
