package com.sistema.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.modelo.usuario.Usuario;
import com.sistema.servicios.UsuarioService;

@RestController
//@RequestMapping("")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuario/new")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) throws Exception{
        /*usuario.setPerfil("default.png");
        Set<UsuarioRol> usuarioRoles = new HashSet<>();
		*/
    	
        //Rol rol = new Rol();
        
    	//rol.setId(2L);
        //rol.setRolNombre("USER");
        /*
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

        usuarioRoles.add(usuarioRol);*/
        return usuarioService.guardarUsuario(usuario/*,*//*usuarioRoles*//*rol*/);
    }
    
    @PutMapping("/usuario")
    public Usuario editarUsuario(@RequestBody Usuario usuario) throws Exception{
    	return usuarioService.editarUsuario(usuario);
    }

    /*
    @GetMapping("/usuario/{username}")
    public Usuario obtenerUsuario(@PathVariable("username") String username){
    	System.out.println(usuarioService.obtenerUsuario(username).getEmail());
        return usuarioService.obtenerUsuario(username);
    }*/
    
    @GetMapping("/usuario")
    public Usuario obtenerUsuario(){
    	return usuarioService.obtenerUsuarioActual();
    	//System.out.println(usuarioService.obtenerUsuario(username).getEmail());
        //return usuarioService.obtenerUsuario(username);
    }

    @DeleteMapping("/usuario")
    public void eliminarUsuario(){
        usuarioService.eliminarUsuario(usuarioService.obtenerUsuarioActual().getId());
    }
    
    @GetMapping("/v1.1/public/{nombreEspacio}")
    public Usuario getUsuarioPorNombreEspacio(@PathVariable String nombreEspacio) {
    	return usuarioService.obtenerUsuarioPorPageId(nombreEspacio);
    }

}
