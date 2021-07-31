package dev.boiarshinov.enumsindb.db;

import dev.boiarshinov.enumsindb.db.PlanetBase.SolarPlanet;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter
public class SolarPlanetConverter
    implements AttributeConverter<SolarPlanet, String>
{
    @Override
    public String convertToDatabaseColumn(SolarPlanet planet) {
        return planet.name();
    }

    @Override
    public SolarPlanet convertToEntityAttribute(String planetName) {
        try {
            return SolarPlanet.valueOf(planetName);
        } catch (IllegalArgumentException ignored) {
            log.warn("Unknown planet '{}' was found while parsing data from db", planetName);
            return SolarPlanet.UNKNOWN_MIDGET;
        }
    }
}
