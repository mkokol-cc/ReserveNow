package com.sistema.examenes.nuevo.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.RecursoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioEspecialService;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioService;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;

@Service
public class RecursoServiceImpl implements RecursoService{

	@Autowired
	private RecursoRepository recursoRepo;
	
	@Autowired
	private HorarioService horarioService;
	
	@Autowired
	private HorarioEspecialService horarioEspService;
	
	@Autowired
	private TipoTurnoService tipoTurnoService;
	
	/*
	@Autowired
	private AsignacionRecursoTipoTurnoService asignacionService;
	*/
	private final Validator validator;
	
    public RecursoServiceImpl(Validator validator) {
        this.validator = validator;
    }
	
    
    
	public Recurso validar(Recurso recursoDTO) throws Exception{
        Errors errors = new BeanPropertyBindingResult(recursoDTO, "recursoDTO");
        ValidationUtils.invokeValidator(validator, recursoDTO, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        } else {
        	return recursoDTO;
        }
    }
	
	
	private Recurso save(Recurso r) throws Exception{
		Recurso guardado = recursoRepo.save(r);
		if(guardado!=null) {
			guardado.setHorarios(r.getHorarios());
			setHorarios(guardado);
			guardado.setHorariosEspeciales(r.getHorariosEspeciales());
			setHorariosEspeciales(guardado);
			return guardado;
		}else {
			throw new Exception("Error al guardar el Recurso");
		}
	}
	
	@Override
	public Recurso guardarRecurso(Recurso recurso) throws Exception {
		return save(validar(recurso));
	}

	@Override
	public Recurso obtenerRecursoPorId(long idRecurso) throws Exception {
		Recurso r = recursoRepo.getById(idRecurso);
		if(r!=null) {
			return r;
		}else {
			throw new Exception("No se encontro el Recurso con el id "+idRecurso+".");
		}
	}

	@Override
	public Recurso editarRecurso(Recurso recurso) throws Exception {
		//obtener por id
		Recurso recursoOriginal = obtenerRecursoPorId(recurso.getId());
		if(recursoOriginal.getUsuario() == recurso.getUsuario()) {
			return guardarRecurso(recurso);//save
		}else {
			throw new Exception("Usuario no autorizado a editar el Recurso.");
		}
	}

	@Override
	public List<Recurso> listarRecurso(Usuario user) throws Exception {
		return recursoRepo.findByUsuario(user);
	}
	
	
	private Recurso guardarHorariosRecurso(Recurso r) throws Exception{
		r=setHorarios(r);
		r=setHorariosEspeciales(r);
		return r;
	}
	
	
	private Recurso setHorarios(Recurso r) throws Exception{
		if(!r.getHorarios().isEmpty()) {
			return horarioService.guardarListaHorariosRecurso(r);
		}
		return r;
	}
	
	
	private Recurso setHorariosEspeciales(Recurso r) throws Exception{
		if(!r.getHorariosEspeciales().isEmpty()) {
			return horarioEspService.guardarListaHorarioEspecialRecurso(r);
		}
		return r;
	}

	
	
	
	
	
	
	@Override
	public Recurso actualizarAsignaciones(List<Long> idTiposDeTurno, long recId,Usuario usuario) throws Exception{
		String mensajeError = "";
		for(TipoTurno tt : tipoTurnoService.listarTipoTurnoDeUsuario(usuario)) {
			if(idTiposDeTurno.contains(tt.getId())) {
				//guardar nueva asignacion
				System.out.println(" -GUARDAR la relacion entre recId:"+recId+" ttID:"+tt.getId()+"- ");
				//ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.guardarAsignacion(tt.getId(), recId, usuario.getId());
				//mensajeError = resp.isSuccess() ? mensajeError : mensajeError + resp.getMessage() + " ";
			}else {
				//eliminar asignacion
				System.out.println(" -ELIMINAR la relacion entre recId:"+recId+" ttID:"+tt.getId()+"- ");
				//ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.eliminarAsignacion(tt.getId(), recId, usuario.getId());
				//mensajeError = resp.isSuccess() ? mensajeError : mensajeError + resp.getMessage() + " ";
			}
		}
		if(!mensajeError.equals("")) {
			throw new Exception(mensajeError);
		}
		return null;//falta de implementar
	}
}
