package com.sistema.examenes.nuevo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.TipoTurnoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;

@Service
public class TipoTurnoServiceImpl implements TipoTurnoService{
	
	@Autowired
	private TipoTurnoRepository tipoTurnoRepo;

	@Override
	public ApiResponse<TipoTurno> guardarTipoTurno(TipoTurno tipoTurno) {
		try {
			if(tipoTurno.sonValidosLosDatos() && tipoTurno.tieneLosDatosMinimos()) {
				TipoTurno t = tipoTurnoRepo.save(tipoTurno);
				if(t!=null) {
					return new ApiResponse<>(true,"",t);
				}
				return new ApiResponse<>(false,"Error al guardar el Tipo de Turno",null);
			}
			return new ApiResponse<>(false,"Datos inválidos",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<TipoTurno> editarTipoTurno(TipoTurno tipoTurno, long idUsuario) {
		try {
			ApiResponse<TipoTurno> response = obtenerTipoTurnoPorId(tipoTurno.getId());
			if(response.isSuccess()) {
				return (response.getData().getUsuario().getId()==idUsuario ? guardarTipoTurno(tipoTurno) 
						: new ApiResponse<>(false,"Usuario no autorizado",null));
			}
			return response;
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<List<TipoTurno>> listarTipoTurnoDeUsuario(Usuario usuario) {
		try {
			List<TipoTurno> t = tipoTurnoRepo.findByUsuario(usuario);
			if(t.size()>0) {
				return new ApiResponse<>(true,"",t);
			}
			return new ApiResponse<>(false,"Error al obtener los Tipos de Turno del usuario",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<TipoTurno> obtenerTipoTurnoPorId(long idTipoTurno) {
		try {
			TipoTurno t = tipoTurnoRepo.getById(idTipoTurno);
			if(t!=null) {
				return new ApiResponse<>(true,"",t);
			}
			return new ApiResponse<>(false,"Error al obtener el Tipo de Turno",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	

}
