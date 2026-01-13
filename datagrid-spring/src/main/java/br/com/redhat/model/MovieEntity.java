package br.com.redhat.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movie")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String year;
    public String director;
    public String genre;
}

