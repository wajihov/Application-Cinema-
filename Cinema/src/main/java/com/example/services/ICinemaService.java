package com.example.services;

import java.nio.file.Path;
import java.util.List;

import com.example.entities.Ticket;
import com.example.entities.TicketForm;

public interface ICinemaService {

	public void InitVilles();

	public void initCinema();

	public void initSalles();

	public void initPlaces();

	public void initSeances();

	public void initCategories();

	public void initFilms();

	public void initProjections();

	public void initTickets();
	
	public Path getImage(Long id);

	public List<Ticket> payerTickets(List<Ticket> listTickets, TicketForm ticketForm);

}
