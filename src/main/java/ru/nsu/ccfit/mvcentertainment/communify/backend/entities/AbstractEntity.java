package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Getter @Setter
@EqualsAndHashCode
public class AbstractEntity<ID extends Serializable> implements Serializable {

    @Id
    @Access(value = AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private ID id;

}