package com.betcoin.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Groupe.
 */
@Entity
@Table(name = "groupe")
public class Groupe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @OneToOne
    @JoinColumn(unique = true)
    private Team winner;

    @OneToOne
    @JoinColumn(unique = true)
    private Team second;

    @OneToMany(mappedBy = "groupe")
    @JsonIgnore
    private Set<Team> teams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Groupe groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Team getWinner() {
        return winner;
    }

    public Groupe winner(Team team) {
        this.winner = team;
        return this;
    }

    public void setWinner(Team team) {
        this.winner = team;
    }

    public Team getSecond() {
        return second;
    }

    public Groupe second(Team team) {
        this.second = team;
        return this;
    }

    public void setSecond(Team team) {
        this.second = team;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public Groupe teams(Set<Team> teams) {
        this.teams = teams;
        return this;
    }

    public Groupe addTeams(Team team) {
        this.teams.add(team);
        team.setGroupe(this);
        return this;
    }

    public Groupe removeTeams(Team team) {
        this.teams.remove(team);
        team.setGroupe(null);
        return this;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
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
        Groupe groupe = (Groupe) o;
        if (groupe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Groupe{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
