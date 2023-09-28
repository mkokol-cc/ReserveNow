package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface RecursoService {
	
	public ApiResponse<Recurso> guardarRecurso(Recurso recurso);
	
	public ApiResponse<Recurso> editarRecurso(Recurso recurso);
	
	public ApiResponse<List<Recurso>> listarRecurso(Usuario userId);
	
	public ApiResponse<Recurso> obtenerRecursoPorId(long idRecurso);
	
	public ApiResponse<Recurso> actualizarAsignaciones(List<Long> idTiposDeTurno, long recId,Usuario usuario);

}
