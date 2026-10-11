package br.com.dio.financeiro.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.audio.transcription.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.audio.transcription.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AudioAssistenteService {
    private final OpenAiAudioTranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final FerramentasFinanceiras ferramentas;
    private final SpeechModel speechModel;

    public AudioAssistenteService(OpenAiAudioTranscriptionModel transcriptionModel,
            ChatClient.Builder builder, FerramentasFinanceiras ferramentas, SpeechModel speechModel) {
        this.transcriptionModel = transcriptionModel;
        this.chatClient = builder.defaultSystem("Você é um assistente financeiro em português do Brasil. "
                + "Use as ferramentas para consultar ou registrar dados reais. Nunca diga que registrou "
                + "uma transação sem confirmação da ferramenta. Não invente valores. Se faltarem dados "
                + "essenciais, faça uma pergunta.").build();
        this.ferramentas = ferramentas;
        this.speechModel = speechModel;
    }

    public ResultadoAudio processar(MultipartFile arquivo) throws Exception {
        if (arquivo == null || arquivo.isEmpty())
            throw new IllegalArgumentException("Envie um arquivo de áudio no campo 'file'.");
        if (arquivo.getSize() > 20L * 1024 * 1024)
            throw new IllegalArgumentException("O áudio deve ter no máximo 20 MB.");

        String nome = arquivo.getOriginalFilename() == null ? "audio.mp3" : arquivo.getOriginalFilename();
        ByteArrayResource resource = new ByteArrayResource(arquivo.getBytes()) {
            @Override public String getFilename() { return nome; }
        };
        var opcoes = OpenAiAudioTranscriptionOptions.builder()
                .language("pt").responseFormat(TranscriptResponseFormat.TEXT).temperature(0f).build();
        AudioTranscriptionResponse transcricao = transcriptionModel.call(new AudioTranscriptionPrompt(resource, opcoes));
        String texto = transcricao.getResult().getOutput();
        if (texto == null || texto.isBlank())
            throw new IllegalArgumentException("Não foi possível identificar fala no áudio.");

        String resposta = chatClient.prompt().user(texto).tools(ferramentas).call().content();
        if (resposta == null || resposta.isBlank())
            resposta = "Não consegui gerar uma resposta para esse comando.";
        SpeechResponse fala = speechModel.call(new SpeechPrompt(resposta));
        return new ResultadoAudio(texto, resposta, fala.getResult().getOutput());
    }

    public record ResultadoAudio(String transcricao, String resposta, byte[] audio) {}
}
