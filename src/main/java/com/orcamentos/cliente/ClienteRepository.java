package com.orcamentos.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByIdAndEmpresaId(Long id, Long empresaId);

    List<Cliente> findByEmpresaIdAndNomeContainingIgnoreCaseOrderByNomeAsc(Long empresaId, String nome);

    List<Cliente> findByEmpresaIdOrderByNomeAsc(Long empresaId);

    void deleteByIdAndEmpresaId(Long id, Long empresaId);

}
