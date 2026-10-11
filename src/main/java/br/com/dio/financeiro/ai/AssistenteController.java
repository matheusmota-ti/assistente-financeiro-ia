package br.com.dio.financeiro.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/assistente")
public class AssistenteController {
    private final ChatClient chatClient;
    private final FerramentasFinanceiras ferramentas;
    public AssistenteController(ChatClient.Builder builder, FerramentasFinanceiras ferramentas) {
        this.chatClient = builder.defaultSystem("Você é um assistente financeiro em português do Brasil. Seja claro e objetivo. Use as ferramentas para consultar ou registrar dados reais. Nunca diga que registrou uma transação sem confirmação da ferramenta. Não invente valores. Para registrar, confirme que existem tipo, descrição, categoria e valor.")
                .build();
        this.ferramentas = ferramentas;
    }

    @PostMapping("/perguntar")
    public Map<String,String> perguntar(@RequestBody Pergunta pergunta) {
        if (pergunta == null || pergunta.mensagem() == null || pergunta.mensagem().isBlank())
            throw new IllegalArgumentException("A mensagem não pode estar vazia.");
        String resposta = chatClient.prompt().user(pergunta.mensagem())
                .tools(ferramentas).call().content();
        return Map.of("resposta", resposta == null ? "Não consegui gerar uma resposta." : resposta);
    }
    public record Pergunta(String mensagem) {}
}
