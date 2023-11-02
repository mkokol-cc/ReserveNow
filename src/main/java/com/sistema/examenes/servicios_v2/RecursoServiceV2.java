package com.sistema.examenes.servicios_v2;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface RecursoServiceV2 {
	Recurso obtenerRecursoPorId(Long id) throws Exception;
	List<Recurso> listarRecursos(Usuario u);
	void eliminarRecurso(Long id, Usuario u) throws Exception;
	Recurso nuevoRecurso(Recurso r) throws Exception;
	Recurso editarRecurso(Recurso r, Usuario u) throws Exception;
	void borrarRecurso(Long idRecurso) throws Exception;
}
