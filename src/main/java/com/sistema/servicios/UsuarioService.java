package com.sistema.servicios;


import com.sistema.modelo.usuario.Usuario;

public interface UsuarioService {

    public Usuario guardarUsuario(Usuario usuario) throws Exception;
    
    public Usuario editarUsuario(Usuario usuario) throws Exception;

    public Usuario obtenerUsuario(String username);

    public void eliminarUsuario(Long usuarioId);
    
    public Usuario obtenerUsuarioPorPageId(String url);
    
    public Long getIdUsuarioActual();
    
    public Usuario obtenerUsuarioActual();

	void verificarVtoLicencias() throws Exception;
}
