package dev.boiarshinov.enumsindb.db;

import dev.boiarshinov.enumsindb.db.PlanetBase.SolarPlanet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
            return SolarPlanet.UNKNOWN_MIDGET;
        }
    }
}
