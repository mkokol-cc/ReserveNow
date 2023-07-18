package com.sistema.examenes.nuevo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservanteRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;

@Service
public class ReservanteServiceImpl implements ReservanteService{
	
	@Autowired
	private ReservanteRepository reservanteRepo;

	@Override
	public ApiResponse<Reservante> guardarReservante(Reservante reservante) {
		try {
			ApiResponse<Reservante> existente = obtenerPorTelefonoYUsuario(reservante)/* reservanteRepo.findByTelefono(reservaStr.getReservante().getTelefono())*/; 
			if(existente.isSuccess()) {
				return new ApiResponse<>(true,"",existente.getData());//reservanteRepo.save(reservaStr.getReservante());
			}else {
				Reservante nuevoReservante  = reservanteRepo.save(reservante);
				return new ApiResponse<>(true,"",nuevoReservante);
			}	
		}catch(Exception e){
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<Reservante> obtenerPorTelefonoYUsuario(Reservante reservante) {
		try {
			Reservante r = reservanteRepo.findByTelefonoAndUsuario(reservante.getTelefono(), reservante.getUsuario());
			if(r!=null) {
				return new ApiResponse<>(true,"",r);
			}
			return new ApiResponse<>(false,"No se obtuvo el Reservante",r);	
		}catch(Exception e){
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<Reservante> editarReservante(Reservante reservante, long idUsuario) {
		try {
			Reservante r = reservanteRepo.getById(reservante.getId());
			if(r.getUsuario().getId()==idUsuario) {
				reservante.setReservas(r.getReservas());
				reservante.setUsuario(r.getUsuario());
				Reservante guardado = reservanteRepo.save(reservante);
				return new ApiResponse<>(true,"",guardado);
			}
			return new ApiResponse<>(false,"Usuario no autorizado",r);	
		}catch(Exception e){
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<List<Reservante>> listarReservanteDeUsuario(Usuario usuario) {
		try {
			List<Reservante> reservantes = reservanteRepo.findByUsuario(usuario);
			return new ApiResponse<>(true,"",reservantes);
		}catch(Exception e){
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

}
