package com.sistema.anterior.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.anterior.modelo.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion,Long>{	
}
