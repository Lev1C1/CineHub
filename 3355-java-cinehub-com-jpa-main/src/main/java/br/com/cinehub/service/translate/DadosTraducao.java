package br.com.cinehub.service.translate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTraducao(
        @JsonAlias("responseData") DadosResposta dadosResposta
) {}
