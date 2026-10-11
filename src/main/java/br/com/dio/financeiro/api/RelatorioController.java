package br.com.dio.financeiro.api;

import br.com.dio.financeiro.service.TransacaoService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    private final TransacaoService service;
    public RelatorioController(TransacaoService service) { this.service = service; }

    @GetMapping("/resumo") public Map<String,Object> resumo() { return service.resumo(); }
    @GetMapping("/categoria/{categoria}")
    public Map<String, Object> categoria(@PathVariable String categoria) {
        return Map.of("categoria", categoria, "totalDespesas", service.gastosPorCategoria(categoria).get("total"));
    }
}
