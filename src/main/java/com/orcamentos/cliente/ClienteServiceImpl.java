package com.orcamentos.cliente;

import com.orcamentos.comum.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente criar(Long empresaId, ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setEmpresaId(empresaId);
        cliente.setNome(request.nome());
        cliente.setTelefone(request.telefone());
        cliente.setEmail(request.email());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente atualizar(Long empresaId, Long clienteId, ClienteRequest request) {
        Cliente cliente = buscarPorId(empresaId, clienteId);
        cliente.setNome(request.nome());
        cliente.setTelefone(request.telefone());
        cliente.setEmail(request.email());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorId(Long empresaId, Long clienteId) {
        return clienteRepository.findByIdAndEmpresaId(clienteId, empresaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
    }

    @Override
    public List<Cliente> listar(Long empresaId, String filtroNome) {
        if (filtroNome == null || filtroNome.isBlank()) {
            return clienteRepository.findByEmpresaIdOrderByNomeAsc(empresaId);
        }
        return clienteRepository.findByEmpresaIdAndNomeContainingIgnoreCaseOrderByNomeAsc(empresaId, filtroNome);
    }

    @Override
    public void excluir(Long empresaId, Long clienteId) {
        buscarPorId(empresaId, clienteId);
        clienteRepository.deleteByIdAndEmpresaId(clienteId, empresaId);
    }

}
