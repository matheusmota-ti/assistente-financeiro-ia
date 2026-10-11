package br.com.dio.financeiro.ai;

import br.com.dio.financeiro.api.NovaTransacao;
import br.com.dio.financeiro.domain.TipoTransacao;
import br.com.dio.financeiro.service.TransacaoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
public class FerramentasFinanceiras {
    private final TransacaoService service;
    public FerramentasFinanceiras(TransacaoService service) { this.service = service; }

    @Tool(description = "Registra uma despesa ou receita. Use somente quando o usuário informar tipo, descrição, categoria e valor. Data no formato ISO yyyy-MM-dd; se não informada, use a data de hoje.")
    public String registrarTransacao(String tipo, String descricao, String categoria, double valor, String data) {
        try {
            TipoTransacao tipoEnum = TipoTransacao.valueOf(tipo.toUpperCase());
            LocalDate dataTransacao = (data == null || data.isBlank()) ? LocalDate.now() : LocalDate.parse(data);
            var criada = service.criar(new NovaTransacao(tipoEnum, descricao, categoria, BigDecimal.valueOf(valor), dataTransacao));
            return "Transação registrada com sucesso. ID " + criada.getId() + ", " + criada.getTipo() + " de R$ " + criada.getValor() + " em " + criada.getCategoria() + ".";
        } catch (RuntimeException e) {
            return "Não foi possível registrar: " + e.getMessage();
        }
    }

    @Tool(description = "Consulta o resumo financeiro com receitas, despesas, saldo e gastos agrupados por categoria.")
    public Map<String,Object> consultarResumo() { return service.resumo(); }

    @Tool(description = "Consulta o total de despesas de uma categoria, por exemplo alimentação, transporte ou lazer.")
    public Map<String,Object> consultarGastosCategoria(String categoria) {
        return Map.of("categoria", categoria, "totalDespesas", service.gastosPorCategoria(categoria).get("total"));
    }
}
