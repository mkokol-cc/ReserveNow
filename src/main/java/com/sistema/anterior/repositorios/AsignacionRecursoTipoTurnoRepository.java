package com.sistema.anterior.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.anterior.modelo.Recurso;
import com.sistema.anterior.modelo.TipoTurno;

public interface AsignacionRecursoTipoTurnoRepository extends JpaRepository<AsignacionRecursoTipoTurno, Long> {
    List<AsignacionRecursoTipoTurno> findByRecursoAndTipoTurno(Recurso recurso, TipoTurno tipoTurno);
    
    @Query("SELECT artt FROM AsignacionRecursoTipoTurno artt JOIN artt.recurso r JOIN r.usuario u WHERE u.id = :userId")
    List<AsignacionRecursoTipoTurno> findByRecursoUsuarioId(Long userId);
    
    @Query("SELECT COUNT(a) FROM AsignacionRecursoTipoTurno a WHERE a.recurso = ?1 AND a.tipoTurno = ?2")
    int existeAsignacion(Recurso recurso, TipoTurno tipoTurno);
    
    @Query("SELECT a FROM AsignacionRecursoTipoTurno a WHERE a.recurso.id = :idRecurso AND a.tipoTurno.id = :idTipoTurno")
    AsignacionRecursoTipoTurno findAsignacionByRecursoAndTipoTurno(@Param("idRecurso") Long idRecurso, @Param("idTipoTurno") Long idTipoTurno);
}

