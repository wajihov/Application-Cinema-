package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.entities.Film;
import com.example.entities.Salle;
import com.example.entities.Ticket;
import com.example.services.ICinemaService;

@SpringBootApplication
@CrossOrigin("*")
public class CinemaApplication implements CommandLineRunner {

	@Autowired
	private ICinemaService cinemaService;

	@Autowired
	private RepositoryRestConfiguration repositoryRestConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(CinemaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repositoryRestConfiguration.exposeIdsFor(Film.class, Salle.class, Ticket.class);
		cinemaService.InitVilles();
		cinemaService.initCinema();
		cinemaService.initSalles();
		cinemaService.initPlaces();
		cinemaService.initSeances();
		cinemaService.initCategories();
		cinemaService.initFilms();
		cinemaService.initProjections();
		cinemaService.initTickets();
	}

}
