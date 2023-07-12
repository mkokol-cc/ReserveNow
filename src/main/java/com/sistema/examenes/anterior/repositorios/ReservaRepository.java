package com.sistema.examenes.anterior.repositorios;

import java.sql.Time;
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
	
	List<Reserva> findByAsignacionTipoTurnoAndFecha(AsignacionRecursoTipoTurno asignacionTipoTurno, Date fecha);
	
	List<Reserva> findByAsignacionTipoTurnoAndFechaAndHora(AsignacionRecursoTipoTurno asignacionTipoTurno, Date fecha, Time hora);
	
	@Query("SELECT r.reservante, COUNT(r) FROM Reserva r GROUP BY r.reservante")
	Map<Reservante, Long> countReservasByReservante();
	
	@Query("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND r.hora = :hora AND r.asignacionTipoTurno.recurso.id = :recursoId")
    List<Reserva> buscarPorFechaHoraRecurso(@Param("fecha") Date fecha, @Param("hora") Time hora, @Param("recursoId") Long recursoId);
	
}
