package br.com.cinehub.service.translate.tmdb;

import br.com.cinehub.service.translate.tmdb.TmdbSearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TmdbService {

    private static final String TMDB_KEY = System.getenv("TMDB_API_KEY");
    private static final String BASE_URL =
            "https://api.themoviedb.org/3/search/tv?api_key=" + TMDB_KEY + "&language=pt-BR&query=";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public TmdbSearchResult buscarPorNome(String nome) {
        try {
            URI endereco = URI.create(BASE_URL + nome.replace(" ", "%20"));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(endereco)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(), TmdbSearchResult.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar TMDB", e);
        }
    }
}
