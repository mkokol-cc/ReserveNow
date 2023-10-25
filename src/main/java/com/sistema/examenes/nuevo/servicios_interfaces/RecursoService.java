package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface RecursoService {
	
	public Recurso guardarRecurso(Recurso recurso) throws Exception;
	
	public Recurso editarRecurso(Recurso recurso) throws Exception;
	
	public List<Recurso> listarRecurso(Usuario userId) throws Exception;
	
	public Recurso obtenerRecursoPorId(long idRecurso) throws Exception;
	
	public Recurso actualizarAsignaciones(List<Long> idTiposDeTurno, long recId,Usuario usuario) throws Exception;

}
