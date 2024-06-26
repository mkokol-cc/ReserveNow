package com.sistema.anterior.repositorios;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.anterior.modelo.Dias;
import com.sistema.anterior.modelo.Horario;
import com.sistema.anterior.modelo.Recurso;

@Repository
public interface HorarioRepository extends JpaRepository<Horario,Long>{
	
	void deleteByAsignacion(AsignacionRecursoTipoTurno asignacion);

	void deleteByRecurso(Recurso recurso);
	
	List<Horario> findByAsignacion(AsignacionRecursoTipoTurno asignacion);
	
	List<Horario> findByAsignacionAndDia(AsignacionRecursoTipoTurno asignacion, Dias dia);
	
	List<Horario> findByRecursoAndDia(Recurso recurso, Dias dia);
	
    @Query("SELECT h FROM Horario h WHERE h.dia = :dia AND :hora BETWEEN h.desde AND h.hasta AND h.asignacion = :asignacion")
    List<Horario> obtenerPorHoraYAsignacion(@Param("dia") Dias dia, @Param("hora") LocalTime hora, @Param("asignacion") AsignacionRecursoTipoTurno asignacion);
    
    @Query("SELECT h FROM Horario h WHERE h.dia = :dia AND :hora BETWEEN h.desde AND h.hasta AND h.recurso = :recurso")
    List<Horario> obtenerPorHoraYRecurso(@Param("dia") Dias dia, @Param("hora") LocalTime hora, @Param("recurso") Recurso recurso);
}
