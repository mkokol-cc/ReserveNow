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
	
	List<Reserva> findByAsignacionTipoTurnoAndFecha(AsignacionRecursoTipoTurno asignacionTipoTurno, LocalDate fecha);
	
	List<Reserva> findByAsignacionTipoTurnoAndFechaAndHora(AsignacionRecursoTipoTurno asignacionTipoTurno, LocalDate fecha, LocalTime hora);
	
	@Query("SELECT r.reservante, COUNT(r) FROM Reserva r GROUP BY r.reservante")
	Map<Reservante, Long> countReservasByReservante();
	
	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND r.hora = :hora AND r.asignacionTipoTurno.recurso.id = :recursoId")
    List<Reserva> buscarPorFechaHoraRecurso(@Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora, @Param("recursoId") Long recursoId);
	
	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND r.hora >= :horaInicio AND r.hora <= :horaFin AND r.asignacionTipoTurno.recurso.id = :recursoId")
	List<Reserva> buscarPorFechaRangoHorarioRecurso(
	        @Param("fecha") LocalDate fecha,
	        @Param("horaInicio") LocalTime horaInicio,
	        @Param("horaFin") LocalTime horaFin,
	        @Param("recursoId") Long recursoId
	);

}
