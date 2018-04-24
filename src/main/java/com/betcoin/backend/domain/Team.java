package com.betcoin.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "color_home")
    private String colorHome;

    @Column(name = "color_away")
    private String colorAway;

    @OneToMany(mappedBy = "team1")
    @JsonIgnore
    private Set<Game> gameHomes = new HashSet<>();

    @OneToMany(mappedBy = "team2")
    @JsonIgnore
    private Set<Game> gameAways = new HashSet<>();

    @ManyToOne
    private Groupe groupe;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Team teamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getColorHome() {
        return colorHome;
    }

    public Team colorHome(String colorHome) {
        this.colorHome = colorHome;
        return this;
    }

    public void setColorHome(String colorHome) {
        this.colorHome = colorHome;
    }

    public String getColorAway() {
        return colorAway;
    }

    public Team colorAway(String colorAway) {
        this.colorAway = colorAway;
        return this;
    }

    public void setColorAway(String colorAway) {
        this.colorAway = colorAway;
    }

    public Set<Game> getGameHomes() {
        return gameHomes;
    }

    public Team gameHomes(Set<Game> games) {
        this.gameHomes = games;
        return this;
    }

    public Team addGameHome(Game game) {
        this.gameHomes.add(game);
        game.setTeam1(this);
        return this;
    }

    public Team removeGameHome(Game game) {
        this.gameHomes.remove(game);
        game.setTeam1(null);
        return this;
    }

    public void setGameHomes(Set<Game> games) {
        this.gameHomes = games;
    }

    public Set<Game> getGameAways() {
        return gameAways;
    }

    public Team gameAways(Set<Game> games) {
        this.gameAways = games;
        return this;
    }

    public Team addGameAway(Game game) {
        this.gameAways.add(game);
        game.setTeam2(this);
        return this;
    }

    public Team removeGameAway(Game game) {
        this.gameAways.remove(game);
        game.setTeam2(null);
        return this;
    }

    public void setGameAways(Set<Game> games) {
        this.gameAways = games;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public Team groupe(Groupe groupe) {
        this.groupe = groupe;
        return this;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
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
        Team team = (Team) o;
        if (team.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), team.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", teamName='" + getTeamName() + "'" +
            ", colorHome='" + getColorHome() + "'" +
            ", colorAway='" + getColorAway() + "'" +
            "}";
    }
}
