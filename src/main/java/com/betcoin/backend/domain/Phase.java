package com.betcoin.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Phase.
 */
@Entity
@Table(name = "phase")
public class Phase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phase_name")
    private String phaseName;

    @OneToMany(mappedBy = "phase")
    @JsonIgnore
    private Set<Game> games = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public Phase phaseName(String phaseName) {
        this.phaseName = phaseName;
        return this;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public Set<Game> getGames() {
        return games;
    }

    public Phase games(Set<Game> games) {
        this.games = games;
        return this;
    }

    public Phase addGame(Game game) {
        this.games.add(game);
        game.setPhase(this);
        return this;
    }

    public Phase removeGame(Game game) {
        this.games.remove(game);
        game.setPhase(null);
        return this;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
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
        Phase phase = (Phase) o;
        if (phase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), phase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Phase{" +
            "id=" + getId() +
            ", phaseName='" + getPhaseName() + "'" +
            "}";
    }
}
