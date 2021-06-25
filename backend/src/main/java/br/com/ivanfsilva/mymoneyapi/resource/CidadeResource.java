package br.com.ivanfsilva.mymoneyapi.resource;

import java.util.List;

import br.com.ivanfsilva.mymoneyapi.model.Cidade;
import br.com.ivanfsilva.mymoneyapi.repository.CidadeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cidades")
public class CidadeResource {

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Cidade> pesquisar(@RequestParam Long estado) {
        return cidadeRepository.findByEstadoCodigo(estado);
    }

}
