package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface TipoTurnoService {

	public ApiResponse<TipoTurno> guardarTipoTurno(TipoTurno tipoTurno);
	public ApiResponse<TipoTurno> editarTipoTurno(TipoTurno tipoTurno);
	public ApiResponse<List<TipoTurno>> listarTipoTurnoDeUsuario(Usuario userId);
	
	public ApiResponse<TipoTurno> obtenerTipoTurnoPorId(long idTipoTurno);
}
