package br.com.cinehub.principal;

import br.com.cinehub.model.DadosSerie;
import br.com.cinehub.model.DadosTemporada;
import br.com.cinehub.model.Serie;
import br.com.cinehub.repository.SerieRepository;
import br.com.cinehub.service.ConsumoApi;
import br.com.cinehub.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<Serie> seriesBuscadas ;
    private SerieRepository repositorio;

   public Principal(SerieRepository repositorio){
       this.repositorio = repositorio;
   }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            try {
                System.out.println("""
                        1 - Buscar séries
                        2 - Buscar episódios
                        3 - Listar séries buscadas
                        
                        0 - Sair
                        """);

                System.out.print("Escolha uma opção: ");

                String entrada = leitura.nextLine().trim(); // lê tudo como texto
                if (!entrada.matches("\\d+")) { // verifica se é número
                    System.out.println("❌ Digite apenas números válidos!");
                    continue;
                }

                opcao = Integer.parseInt(entrada);

                switch (opcao) {
                    case 1 -> buscarSerieWeb();
                    case 2 -> buscarEpisodioPorSerie();
                    case 3 -> listarSeriesBuscadas();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }

            } catch (Exception e) {
                System.out.println("⚠️ Ocorreu um erro inesperado: " + e.getMessage());
            }
        }
    }

    private void buscarSerieWeb() {
        // Obtem os dados da série via OMDb
        DadosSerie dados = getDadosSerie();

        // Converte DadosSerie em Serie, traduzindo a sinopse automaticamente
        Serie serie = new Serie(dados);

        // Adiciona à lista de séries buscadas
        //seriesBuscadas.add(serie);
        repositorio.save(serie);

        // Exibe a série já traduzida
        System.out.println("-------------------------------------------------");
        System.out.println("Título: " + serie.getTitulo());
        System.out.println("Gênero: " + serie.getGenero());
        System.out.println("Temporadas: " + serie.getTotalTemporadas());
        System.out.println("Avaliação: " + serie.getAvaliacao());
        System.out.println("Atores: " + serie.getAtores());
        System.out.println("Sinopse: " + serie.getSinopse());
        System.out.println("Poster: " + serie.getPoster());
        System.out.println("-------------------------------------------------");
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
       seriesBuscadas = repositorio.findAll();

        if (seriesBuscadas.isEmpty()) {
            System.out.println("⚠️ Nenhuma série foi buscada ainda!");
            return;
        }

        seriesBuscadas.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(serie -> {
                    System.out.println("-------------------------------------------------");
                    System.out.println("Título: " + serie.getTitulo());
                    System.out.println("Gênero: " + serie.getGenero());
                    System.out.println("Temporadas: " + serie.getTotalTemporadas());
                    System.out.println("Avaliação: " + serie.getAvaliacao());
                    System.out.println("Atores: " + serie.getAtores());
                    System.out.println("Sinopse: " + serie.getSinopse());
                    System.out.println("Poster: " + serie.getPoster());
                    System.out.println("-------------------------------------------------");
                });
    }
}
