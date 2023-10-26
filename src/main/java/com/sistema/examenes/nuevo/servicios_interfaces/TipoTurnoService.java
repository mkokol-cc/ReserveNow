package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface TipoTurnoService {

	TipoTurno guardarTipoTurno(TipoTurno tipoTurno) throws Exception;
	TipoTurno editarTipoTurno(TipoTurno tipoTurno) throws Exception;
	List<TipoTurno> listarTipoTurnoDeUsuario(Usuario userId) throws Exception;
	
	TipoTurno obtenerTipoTurnoPorId(long idTipoTurno) throws Exception;
	void eliminarTipoTurno(Long idTipoTurno, Usuario u) throws Exception;
}
