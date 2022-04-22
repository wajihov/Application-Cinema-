package com.example.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.CategorieRepositorie;
import com.example.dao.CinemaRepository;
import com.example.dao.FilmRepositorie;
import com.example.dao.PlaceRepositorie;
import com.example.dao.ProjectionRepositorie;
import com.example.dao.SalleRepositorie;
import com.example.dao.SeanceRepositorie;
import com.example.dao.TicketRepositorie;
import com.example.dao.VilleRepositorie;
import com.example.entities.Categorie;
import com.example.entities.Cinema;
import com.example.entities.Film;
import com.example.entities.Place;
import com.example.entities.Projection;
import com.example.entities.Salle;
import com.example.entities.Seance;
import com.example.entities.Ticket;
import com.example.entities.TicketForm;
import com.example.entities.Ville;

@Transactional
@Service
public class CinemaServiceImpl implements ICinemaService {

	@Autowired
	private VilleRepositorie villeRepositorie;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepositorie salleRepositorie;
	@Autowired
	private PlaceRepositorie placeRepositorie;
	@Autowired
	private SeanceRepositorie seanceRepositorie;
	@Autowired
	private FilmRepositorie filmRepositorie;
	@Autowired
	private ProjectionRepositorie projectionRepositorie;
	@Autowired
	private CategorieRepositorie categorieRepositorie;
	@Autowired
	private TicketRepositorie ticketRepositorie;

	@Override
	public void InitVilles() {
		Stream.of("Casablanca", "Marrakech", "Rebat", "Tanger").forEach(nameVille -> {
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepositorie.save(ville);
		});
	}

	@Override
	public void initCinema() {
		villeRepositorie.findAll().forEach(ville -> {
			Stream.of("MegaRama", "IMAX", "Foundou", "Chahrazed", "Daouliz").forEach(nameCinema -> {
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setVille(ville);
				cinema.setNbreSalles(3 + (int) (Math.random() * 7));
				cinemaRepository.save(cinema);
			});
		});
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema -> {
			for (int i = 0; i < cinema.getNbreSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + (i + 1));
				salle.setCinema(cinema);
				salle.setNbrePlace(20 + (int) (Math.random() * 15));
				salleRepositorie.save(salle);
			}
		});
	}

	@Override
	public void initPlaces() {
		salleRepositorie.findAll().forEach(salle -> {
			for (int i = 0; i < salle.getNbrePlace(); i++) {
				Place place = new Place();
				place.setNumero(i + 1);
				place.setSalle(salle);
				placeRepositorie.save(place);
			}
		});
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepositorie.save(seance);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void initCategories() {
		Stream.of("Drama", "Fiction", "Histoire", "Actions").forEach(cat -> {
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepositorie.save(categorie);
		});
	}

	@Override
	public void initFilms() {
		List<Categorie> categories = categorieRepositorie.findAll();
		double[] duree = { 1, 1.5, 2, 2.5, 3.15 };
		Stream.of("12 homme en claire", "Forest Gump", "Green book", "la ligne verte", "le parien",
				"le seigneur des anneaux").forEach(titreFilm -> {
					Film film = new Film();
					film.setTitre(titreFilm);
					film.setDuree(duree[new Random().nextInt(duree.length)]);
					film.setPhoto(titreFilm.replaceAll(" ", "") + ".jpg");
					film.setCategorie(categories.get(new Random().nextInt(categories.size())));
					filmRepositorie.save(film);
				});
	}

	@Override
	public void initProjections() {
		double[] prices = new double[] { 7, 9.5, 25, 35, 120 };
		List<Film> films = filmRepositorie.findAll();
		villeRepositorie.findAll().forEach(ville -> {
			ville.getCinemas().forEach(cinema -> {
				cinema.getSalles().forEach(salle -> {
					int index = new Random().nextInt(films.size());
					Film film = films.get(index);
					seanceRepositorie.findAll().forEach(seance -> {
						Projection projection = new Projection();
						projection.setDateProjection(new Date());
						projection.setFilm(film);
						projection.setPrix(prices[new Random().nextInt(prices.length)]);
						projection.setSalle(salle);
						projection.setSeance(seance);
						projectionRepositorie.save(projection);
					});

				});
			});
		});
	}

	@Override
	public void initTickets() {
		projectionRepositorie.findAll().forEach(p -> {
			p.getSalle().getPlaces().forEach(place -> {
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReservation(false);
				ticketRepositorie.save(ticket);
			});

		});
	}

	@Override
	public List<Ticket> payerTickets(List<Ticket> listTickets, TicketForm ticketForm) {
		ticketForm.getTickets().forEach(idTicket -> {
			Ticket ticket = ticketRepositorie.findById(idTicket).get();
			ticket.setNomClient(ticketForm.getNomClient());
			ticket.setCodePayement(ticketForm.getCodePayement());
			ticket.setReservation(true);
			listTickets.add(ticket);
		});
		return listTickets;
	}

	@Override
	public Path getImage(Long id) {
		Film film = filmRepositorie.findById(id).get();
		String photoName = film.getPhoto();
		File file = new File("C:/Eclipse/cinema/image/" + photoName);
		// File file = new File(System.getProperty("user.home") + "/cinema/images/" +
		// photoName);
		Path path = Paths.get(file.toURI());
		return path;
	}

}
