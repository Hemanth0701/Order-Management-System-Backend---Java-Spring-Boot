package com.tenjiku.omg.entity;

import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Entity
@Setter
@Getter
public class Admin extends UserDetails {

}
