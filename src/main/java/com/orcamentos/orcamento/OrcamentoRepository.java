package com.orcamentos.orcamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    Optional<Orcamento> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Orcamento> findByTokenPublico(String tokenPublico);

    List<Orcamento> findByEmpresaIdAndStatusAndClienteIdOrderByIdDesc(Long empresaId, StatusOrcamento status, Long clienteId);

    List<Orcamento> findByEmpresaIdAndStatusOrderByIdDesc(Long empresaId, StatusOrcamento status);

    List<Orcamento> findByEmpresaIdAndClienteIdOrderByIdDesc(Long empresaId, Long clienteId);

    List<Orcamento> findByEmpresaIdOrderByIdDesc(Long empresaId);

    long countByEmpresaId(Long empresaId);

    long countByEmpresaIdAndStatusIn(Long empresaId, List<StatusOrcamento> status);

    @Query("select coalesce(sum(o.total), 0) from Orcamento o where o.empresaId = :empresaId and o.status <> 'RASCUNHO'")
    BigDecimal somarValorEnviado(@Param("empresaId") Long empresaId);

    @Query("select coalesce(sum(o.total), 0) from Orcamento o where o.empresaId = :empresaId and o.status = 'APROVADO'")
    BigDecimal somarValorAprovado(@Param("empresaId") Long empresaId);

    long countByEmpresaIdAndStatus(Long empresaId, StatusOrcamento status);

}
