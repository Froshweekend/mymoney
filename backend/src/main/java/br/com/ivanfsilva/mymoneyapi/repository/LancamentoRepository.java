package br.com.ivanfsilva.mymoneyapi.repository;

import java.time.LocalDate;
import java.util.List;

import br.com.ivanfsilva.mymoneyapi.model.Lancamento;
import br.com.ivanfsilva.mymoneyapi.repository.lancamento.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

    List<Lancamento> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);
}
