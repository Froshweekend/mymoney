package br.com.ivanfsilva.mymoneyapi.repository.listener;

import br.com.ivanfsilva.mymoneyapi.MymoneyApiApplication;

import br.com.ivanfsilva.mymoneyapi.model.Lancamento;
import br.com.ivanfsilva.mymoneyapi.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class LancamentoAnexoListener {

    @PostLoad
    public void postLoad(Lancamento lancamento) {
        if (StringUtils.hasText(lancamento.getAnexo())) {
            S3 s3 = MymoneyApiApplication.getBean(S3.class);
            lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
        }
    }

}
