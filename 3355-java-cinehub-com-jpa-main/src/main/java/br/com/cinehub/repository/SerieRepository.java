package br.com.cinehub.repository;

import br.com.cinehub.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitulo(String titulo);
}
