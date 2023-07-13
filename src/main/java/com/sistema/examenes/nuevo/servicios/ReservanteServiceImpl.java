package com.sistema.examenes.nuevo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservanteRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

@Service
public class ReservanteServiceImpl implements ReservanteService{
	
	@Autowired
	private ReservanteRepository reservanteRepo;

	@Override
	public ApiResponse<Reservante> guardarReservante(Reservante reservante) {
		System.out.println("LLEGUE ACA");
		ApiResponse<Reservante> existente = obtenerPorTelefonoYUsuario(reservante)/* reservanteRepo.findByTelefono(reservaStr.getReservante().getTelefono())*/; 
		if(existente.isSuccess()) {
			return new ApiResponse<>(true,"",existente.getData());//reservanteRepo.save(reservaStr.getReservante());
		}else {
			Reservante nuevoReservante  = reservanteRepo.save(reservante);
			return new ApiResponse<>(true,"",nuevoReservante);
		}
	}

	@Override
	public ApiResponse<Reservante> obtenerPorTelefonoYUsuario(Reservante reservante) {
		System.out.println("LLEGUE ACA");
		Reservante r = reservanteRepo.findByTelefonoAndUsuario(reservante.getTelefono(), reservante.getUsuario());
		System.out.println("LLEGUE ACA");
		if(r!=null) {
			System.out.println("LLEGUE ACA");
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
