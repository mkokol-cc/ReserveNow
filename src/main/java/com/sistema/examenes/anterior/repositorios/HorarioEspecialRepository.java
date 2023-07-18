package com.sistema.examenes.anterior.repositorios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;

@Repository
public interface HorarioEspecialRepository extends JpaRepository<HorarioEspecial,Long>{

	void deleteByAsignacion(AsignacionRecursoTipoTurno asignacion);

	void deleteByRecurso(Recurso recurso);
	
	List<HorarioEspecial> findByAsignacion(AsignacionRecursoTipoTurno asignacion);
	
	List<HorarioEspecial> findByFechaAndAsignacion(LocalDate fecha, AsignacionRecursoTipoTurno asignacion);
	
	List<HorarioEspecial> findByFechaAndRecurso(LocalDate fecha, Recurso recurso);
	
    @Query("SELECT he FROM HorarioEspecial he WHERE he.fecha = :fecha AND :hora BETWEEN he.desde AND he.hasta AND he.asignacion = :asignacion")
    List<HorarioEspecial> obtenerPorFechaHoraYAsignacion(@Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora, @Param("asignacion") AsignacionRecursoTipoTurno asignacion);
    
}
