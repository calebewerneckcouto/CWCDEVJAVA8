package curso.api.rest.repositoy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import curso.api.rest.model.Codigo;

public interface CodigoRepository extends JpaRepository<Codigo, Long> {

    // Método para buscar por linguagem ou descrição com paginação
    Page<Codigo> findByLinguagemContainingIgnoreCaseOrDescricaoContainingIgnoreCase(
            String linguagem, 
            String descricao, 
            Pageable pageable);
}
