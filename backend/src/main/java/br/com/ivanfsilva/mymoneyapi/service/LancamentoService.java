package br.com.ivanfsilva.mymoneyapi.service;

import br.com.ivanfsilva.mymoneyapi.dto.LancamentoEstatisticaPessoa;
import br.com.ivanfsilva.mymoneyapi.model.Lancamento;
import br.com.ivanfsilva.mymoneyapi.model.Pessoa;
import br.com.ivanfsilva.mymoneyapi.repository.LancamentoRepository;
import br.com.ivanfsilva.mymoneyapi.repository.PessoaRepository;
import br.com.ivanfsilva.mymoneyapi.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos() {
        System.out.println(">>>>>>>>>>>>>>> Método sendo executado...");
    }

    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
        List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream(
                "/report/lancamentos-por-pessoa.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(dados));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public Lancamento salvar(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());

        if( pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;
        if (lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
        }

        if (pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoExistente(Long codigo) {
        Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
        if (lancamentoSalvo == null) {
            throw new IllegalArgumentException();
        }
        return lancamentoSalvo;
    }
}
