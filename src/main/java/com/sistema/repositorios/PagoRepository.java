package com.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.modelo.pagosMP.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{

}
