package dev.boiarshinov.enumsindb.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter @Setter
@EqualsAndHashCode
@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Continent continent;

    public enum Continent {
        AFRICA,
        EURASIA,
        NORTH_AMERICA,
        SOUTH_AMERICA,
        ANTARCTICA,
        AUSTRALIA
    }

}
