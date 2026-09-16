package com.orcamentos.servico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    Optional<Servico> findByIdAndEmpresaId(Long id, Long empresaId);

    List<Servico> findByEmpresaIdAndNomeContainingIgnoreCaseOrderByNomeAsc(Long empresaId, String nome);

    List<Servico> findByEmpresaIdOrderByNomeAsc(Long empresaId);

    void deleteByIdAndEmpresaId(Long id, Long empresaId);

}
