package br.com.ivanfsilva.mymoneyapi.repository;

import br.com.ivanfsilva.mymoneyapi.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
