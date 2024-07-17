package com.generation.games.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.generation.games.model.Jogo;

public interface JogoRepository extends JpaRepository<Jogo,Long>{
	
	public List <Jogo> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);

}
