package com.sistema.examenes.anterior.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.repositorios.ReservaRepository;


@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
public class ReservaController {

	@Autowired
	private ReservaRepository repo;

	@GetMapping("/reservas")
	public String listarReservas() throws JsonProcessingException {
		List<Reserva> listaReservas = repo.findAll();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(listaReservas);
		return json;
	}
	
	@PostMapping("/reservas")
	public Reserva guardarReserva(@RequestBody Reserva reserva) {
		reserva.setEstado(null);
		return repo.save(reserva);
	}
	

}
