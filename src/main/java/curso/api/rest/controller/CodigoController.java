package curso.api.rest.controller;

import java.util.Optional;

import javax.persistence.Cacheable;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    /*

    @GetMapping(value = "/", produces = "application/json")
    @CacheEvict(value = "cacheusuarios", allEntries = true)
    @CachePut(value = "cacheusuarios")
    public ResponseEntity<List<Codigo>> listarCodigos() {
        List<Codigo> list = (List<Codigo>) codigoRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    */
    
    
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Page<Codigo>> listarCodigos(@RequestParam(defaultValue = "0") int page, 
                                                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Codigo> pageResult = codigoRepository.findAll(pageable);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }




    @GetMapping(value = "/{id}", produces = "application/json")
    @CacheEvict(value = "cacheusuarios", allEntries = true)
    @CachePut(value = "cacheusuarios")
    public ResponseEntity<Codigo> buscarPorId(@PathVariable("id") Long id) {
        Optional<Codigo> codigo = codigoRepository.findById(id);

        return codigo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Codigo> cadastrar(@RequestBody @Valid Codigo codigo) {
        Codigo codigoSalvo = codigoRepository.save(codigo);
        return new ResponseEntity<>(codigoSalvo, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Codigo> atualizar(@PathVariable("id") Long id, @RequestBody @Valid Codigo codigo) {
        if (!codigoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        codigo.setId(id);
        Codigo codigoAtualizado = codigoRepository.save(codigo);
        return new ResponseEntity<>(codigoAtualizado, HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<Page<Codigo>> searchCodigos(@RequestParam String keyword,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Alteração aqui: a busca vai comparar a keyword com linguagem ou descrição
        Page<Codigo> result = codigoRepository.findByLinguagemContainingIgnoreCaseOrDescricaoContainingIgnoreCase(keyword, keyword, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
