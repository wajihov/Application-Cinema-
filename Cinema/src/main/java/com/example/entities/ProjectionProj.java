package com.example.entities;

import java.util.Collection;
import java.util.Date;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "p1", types = { com.example.entities.Projection.class })
public interface ProjectionProj {

	public Long getId();

	public Date getDateProjection();

	public double getPrix();

	public Salle getsalle();

	public Film getFilm();

	public Collection<Ticket> getTickets();

	public Seance getSeance();

}
