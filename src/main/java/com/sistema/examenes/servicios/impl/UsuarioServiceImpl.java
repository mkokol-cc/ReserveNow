package com.sistema.examenes.servicios.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
    @Autowired
    private RolRepository rolRepository;*/

    @Override
    public Usuario guardarUsuario(Usuario usuario/*, Rol rol*//*, Set<UsuarioRol> usuarioRoles*/) throws Exception {
        Usuario usuarioLocal = usuarioRepository.findByEmail(usuario.getUsername());
        if(usuarioLocal != null){
            System.out.println("El usuario ya existe");
            throw new Exception("El usuario ya esta presente");
        }
        else{/*
            for(UsuarioRol usuarioRol:usuarioRoles){
                rolRepository.save(usuarioRol.getRol());
            }*/
            //usuario.setRol(rol);//.getUsuarioRoles().addAll(usuarioRoles);
            usuarioLocal = usuarioRepository.save(usuario);
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
			Usuario u = usuarioRepository.findByDbUrl(url);
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

}