package com.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.modelo.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    public Usuario findByEmail(String username);
    
    public Usuario findByNombreEspacioPersonal(String nombreEspacioPersonal);

}
