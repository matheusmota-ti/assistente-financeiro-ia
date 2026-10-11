package br.com.dio.financeiro.api;

import br.com.dio.financeiro.domain.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDate;

public record NovaTransacao(TipoTransacao tipo, String descricao, String categoria,
                            BigDecimal valor, LocalDate data) {}
