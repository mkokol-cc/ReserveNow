package com.sistema.examenes.anterior.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.TipoTurno;

public interface AsignacionRecursoTipoTurnoRepository extends JpaRepository<AsignacionRecursoTipoTurno, Long> {
    AsignacionRecursoTipoTurno findByRecursoAndTipoTurno(Recurso recurso, TipoTurno tipoTurno);
    
    @Query("SELECT artt FROM AsignacionRecursoTipoTurno artt JOIN artt.recurso r JOIN r.usuario u WHERE u.id = :userId")
    List<AsignacionRecursoTipoTurno> findByRecursoUsuarioId(Long userId);
}

