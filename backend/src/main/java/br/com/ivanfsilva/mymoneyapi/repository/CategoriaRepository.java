package br.com.ivanfsilva.mymoneyapi.repository;

import br.com.ivanfsilva.mymoneyapi.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
