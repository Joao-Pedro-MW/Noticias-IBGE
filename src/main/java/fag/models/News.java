package fag.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class News {

    private int id;
    private String tipo;
    private String titulo;
    private String introducao;
    @JsonProperty("data_publicacao")
    private LocalDateTime dataPublicacao;
    private String link;
    private final String fonte = "IBGE";

    public News() {
    }

    public News(int id, String tipo, String titulo, String introducao, LocalDateTime dataPublicacao, String link) {
        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.introducao = introducao;
        this.dataPublicacao = dataPublicacao;
        this.link = link;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIntroducao() {
        return introducao;
    }

    public void setIntroducao(String introducao) {
        this.introducao = introducao;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFonte() {
        return fonte;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Tipo: " + tipo +
                ", Título: " + titulo +
                ", Introdução: " + introducao +
                ", Data: " + (dataPublicacao != null ? dataPublicacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "N/A") +
                ", Link: " + link +
                ", Fonte: " + fonte;
    }
}