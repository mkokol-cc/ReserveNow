package com.sistema.examenes.anterior.repositorios;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;

@Repository
public interface HorarioEspecialRepository extends JpaRepository<HorarioEspecial,Long>{

	List<HorarioEspecial> findByAsignacion(AsignacionRecursoTipoTurno asignacion);
	
	List<HorarioEspecial> findByAsignacionAndFecha(AsignacionRecursoTipoTurno asignacion, Date fecha);
	
    @Query("SELECT he FROM HorarioEspecial he WHERE he.fecha = :fecha AND :hora BETWEEN he.desde AND he.hasta AND he.asignacion = :asignacion")
    List<HorarioEspecial> obtenerPorFechaHoraYAsignacion(@Param("fecha") Date fecha, @Param("hora") Time hora, @Param("asignacion") AsignacionRecursoTipoTurno asignacion);
    
}
