package br.com.ivanfsilva.mymoneyapi.repository;

import br.com.ivanfsilva.mymoneyapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByEmail(String email);

    public List<Usuario> findByPermissoesDescricao(String permissaoDescricao);

}
