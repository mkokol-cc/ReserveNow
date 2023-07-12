package com.sistema.examenes.anterior.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.TipoTurno;

public interface AsignacionRecursoTipoTurnoRepository extends JpaRepository<AsignacionRecursoTipoTurno, Long> {
    AsignacionRecursoTipoTurno findByRecursoAndTipoTurno(Recurso recurso, TipoTurno tipoTurno);
}

