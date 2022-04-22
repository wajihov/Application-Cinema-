package com.example.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cinema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 80)
	private String name;
	@Column(length = 80)
	private double longitude, latitude, altitude;
	private int nbreSalles;
	@ManyToOne
	private Ville ville;
	@OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Salle> salles;
}
