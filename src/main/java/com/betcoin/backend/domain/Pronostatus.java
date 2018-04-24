package com.betcoin.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Pronostatus.
 */
@Entity
@Table(name = "pronostatus")
public class Pronostatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pronostatus_name")
    private String pronostatusName;

    @OneToMany(mappedBy = "pronostatus")
    @JsonIgnore
    private Set<Pronostic> pronostics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPronostatusName() {
        return pronostatusName;
    }

    public Pronostatus pronostatusName(String pronostatusName) {
        this.pronostatusName = pronostatusName;
        return this;
    }

    public void setPronostatusName(String pronostatusName) {
        this.pronostatusName = pronostatusName;
    }

    public Set<Pronostic> getPronostics() {
        return pronostics;
    }

    public Pronostatus pronostics(Set<Pronostic> pronostics) {
        this.pronostics = pronostics;
        return this;
    }

    public Pronostatus addPronostic(Pronostic pronostic) {
        this.pronostics.add(pronostic);
        pronostic.setPronostatus(this);
        return this;
    }

    public Pronostatus removePronostic(Pronostic pronostic) {
        this.pronostics.remove(pronostic);
        pronostic.setPronostatus(null);
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
        Pronostatus pronostatus = (Pronostatus) o;
        if (pronostatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pronostatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pronostatus{" +
            "id=" + getId() +
            ", pronostatusName='" + getPronostatusName() + "'" +
            "}";
    }
}
