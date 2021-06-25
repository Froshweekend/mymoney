package br.com.ivanfsilva.mymoneyapi.repository;

import br.com.ivanfsilva.mymoneyapi.model.Cidade;
import br.com.ivanfsilva.mymoneyapi.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CidadeRepository extends JpaRepository<Estado, Long> {

    List<Cidade> findByEstadoCodigo(Long estado);
}
