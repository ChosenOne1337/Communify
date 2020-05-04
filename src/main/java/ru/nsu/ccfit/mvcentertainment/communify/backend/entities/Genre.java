package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "genre")
@Getter @Setter
public class Genre extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;

}
