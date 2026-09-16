package com.orcamentos.orcamento;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orcamentos")
@Getter
@Setter
@NoArgsConstructor
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long empresaId;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento status = StatusOrcamento.RASCUNHO;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemOrcamento> itens = new ArrayList<>();

    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;

    private LocalDate validade;
    private String prazoExecucao;
    private String condicoesPagamento;

    @Column(length = 2000)
    private String observacoes;

    @Column(unique = true, nullable = false)
    private String tokenPublico;

    private LocalDateTime enviadoEm;
    private LocalDateTime visualizadoEm;
    private LocalDateTime respondidoEm;

    @Column(length = 1000)
    private String observacaoRecusa;

    public void definirItens(List<ItemOrcamento> novosItens) {
        this.itens.clear();
        for (ItemOrcamento item : novosItens) {
            item.setOrcamento(this);
            this.itens.add(item);
        }
    }

}
