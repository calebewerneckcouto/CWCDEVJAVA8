package curso.api.rest.repositoy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import curso.api.rest.model.Codigo;

public interface CodigoRepository extends JpaRepository<Codigo, Long> {
	
	
	

	// Método para buscar por linguagem ou descrição de forma ignorante a
		// maiúsculas/minúsculas
		List<Codigo> findByLinguagemContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String linguagem, String descricao);
}
