package com.betcoin.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Pronotype.
 */
@Entity
@Table(name = "pronotype")
public class Pronotype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pronotype_name")
    private String pronotypeName;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "points")
    private Integer points;

    @OneToMany(mappedBy = "pronotype")
    @JsonIgnore
    private Set<Pronostic> pronostics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPronotypeName() {
        return pronotypeName;
    }

    public Pronotype pronotypeName(String pronotypeName) {
        this.pronotypeName = pronotypeName;
        return this;
    }

    public void setPronotypeName(String pronotypeName) {
        this.pronotypeName = pronotypeName;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public Pronotype expirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getPoints() {
        return points;
    }

    public Pronotype points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Set<Pronostic> getPronostics() {
        return pronostics;
    }

    public Pronotype pronostics(Set<Pronostic> pronostics) {
        this.pronostics = pronostics;
        return this;
    }

    public Pronotype addPronostic(Pronostic pronostic) {
        this.pronostics.add(pronostic);
        pronostic.setPronotype(this);
        return this;
    }

    public Pronotype removePronostic(Pronostic pronostic) {
        this.pronostics.remove(pronostic);
        pronostic.setPronotype(null);
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
        Pronotype pronotype = (Pronotype) o;
        if (pronotype.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pronotype.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pronotype{" +
            "id=" + getId() +
            ", pronotypeName='" + getPronotypeName() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", points=" + getPoints() +
            "}";
    }
}
