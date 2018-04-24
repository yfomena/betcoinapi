package com.betcoin.backend.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Competition.
 */
@Entity
@Table(name = "competition")
public class Competition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "competition_name")
    private String competitionName;

    @OneToOne
    @JoinColumn(unique = true)
    private Team winner;

    @OneToOne
    @JoinColumn(unique = true)
    private Team second;

    @OneToOne
    @JoinColumn(unique = true)
    private Team third;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public Competition competitionName(String competitionName) {
        this.competitionName = competitionName;
        return this;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Team getWinner() {
        return winner;
    }

    public Competition winner(Team team) {
        this.winner = team;
        return this;
    }

    public void setWinner(Team team) {
        this.winner = team;
    }

    public Team getSecond() {
        return second;
    }

    public Competition second(Team team) {
        this.second = team;
        return this;
    }

    public void setSecond(Team team) {
        this.second = team;
    }

    public Team getThird() {
        return third;
    }

    public Competition third(Team team) {
        this.third = team;
        return this;
    }

    public void setThird(Team team) {
        this.third = team;
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
        Competition competition = (Competition) o;
        if (competition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), competition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Competition{" +
            "id=" + getId() +
            ", competitionName='" + getCompetitionName() + "'" +
            "}";
    }
}
