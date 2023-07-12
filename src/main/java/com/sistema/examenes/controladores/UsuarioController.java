package com.sistema.examenes.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
//import com.sistema.examenes.modelo.UsuarioRol;
import com.sistema.examenes.servicios.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository repo;

    @PostMapping("/")
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
    
    @PostMapping("/editar")
    public Usuario editarUsuario(@RequestBody Usuario usuario) throws Exception{
    	return repo.save(usuario);
    }


    @GetMapping("/{username}")
    public Usuario obtenerUsuario(@PathVariable("username") String username){
    	System.out.println(usuarioService.obtenerUsuario(username).getEmail());
        return usuarioService.obtenerUsuario(username);
    }

    @DeleteMapping("/{usuarioId}")
    public void eliminarUsuario(@PathVariable("usuarioId") Long usuarioId){
        usuarioService.eliminarUsuario(usuarioId);
    }

}
