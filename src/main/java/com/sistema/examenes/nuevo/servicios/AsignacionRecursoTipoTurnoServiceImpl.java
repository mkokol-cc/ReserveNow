package com.sistema.examenes.nuevo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;

@Service
public class AsignacionRecursoTipoTurnoServiceImpl implements AsignacionRecursoTipoTurnoService{

	@Autowired
	private AsignacionRecursoTipoTurnoRepository asignacionRepo;
	
	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> guardarAsignacion(AsignacionRecursoTipoTurno asignacion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> editarAsignacion(AsignacionRecursoTipoTurno asignacion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacion(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacionPorUsuario(Long idUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> obtenerPorId(Long id) {
		AsignacionRecursoTipoTurno asig = asignacionRepo.getById(id);
		System.out.println("LLEGUE ACA");
		if(asig!=null) {
			System.out.println("LLEGUE ACA");
			return new ApiResponse<>(true,"",asig);
		}
		return new ApiResponse<>(false,"No se obtuvo el Reservante",asig);
	}

}
