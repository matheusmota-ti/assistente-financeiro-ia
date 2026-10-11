package br.com.dio.financeiro.ai;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/assistente")
public class AudioAssistenteController {
    private final AudioAssistenteService service;
    public AudioAssistenteController(AudioAssistenteService service) { this.service = service; }

    @PostMapping(value = "/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mpeg")
    public ResponseEntity<byte[]> processarAudio(@RequestPart("file") MultipartFile file) throws Exception {
        var resultado = service.processar(file);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("audio/mpeg"))
                .header("X-Transcricao", limparHeader(resultado.transcricao()))
                .header("X-Resposta", limparHeader(resultado.resposta()))
                .body(resultado.audio());
    }

    private String limparHeader(String texto) {
        return texto.replace("\r", " ").replace("\n", " ");
    }
}
