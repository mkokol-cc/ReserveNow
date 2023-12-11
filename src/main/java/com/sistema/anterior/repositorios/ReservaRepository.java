package com.sistema.anterior.repositorios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.anterior.modelo.Reserva;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long>{
	List<Reserva> findByAsignacionTipoTurno(AsignacionRecursoTipoTurno asignacionTipoTurno);
	
	@Query("SELECT r FROM Reserva r WHERE r.asignacionTipoTurno.recurso.id = :recursoId")
	List<Reserva> findByRecurso(@Param("recursoId") Long recursoId);
	
	@Query("SELECT r FROM Reserva r WHERE r.asignacionTipoTurno.tipoTurno.id = :tipoTurnoId")
	List<Reserva> findByTipoTurno(@Param("tipoTurnoId") Long tipoTurnoId);
}
