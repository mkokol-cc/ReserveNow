package com.sistema.examenes.anterior.repositorios;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long>{
	//List<Reserva> findByUsuarioId(Long idUsuario);
	List<Reserva> findByAsignacionTipoTurno(AsignacionRecursoTipoTurno asignacionTipoTurno);
	
	@Query("SELECT r FROM Reserva r WHERE r.asignacionTipoTurno.recurso.id = :recursoId")
	List<Reserva> findByRecurso(@Param("recursoId") Long recursoId);
	
	@Query("SELECT r FROM Reserva r WHERE r.asignacionTipoTurno.tipoTurno.id = :tipoTurnoId")
	List<Reserva> findByTipoTurno(@Param("tipoTurnoId") Long recursoId);
	
	List<Reserva> findByAsignacionTipoTurnoAndFecha(AsignacionRecursoTipoTurno asignacionTipoTurno, LocalDate fecha);
	
	List<Reserva> findByAsignacionTipoTurnoAndFechaAndHora(AsignacionRecursoTipoTurno asignacionTipoTurno, LocalDate fecha, LocalTime hora);
	
	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND r.hora = :hora AND r.asignacionTipoTurno.recurso.id = :recursoId")
    List<Reserva> buscarPorFechaHoraRecurso(@Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora, @Param("recursoId") Long recursoId);
	
	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND r.asignacionTipoTurno.recurso.id = :recursoId")
    List<Reserva> buscarPorFechaYRecurso(@Param("fecha") LocalDate fecha, @Param("recursoId") Long recursoId);
	
	@Query("SELECT r FROM Reserva r WHERE r.asignacionTipoTurno.recurso.id = :recursoId")
    List<Reserva> buscarRecurso(@Param("recursoId") Long recursoId);
	
	//no es mayoy igual o menor igual porque puede haber un turno desde las 12:00 - 13:00 y otro de 13:00 a 14:00 y si fuera igual
	//entonces 13:00==13:00 y seria ocupado
	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND NOT (:horaDesde >= r.horaFin OR :horaHasta <= r.hora) AND r.asignacionTipoTurno.recurso.id = :recursoId")
	List<Reserva> buscarPorFechaRangoHorarioRecurso(
	        @Param("fecha") LocalDate fecha,
	        @Param("horaDesde") LocalTime horaDesde,
	        @Param("horaHasta") LocalTime horaHasta,
	        @Param("recursoId") Long recursoId
	);

	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND NOT (:horaDesde >= r.horaFin OR :horaHasta <= r.hora) AND r.asignacionTipoTurno.id = :asignacionId")
	List<Reserva> buscarPorFechaRangoHorarioAsignacion(
	        @Param("fecha") LocalDate fecha,
	        @Param("horaDesde") LocalTime horaDesde,
	        @Param("horaHasta") LocalTime horaHasta,
	        @Param("asignacionId") Long asignacionId
	);
	
	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND NOT (:horaDesde >= r.horaFin OR :horaHasta <= r.hora)")
	List<Reserva> buscarPorFechaYRangoHorario(
	        @Param("fecha") LocalDate fecha,
	        @Param("horaDesde") LocalTime horaDesde,
	        @Param("horaHasta") LocalTime horaHasta
	);

}
