package br.com.ivanfsilva.mymoneyapi.service;

import br.com.ivanfsilva.mymoneyapi.storage.S3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ivanfsilva.mymoneyapi.dto.LancamentoEstatisticaPessoa;
import br.com.ivanfsilva.mymoneyapi.mail.Mailer;
import br.com.ivanfsilva.mymoneyapi.model.Lancamento;
import br.com.ivanfsilva.mymoneyapi.model.Pessoa;
import br.com.ivanfsilva.mymoneyapi.model.Usuario;
import br.com.ivanfsilva.mymoneyapi.repository.LancamentoRepository;
import br.com.ivanfsilva.mymoneyapi.repository.PessoaRepository;
import br.com.ivanfsilva.mymoneyapi.repository.UsuarioRepository;
import br.com.ivanfsilva.mymoneyapi.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LancamentoService {

    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Mailer mailer;

    @Autowired
    private S3 s3;

    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos() {
        if (logger.isDebugEnabled()) {
            logger.debug("Preparando envio de "
                    + "e-mails de aviso de lan??amentos vencidos.");
        }

        List<Lancamento> vencidos = lancamentoRepository
                .findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

        if (vencidos.isEmpty()) {
            logger.info("Sem lan??amentos vencidos para aviso.");

            return;
        }

        logger.info("Exitem {} lan??amentos vencidos.", vencidos.size());

        List<Usuario> destinatarios = usuarioRepository
                .findByPermissoesDescricao(DESTINATARIOS);

        if (destinatarios.isEmpty()) {
            logger.warn("Existem lan??amentos vencidos, mas o "
                    + "sistema n??o encontrou destinat??rios.");

            return;
        }

        mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

        logger.info("Envio de e-mail de aviso conclu??do.");
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
        validarPessoa(lancamento);

        if (StringUtils.hasText(lancamento.getAnexo())) {
            s3.salvar(lancamento.getAnexo());
        }

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        if (StringUtils.isEmpty(lancamento.getAnexo())
                && StringUtils.hasText(lancamentoSalvo.getAnexo())) {
            s3.remover(lancamentoSalvo.getAnexo());
        } else if (StringUtils.hasText(lancamento.getAnexo())
                && !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
            s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
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
