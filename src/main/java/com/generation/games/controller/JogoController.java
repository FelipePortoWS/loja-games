package com.generation.games.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.games.model.Jogo;
import com.generation.games.repository.JogoRepository;
import com.generation.games.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jogos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JogoController {

	@Autowired
	private JogoRepository jogoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Jogo>> getAll() {
		return ResponseEntity.ok(jogoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Jogo> getById(@PathVariable long id) {
		return jogoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
					.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Jogo>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(jogoRepository
				.findAllByTituloContainingIgnoreCase(titulo));
	}

	@PostMapping
	public ResponseEntity<Jogo> post(@Valid @RequestBody Jogo jogo) {
		if (categoriaRepository.existsById(jogo.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(jogoRepository.save(jogo));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!, null");
	}

	@PutMapping
	public ResponseEntity<Jogo> put(@Valid @RequestBody Jogo jogo) {
		if (jogoRepository.existsById(jogo.getId())) {

			if (categoriaRepository.existsById(jogo.getCategoria().getId())) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(jogoRepository.save(jogo));
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Jogo> jogo = jogoRepository.findById(id);

		if (jogo.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		jogoRepository.deleteById(id);
	}

}
