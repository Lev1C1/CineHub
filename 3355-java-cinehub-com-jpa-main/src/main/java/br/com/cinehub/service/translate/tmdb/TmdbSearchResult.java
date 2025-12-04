package br.com.cinehub.service.translate.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbSearchResult {
    private List<TmdbSerieResult> results;

    public List<TmdbSerieResult> getResults() {
        return results;
    }

    public void setResults(List<TmdbSerieResult> results) {
        this.results = results;
    }
}
