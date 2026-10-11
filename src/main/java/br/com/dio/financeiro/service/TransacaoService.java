package br.com.dio.financeiro.service;

import br.com.dio.financeiro.api.NovaTransacao;
import br.com.dio.financeiro.domain.*;
import br.com.dio.financeiro.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransacaoService {
    private final TransacaoRepository repository;
    public TransacaoService(TransacaoRepository repository) { this.repository = repository; }

    @Transactional
    public Transacao criar(NovaTransacao entrada) {
        if (entrada == null || entrada.tipo() == null) throw new IllegalArgumentException("Informe receita ou despesa.");
        if (entrada.descricao() == null || entrada.descricao().isBlank()) throw new IllegalArgumentException("A descrição é obrigatória.");
        if (entrada.categoria() == null || entrada.categoria().isBlank()) throw new IllegalArgumentException("A categoria é obrigatória.");
        if (entrada.valor() == null || entrada.valor().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("O valor deve ser maior que zero.");
        LocalDate data = entrada.data() == null ? LocalDate.now() : entrada.data();
        return repository.save(new Transacao(entrada.tipo(), entrada.descricao().trim(),
                entrada.categoria().trim(), entrada.valor(), data));
    }
    public List<Transacao> listar() { return repository.findAll(); }
    public Optional<Transacao> buscar(Long id) { return repository.findById(id); }
    public void excluir(Long id) { repository.deleteById(id); }

    public Map<String, Object> resumo() {
        BigDecimal receitas = BigDecimal.ZERO, despesas = BigDecimal.ZERO;
        Map<String, BigDecimal> categorias = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Transacao t : repository.findAll()) {
            if (t.getTipo() == TipoTransacao.RECEITA) receitas = receitas.add(t.getValor());
            else {
                despesas = despesas.add(t.getValor());
                categorias.merge(t.getCategoria(), t.getValor(), BigDecimal::add);
            }
        }
        Map<String,Object> resultado = new LinkedHashMap<>();
        resultado.put("totalReceitas", receitas);
        resultado.put("totalDespesas", despesas);
        resultado.put("saldo", receitas.subtract(despesas));
        resultado.put("despesasPorCategoria", categorias);
        return resultado;
    }
    public Map<String, BigDecimal> gastosPorCategoria(String categoria) {
        BigDecimal total = repository.findByCategoriaIgnoreCase(categoria).stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        return Map.of("categoria", new BigDecimal("0.00"), "total", total);
    }
}
