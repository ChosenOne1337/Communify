package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
public class AbstractDto<I extends Serializable> implements Serializable {

    private I id;

}