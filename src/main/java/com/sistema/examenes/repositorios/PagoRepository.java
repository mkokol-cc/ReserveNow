package com.sistema.examenes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.modelo.usuario.pagos.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{

}
