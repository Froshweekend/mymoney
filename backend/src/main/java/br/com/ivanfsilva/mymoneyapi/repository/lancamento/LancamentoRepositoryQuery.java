package br.com.ivanfsilva.mymoneyapi.repository.lancamento;

import br.com.ivanfsilva.mymoneyapi.dto.LancamentoEstatisticaCategoria;
import br.com.ivanfsilva.mymoneyapi.model.Lancamento;
import br.com.ivanfsilva.mymoneyapi.repository.filter.LancamentoFilter;
import br.com.ivanfsilva.mymoneyapi.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);

    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
