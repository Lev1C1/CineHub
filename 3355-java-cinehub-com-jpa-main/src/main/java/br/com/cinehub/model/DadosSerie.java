package br.com.cinehub.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String avaliacao,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Actors") String atores,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Plot") String sinopse,
        String tituloPtBr
) {

    @Override
    public String toString() {
        try {
            ObjectWriter writer = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter(); // ativa formatação bonita
            return writer.writeValueAsString(this);
        } catch (Exception e) {
            return "Erro ao converter para JSON: " + e.getMessage();
        }
    }
}