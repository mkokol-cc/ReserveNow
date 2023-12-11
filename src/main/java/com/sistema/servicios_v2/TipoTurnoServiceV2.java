package com.sistema.servicios_v2;

import java.util.List;

import com.sistema.anterior.modelo.TipoTurno;
import com.sistema.modelo.usuario.Usuario;

public interface TipoTurnoServiceV2 {
	TipoTurno obtenerTipoTurnoPorId(Long id) throws Exception;
	List<TipoTurno> listarTipoTurno(Usuario u);
	void eliminarTipoTurno(Long id, Usuario u) throws Exception;
	TipoTurno nuevoTipoTurno(TipoTurno t) throws Exception;
	TipoTurno editarTipoTurno(TipoTurno t, Usuario u) throws Exception;
	void borrarTipoTurno(Long idTipoTurno) throws Exception;

}
