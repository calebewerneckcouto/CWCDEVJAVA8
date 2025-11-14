package curso.api.rest.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Codigo;
import curso.api.rest.repositoy.CodigoRepository;

@RestController
@RequestMapping(value = "/codigos")
// ❌ REMOVIDO @Transactional da classe - era isso que causava o problema!
public class CodigoController {

    @Autowired
    private CodigoRepository codigoRepository;

    @DeleteMapping(value = "/{id}", produces = "application/text")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        if (!codigoRepository.existsById(id)) {
            return new ResponseEntity<>("Código não encontrado", HttpStatus.NOT_FOUND);
        }

        codigoRepository.deleteById(id);
        return new ResponseEntity<>("Código deletado com sucesso", HttpStatus.OK);
    }

    /**
     * Lista códigos com paginação
     * Endpoint: GET /codigos/?page=0&size=10
     */
    @GetMapping(value = "/", produces = "application/json")
    @Transactional(readOnly = true) // ✅ Apenas leitura
    public ResponseEntity<Page<Codigo>> listarCodigos(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Codigo> pageResult = codigoRepository.findAll(pageable);
            return new ResponseEntity<>(pageResult, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar códigos: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca código por ID
     * Endpoint: GET /codigos/{id}
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @Transactional(readOnly = true) // ✅ Apenas leitura
    public ResponseEntity<Codigo> buscarPorId(@PathVariable("id") Long id) {
        Optional<Codigo> codigo = codigoRepository.findById(id);

        return codigo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Cadastra novo código
     * Endpoint: POST /codigos/
     */
    @PostMapping(value = "/", produces = "application/json")
    // ❌ REMOVIDO @Transactional - deixa o Spring gerenciar automaticamente
    public ResponseEntity<Codigo> cadastrar(@RequestBody @Valid Codigo codigo) {
        try {
            System.out.println("📝 Cadastrando código: " + codigo.getLinguagem());
            Codigo codigoSalvo = codigoRepository.save(codigo);
            System.out.println("✅ Código salvo com ID: " + codigoSalvo.getId());
            return new ResponseEntity<>(codigoSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("❌ Erro ao cadastrar código: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Atualiza código existente
     * Endpoint: PUT /codigos/{id}
     */
    @PutMapping(value = "/{id}", produces = "application/json")
    // ❌ REMOVIDO @Transactional
    public ResponseEntity<Codigo> atualizar(
            @PathVariable("id") Long id, 
            @RequestBody @Valid Codigo codigo) {
        
        if (!codigoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            codigo.setId(id);
            Codigo codigoAtualizado = codigoRepository.save(codigo);
            return new ResponseEntity<>(codigoAtualizado, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar código: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca códigos por palavra-chave
     * Endpoint: GET /codigos/search?keyword=java&page=0&size=10
     */
    @GetMapping(value = "/search", produces = "application/json")
    @Transactional(readOnly = true) // ✅ Apenas leitura
    public ResponseEntity<Page<Codigo>> searchCodigos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Codigo> result = codigoRepository
                .findByLinguagemContainingIgnoreCaseOrDescricaoContainingIgnoreCase(
                    keyword, keyword, pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Erro na busca: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}