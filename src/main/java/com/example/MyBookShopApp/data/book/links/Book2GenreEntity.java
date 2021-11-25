package com.example.MyBookShopApp.data.book.links;

import javax.persistence.*;

@Entity
@Table(name = "book2genre")
public class Book2GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_id",columnDefinition = "INT NOT NULL")
    private int bookId;

    @Column(name = "genre_id",columnDefinition = "INT NOT NULL")
    private int genreId;
}
