package br.com.cinehub.dto;

import br.com.cinehub.model.Categoria;
import br.com.cinehub.model.Serie;

public record SerieDTO(
        Long id,
        String titulo,
        String tituloPtBr,
        Integer totalTemporadas,
        Double avaliacao,
        Categoria genero,
        String atores,
        String poster,
        String sinopse
) {
    public static SerieDTO from(Serie s) {
        return new SerieDTO(
                s.getId(),
                s.getTitulo(),
                s.getTituloPtBr(),
                s.getTotalTemporadas(),
                s.getAvaliacao(),
                s.getGenero(),
                s.getAtores(),
                s.getPoster(),
                s.getSinopse()
        );
    }
}