package com.example.web;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.FilmRepositorie;
import com.example.entities.Film;
import com.example.entities.Ticket;
import com.example.entities.TicketForm;
import com.example.services.ICinemaService;

@RestController
@CrossOrigin("*")
public class CinemaRestController {

	@Autowired
	private FilmRepositorie filmRepositorie;

	@Autowired
	private ICinemaService cinemaService;

	@GetMapping(path = "/listFilms")
	public List<Film> listFilm() {
		return filmRepositorie.findAll();
	}

	// MediaType.APPLICATION_PDF_VALUE // si vs voulez télécharger fichier pdf
	@GetMapping(path = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] image(@PathVariable(name = "id") Long id) throws Exception {		
		return Files.readAllBytes(cinemaService.getImage(id));
	}

	@PostMapping("/payerTickets")
	@Transactional
	public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm) {
		List<Ticket> listTickets = new ArrayList<Ticket>();
		cinemaService.payerTickets(listTickets, ticketForm);
		return listTickets;
	}

}
