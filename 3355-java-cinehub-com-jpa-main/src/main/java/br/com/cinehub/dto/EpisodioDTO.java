package br.com.cinehub.dto;

import br.com.cinehub.model.Episodio;
import java.time.LocalDate;

public record EpisodioDTO(
        Long id,
        Integer temporada,
        Integer numeroEpisodio,
        String titulo,
        Double avaliacao,
        LocalDate dataLancamento
) {
    public static EpisodioDTO from(Episodio e) {
        return new EpisodioDTO(
                e.getId(),
                e.getTemporada(),
                e.getNumeroEpisodio(),
                e.getTitulo(),
                e.getAvaliacao(),
                e.getDataLancamento()
        );
    }
}