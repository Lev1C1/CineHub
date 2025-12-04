package br.com.cinehub.service.translate.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbSerieResult {

    private String name;
    private String original_name;

    public String getName() {
        return name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }
}
