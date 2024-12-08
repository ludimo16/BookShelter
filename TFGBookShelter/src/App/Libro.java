/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import java.io.Serializable;

/**
 *
 * @author ludimo16
 */
public class Libro {
    private long id;
    private String titulo;
    private String autor;
    private String anioPublicacion; // Cambiado a String
    private String isbn;
    private boolean disponible;
    private String usuarioPrestado;

    // Constructor completo
    public Libro(long id, String titulo, String autor, String anioPublicacion, String isbn, boolean disponible, String usuarioPrestado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion; // Ahora es String
        this.isbn = isbn;
        this.disponible = disponible;
        this.usuarioPrestado = usuarioPrestado;
    }

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(String anioPublicacion) { // Cambiado a String
        this.anioPublicacion = anioPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getUsuarioPrestado() {
        return usuarioPrestado;
    }

    public void setUsuarioPrestado(String usuarioPrestado) {
        this.usuarioPrestado = usuarioPrestado;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anioPublicacion='" + anioPublicacion + '\'' +
                ", isbn='" + isbn + '\'' +
                ", disponible=" + disponible +
                ", usuarioPrestado='" + usuarioPrestado + '\'' +
                '}';
    }
}