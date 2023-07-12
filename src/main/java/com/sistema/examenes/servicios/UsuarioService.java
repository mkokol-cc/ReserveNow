package com.sistema.examenes.servicios;


import com.sistema.examenes.modelo.usuario.Usuario;

public interface UsuarioService {

    public Usuario guardarUsuario(Usuario usuario/*, Rol rol*//*Set<UsuarioRol> usuarioRoles*/) throws Exception;

    public Usuario obtenerUsuario(String username);

    public void eliminarUsuario(Long usuarioId);
}
