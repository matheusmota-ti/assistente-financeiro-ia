package br.com.dio.financeiro.api;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class ErroController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> argumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }
}
