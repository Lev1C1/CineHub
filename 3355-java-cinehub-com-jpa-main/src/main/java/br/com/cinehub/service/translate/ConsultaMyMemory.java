package br.com.cinehub.service.translate;

import br.com.cinehub.service.ConsumoApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class    ConsultaMyMemory {

    public static String obterTraducao(String text) {
        ObjectMapper mapper = new ObjectMapper();
        ConsumoApi consumo = new ConsumoApi();

        // Encode do texto e do idioma
        String texto = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String langpair = URLEncoder.encode("en|pt-br", StandardCharsets.UTF_8);

        String url = "https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair;

        String json = consumo.obterDados(url);

        DadosTraducao traducao;
        try {
            traducao = mapper.readValue(json, DadosTraducao.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar JSON da tradução: " + e.getMessage(), e);
        }

        return traducao.dadosResposta().textoTraduzido();
    }
}
