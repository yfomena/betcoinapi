package com.betcoin.backend.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Pronostic.
 */
@Entity
@Table(name = "pronostic")
public class Pronostic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prono_date")
    private Instant pronoDate;

    @Column(name = "match_or_group_orcompet_id")
    private Long matchOrGroupOrcompetId;

    @Column(name = "score_home")
    private Integer scoreHome;

    @Column(name = "score_away")
    private Integer scoreAway;

    @Column(name = "winner")
    private Long winner;

    @ManyToOne
    private Gamer gamer;

    @ManyToOne
    private Pronotype pronotype;

    @ManyToOne
    private Pronostatus pronostatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPronoDate() {
        return pronoDate;
    }

    public Pronostic pronoDate(Instant pronoDate) {
        this.pronoDate = pronoDate;
        return this;
    }

    public void setPronoDate(Instant pronoDate) {
        this.pronoDate = pronoDate;
    }

    public Long getMatchOrGroupOrcompetId() {
        return matchOrGroupOrcompetId;
    }

    public Pronostic matchOrGroupOrcompetId(Long matchOrGroupOrcompetId) {
        this.matchOrGroupOrcompetId = matchOrGroupOrcompetId;
        return this;
    }

    public void setMatchOrGroupOrcompetId(Long matchOrGroupOrcompetId) {
        this.matchOrGroupOrcompetId = matchOrGroupOrcompetId;
    }

    public Integer getScoreHome() {
        return scoreHome;
    }

    public Pronostic scoreHome(Integer scoreHome) {
        this.scoreHome = scoreHome;
        return this;
    }

    public void setScoreHome(Integer scoreHome) {
        this.scoreHome = scoreHome;
    }

    public Integer getScoreAway() {
        return scoreAway;
    }

    public Pronostic scoreAway(Integer scoreAway) {
        this.scoreAway = scoreAway;
        return this;
    }

    public void setScoreAway(Integer scoreAway) {
        this.scoreAway = scoreAway;
    }

    public Long getWinner() {
        return winner;
    }

    public Pronostic winner(Long winner) {
        this.winner = winner;
        return this;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }

    public Gamer getGamer() {
        return gamer;
    }

    public Pronostic gamer(Gamer gamer) {
        this.gamer = gamer;
        return this;
    }

    public void setGamer(Gamer gamer) {
        this.gamer = gamer;
    }

    public Pronotype getPronotype() {
        return pronotype;
    }

    public Pronostic pronotype(Pronotype pronotype) {
        this.pronotype = pronotype;
        return this;
    }

    public void setPronotype(Pronotype pronotype) {
        this.pronotype = pronotype;
    }

    public Pronostatus getPronostatus() {
        return pronostatus;
    }

    public Pronostic pronostatus(Pronostatus pronostatus) {
        this.pronostatus = pronostatus;
        return this;
    }

    public void setPronostatus(Pronostatus pronostatus) {
        this.pronostatus = pronostatus;
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
        Pronostic pronostic = (Pronostic) o;
        if (pronostic.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pronostic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pronostic{" +
            "id=" + getId() +
            ", pronoDate='" + getPronoDate() + "'" +
            ", matchOrGroupOrcompetId=" + getMatchOrGroupOrcompetId() +
            ", scoreHome=" + getScoreHome() +
            ", scoreAway=" + getScoreAway() +
            ", winner=" + getWinner() +
            "}";
    }
}
