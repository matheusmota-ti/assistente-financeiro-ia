package br.com.dio.financeiro.repository;

import br.com.dio.financeiro.domain.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByCategoriaIgnoreCase(String categoria);
    List<Transacao> findByDataBetween(LocalDate inicio, LocalDate fim);
}
