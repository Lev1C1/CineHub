package br.com.cinehub.controller;

import br.com.cinehub.dto.EpisodioDTO;
import br.com.cinehub.dto.SerieDTO;
import br.com.cinehub.service.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    // GET /series
    @GetMapping
    public List<SerieDTO> listarTodas() {
        return serieService.listarTodas();
    }

    // GET /series/top5
    @GetMapping("/top5")
    public List<SerieDTO> top5() {
        return serieService.top5();
    }

    // GET /series/busca?titulo=Breaking Bad
    @GetMapping("/busca")
    public ResponseEntity<SerieDTO> buscarPorTitulo(@RequestParam String titulo) {
        return serieService.buscarPorTitulo(titulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /series/ator?nome=Bryan&avaliacao=8.0
    @GetMapping("/ator")
    public List<SerieDTO> buscarPorAtor(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0.0") Double avaliacao) {
        return serieService.buscarPorAtor(nome, avaliacao);
    }

    // GET /series/categoria?genero=drama
    @GetMapping("/categoria")
    public List<SerieDTO> buscarPorCategoria(@RequestParam String genero) {
        return serieService.buscarPorCategoria(genero);
    }

    // GET /series/filtro?temporadas=4&avaliacao=8.5
    @GetMapping("/filtro")
    public List<SerieDTO> filtrar(
            @RequestParam int temporadas,
            @RequestParam double avaliacao) {
        return serieService.filtrarPorTemporadaEAvaliacao(temporadas, avaliacao);
    }

    // POST /series?nome=Breaking Bad  → busca na OMDB e salva
    @PostMapping
    public ResponseEntity<SerieDTO> buscarESalvar(@RequestParam String nome) {
        return ResponseEntity.ok(serieService.buscarOuSalvarSerie(nome));
    }

    // POST /series/episodios?nome=Breaking Bad  → carrega todos os episódios
    @PostMapping("/episodios")
    public ResponseEntity<List<EpisodioDTO>> carregarEpisodios(@RequestParam String nome) {
        List<EpisodioDTO> result = serieService.buscarECarregarEpisodios(nome);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // GET /series/episodios/busca?trecho=pilot
    @GetMapping("/episodios/busca")
    public List<EpisodioDTO> buscarEpisodioPorTrecho(@RequestParam String trecho) {
        return serieService.episodiosPorTrecho(trecho);
    }

    // GET /series/episodios/top?nome=Breaking Bad
    @GetMapping("/episodios/top")
    public List<EpisodioDTO> topEpisodios(@RequestParam String nome) {
        return serieService.topEpisodiosPorSerie(nome);
    }
}