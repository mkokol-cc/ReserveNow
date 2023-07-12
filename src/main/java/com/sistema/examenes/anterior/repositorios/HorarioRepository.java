package com.sistema.examenes.anterior.repositorios;

import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario,Long>{

	List<Horario> findByAsignacion(AsignacionRecursoTipoTurno asignacion);
	
	List<Horario> findByAsignacionAndDia(AsignacionRecursoTipoTurno asignacion, Dias dia);
	
    @Query("SELECT h FROM Horario h WHERE h.dia = :dia AND :hora BETWEEN h.desde AND h.hasta AND h.asignacion = :asignacion")
    List<Horario> obtenerPorHoraYAsignacion(@Param("dia") Dias dia, @Param("hora") Time hora, @Param("asignacion") AsignacionRecursoTipoTurno asignacion);
}
