package com.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.modelo.pagosMP.Licencia;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long>{

}
