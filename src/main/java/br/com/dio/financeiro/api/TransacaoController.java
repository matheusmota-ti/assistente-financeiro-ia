package br.com.dio.financeiro.api;

import br.com.dio.financeiro.domain.Transacao;
import br.com.dio.financeiro.service.TransacaoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {
    private final TransacaoService service;
    public TransacaoController(TransacaoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Transacao> criar(@RequestBody NovaTransacao entrada) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(entrada));
    }
    @GetMapping public List<Transacao> listar() { return service.listar(); }
    @GetMapping("/{id}") public ResponseEntity<Transacao> buscar(@PathVariable Long id) {
        return service.buscar(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (service.buscar(id).isEmpty()) return ResponseEntity.notFound().build();
        service.excluir(id); return ResponseEntity.noContent().build();
    }
}
