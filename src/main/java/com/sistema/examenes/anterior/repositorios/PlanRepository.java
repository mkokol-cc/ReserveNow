package com.sistema.examenes.anterior.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.examenes.anterior.modelo.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long>{

}
