package com.sistema.examenes.anterior.repositorios;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.anterior.modelo.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario,Long>{

}

