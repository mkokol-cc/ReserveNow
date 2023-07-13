package com.sistema.examenes.nuevo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservanteRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public class ReservanteServiceImpl implements ReservanteService{
	
	@Autowired
	private ReservanteRepository reservanteRepo;

	@Override
	public ApiResponse<Reservante> guardarReservante(Reservante reservante) {
		ApiResponse<Reservante> existente = obtenerPorTelefono(reservante.getTelefono())/* reservanteRepo.findByTelefono(reservaStr.getReservante().getTelefono())*/; 
		if(existente.isSuccess()) {
			return new ApiResponse<>(true,"",reservanteRepo.save(reservante));//reservanteRepo.save(reservaStr.getReservante());
		}
		return existente;
	}

	@Override
	public ApiResponse<Reservante> obtenerPorTelefono(String telefono) {
		Reservante r = reservanteRepo.findByTelefono(telefono);
		if(r!=null) {
			return new ApiResponse<>(true,"",r);
		}
		return new ApiResponse<>(false,"No se obtuvo el Reservante",r);
	}

	@Override
	public ApiResponse<Reservante> editarReservante(Reservante reservante) {
		// TODO Auto-generated method stub
		return null;
	}

}
