package com.betcoin.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Gamer.
 */
@Entity
@Table(name = "gamer")
public class Gamer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pseudo")
    private String pseudo;

    @Column(name = "email")
    private String email;

    @Column(name = "points")
    private Integer points;

    @Column(name = "last_connexion")
    private Instant lastConnexion;

    @Column(name = "winning_run")
    private Integer winningRun;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @OneToMany(mappedBy = "gamer")
    @JsonIgnore
    private Set<Pronostic> pronostics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public Gamer pseudo(String pseudo) {
        this.pseudo = pseudo;
        return this;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public Gamer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return points;
    }

    public Gamer points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Instant getLastConnexion() {
        return lastConnexion;
    }

    public Gamer lastConnexion(Instant lastConnexion) {
        this.lastConnexion = lastConnexion;
        return this;
    }

    public void setLastConnexion(Instant lastConnexion) {
        this.lastConnexion = lastConnexion;
    }

    public Integer getWinningRun() {
        return winningRun;
    }

    public Gamer winningRun(Integer winningRun) {
        this.winningRun = winningRun;
        return this;
    }

    public void setWinningRun(Integer winningRun) {
        this.winningRun = winningRun;
    }

    public Boolean isIsAdmin() {
        return isAdmin;
    }

    public Gamer isAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Set<Pronostic> getPronostics() {
        return pronostics;
    }

    public Gamer pronostics(Set<Pronostic> pronostics) {
        this.pronostics = pronostics;
        return this;
    }

    public Gamer addPronostics(Pronostic pronostic) {
        this.pronostics.add(pronostic);
        pronostic.setGamer(this);
        return this;
    }

    public Gamer removePronostics(Pronostic pronostic) {
        this.pronostics.remove(pronostic);
        pronostic.setGamer(null);
        return this;
    }

    public void setPronostics(Set<Pronostic> pronostics) {
        this.pronostics = pronostics;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gamer gamer = (Gamer) o;
        if (gamer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gamer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Gamer{" +
            "id=" + getId() +
            ", pseudo='" + getPseudo() + "'" +
            ", email='" + getEmail() + "'" +
            ", points=" + getPoints() +
            ", lastConnexion='" + getLastConnexion() + "'" +
            ", winningRun=" + getWinningRun() +
            ", isAdmin='" + isIsAdmin() + "'" +
            "}";
    }
}
