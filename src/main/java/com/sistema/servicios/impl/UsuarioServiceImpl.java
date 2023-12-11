package com.sistema.servicios.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.anterior.modelo.Reserva;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.notificaciones.NotificationsService;
import com.sistema.repositorios.UsuarioRepository;
import com.sistema.servicios.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private NotificationsService notificationsService;
    
	private final Validator validator;
	
    public UsuarioServiceImpl(Validator validator) {
        this.validator = validator;
    }
	private void validar(Usuario u) throws Exception{
		Errors errors = new BeanPropertyBindingResult(u, "usuario");
        ValidationUtils.invokeValidator(validator, u, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
	private Usuario save(Usuario u) throws Exception{
		validar(u);
		return usuarioRepository.save(u);
	}

    @Override
    public Usuario guardarUsuario(Usuario usuario/*, Rol rol*//*, Set<UsuarioRol> usuarioRoles*/) throws Exception {
        Usuario usuarioLocal = usuarioRepository.findByEmail(usuario.getUsername());
        if(usuarioLocal != null){
            System.out.println("El usuario ya existe");
            throw new Exception("El usuario ya esta presente");
        }
        else{
            usuarioLocal = save(usuario);
        }
        return usuarioLocal;
    }

    @Override
    public Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByEmail(username);
    }

    @Override
    public void eliminarUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }

	@Override
	public Usuario obtenerUsuarioPorPageId(String url) {
		try {
			Usuario u = usuarioRepository.findByNombreEspacioPersonal(url);
			return u;
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public Long getIdUsuarioActual() {
		// Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Obtener los detalles del usuario autenticado
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            // Aquí puedes realizar las operaciones necesarias con el usuario autenticado
            try {
            	return usuarioRepository.findByEmail(username).getId();
            }catch(Exception e) {
            	return (long) 0;
            }
        }
        // Si no se encuentra un usuario autenticado, puedes manejarlo según tus necesidades
        return (long) 0;
	}

	@Override
	public Usuario obtenerUsuarioActual() {
		// Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Obtener los detalles del usuario autenticado
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // Aquí puedes realizar las operaciones necesarias con el usuario autenticado
            try {
            	return usuarioRepository.findByEmail(username);
            }catch(Exception e) {
            	return null;
            }
        }

        // Si no se encuentra un usuario autenticado, puedes manejarlo según tus necesidades
        return null;
	}

	@Override
	public Usuario editarUsuario(Usuario usuario) throws Exception {
		Usuario editado = obtenerUsuarioActual().editar(usuario);
		return save(editado);
	}
	
	@Override
	public void verificarVtoLicencias() throws Exception {
		List<Usuario> usuarios = usuarioRepository.findAll();
		for(Usuario u : usuarios) {
			if(u.getFechaVtoLicencia().minusDays(4).isBefore(LocalDate.now())) {
				notificationsService.notificarVencimientoLicencia(u);
			}
		}
	}

}