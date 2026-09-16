package com.orcamentos.orcamento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_orcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id")
    @JsonIgnore
    private Orcamento orcamento;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    public BigDecimal getSubtotal() {
        return quantidade.multiply(precoUnitario);
    }

}
