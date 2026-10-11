package br.com.dio.financeiro.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Transacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private TipoTransacao tipo;
    @Column(nullable = false, length = 100)
    private String descricao;
    @Column(nullable = false, length = 60)
    private String categoria;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private LocalDateTime criadoEm;

    protected Transacao() {}
    public Transacao(TipoTransacao tipo, String descricao, String categoria, BigDecimal valor, LocalDate data) {
        this.tipo = tipo; this.descricao = descricao; this.categoria = categoria;
        this.valor = valor; this.data = data; this.criadoEm = LocalDateTime.now();
    }
    public Long getId() { return id; }
    public TipoTransacao getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getData() { return data; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
