package com.sistema.examenes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.modelo.usuario.pagos.Licencia;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long>{

}
