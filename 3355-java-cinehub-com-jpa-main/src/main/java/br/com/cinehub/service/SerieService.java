package br.com.cinehub.service;

import br.com.cinehub.dto.EpisodioDTO;
import br.com.cinehub.dto.SerieDTO;
import br.com.cinehub.model.*;
import br.com.cinehub.repository.SerieRepository;
import br.com.cinehub.service.translate.tmdb.TmdbService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    private final SerieRepository repositorio;
    private final ConsumoApi consumo;
    private final ConverteDados conversor;
    private final TmdbService tmdbService;

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY  = "&apikey=" + System.getenv("OMDB_API_KEY");

    public SerieService(SerieRepository repositorio,
                        ConsumoApi consumo,
                        ConverteDados conversor,
                        TmdbService tmdbService) {
        this.repositorio = repositorio;
        this.consumo     = consumo;
        this.conversor   = conversor;
        this.tmdbService = tmdbService;
    }

    // ---------- queries simples ----------

    public List<SerieDTO> listarTodas() {
        return repositorio.findAll().stream()
                .map(SerieDTO::from)
                .toList();
    }

    public Optional<SerieDTO> buscarPorTitulo(String titulo) {
        return repositorio.findByTituloContainingIgnoreCase(titulo)
                .map(SerieDTO::from);
    }

    public List<SerieDTO> buscarPorAtor(String ator, Double avaliacao) {
        return repositorio
                .findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(ator, avaliacao)
                .stream().map(SerieDTO::from).toList();
    }

    public List<SerieDTO> top5() {
        return repositorio.findTop5ByOrderByAvaliacaoDesc()
                .stream().map(SerieDTO::from).toList();
    }

    public List<SerieDTO> buscarPorCategoria(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return repositorio.findByGenero(categoria)
                .stream().map(SerieDTO::from).toList();
    }

    public List<SerieDTO> filtrarPorTemporadaEAvaliacao(int maxTemporadas, double minAvaliacao) {
        return repositorio.seriesPorTemporadaEAvaliacao(maxTemporadas, minAvaliacao)
                .stream().map(SerieDTO::from).toList();
    }

    public List<EpisodioDTO> episodiosPorTrecho(String trecho) {
        return repositorio.episodiosPorTrecho(trecho)
                .stream().map(EpisodioDTO::from).toList();
    }

    public List<EpisodioDTO> topEpisodiosPorSerie(String nomeSerie) {
        return repositorio.findByTituloContainingIgnoreCase(nomeSerie)
                .map(serie -> repositorio
                        .topEpisodiosPorSerie(serie, PageRequest.of(0, 5))
                        .stream().map(EpisodioDTO::from).toList())
                .orElse(List.of());
    }

    // ---------- operações que chamam APIs externas ----------

    public SerieDTO buscarOuSalvarSerie(String nome) {
        DadosSerie dados = getDadosSerie(nome);

        Serie serie = repositorio.findByTituloContainingIgnoreCase(dados.titulo())
                .orElseGet(() -> repositorio.save(new Serie(dados)));

        return SerieDTO.from(serie);
    }

    public List<EpisodioDTO> buscarECarregarEpisodios(String nomeSerie) {
        var resultadoTMDB = tmdbService.buscarPorNome(nomeSerie);
        String nomeOriginal = nomeSerie;

        if (resultadoTMDB != null &&
                resultadoTMDB.getResults() != null &&
                !resultadoTMDB.getResults().isEmpty()) {
            var primeiro = resultadoTMDB.getResults().get(0);
            if (primeiro.getOriginal_name() != null && !primeiro.getOriginal_name().isBlank()) {
                nomeOriginal = primeiro.getOriginal_name();
            }
        }

        final String nomeParaBusca = nomeOriginal;

        Optional<Serie> serieOpt = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieOpt.isEmpty()) {
            serieOpt = repositorio.findByTituloContainingIgnoreCase(nomeParaBusca);
        }

        if (serieOpt.isEmpty()) return List.of();

        Serie serieEncontrada = serieOpt.get();
        List<DadosTemporada> temporadas = new java.util.ArrayList<>();

        for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
            var json = consumo.obterDados(
                    ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") +
                            "&season=" + i + API_KEY);
            temporadas.add(conversor.obterDados(json, DadosTemporada.class));
        }

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e -> new Episodio(t.numero(), e, serieEncontrada)))
                .collect(Collectors.toList());

        serieEncontrada.setEpisodios(episodios);
        repositorio.save(serieEncontrada);

        return episodios.stream().map(EpisodioDTO::from).toList();
    }

    // ---------- helper privado ----------

    private DadosSerie getDadosSerie(String nome) {
        var resultadoTMDB = tmdbService.buscarPorNome(nome);
        String nomeOriginal = nome;
        String nomePtBr = nome;

        if (resultadoTMDB != null &&
                resultadoTMDB.getResults() != null &&
                !resultadoTMDB.getResults().isEmpty()) {
            var serie = resultadoTMDB.getResults().get(0);
            if (serie.getOriginal_name() != null && !serie.getOriginal_name().isBlank())
                nomeOriginal = serie.getOriginal_name();
            if (serie.getName() != null && !serie.getName().isBlank())
                nomePtBr = serie.getName();
        }

        String json = consumo.obterDados(
                ENDERECO + nomeOriginal.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        return new DadosSerie(
                dados.titulo(), dados.totalTemporadas(), dados.avaliacao(),
                dados.genero(), dados.atores(), dados.poster(),
                dados.sinopse(), nomePtBr);
    }
}