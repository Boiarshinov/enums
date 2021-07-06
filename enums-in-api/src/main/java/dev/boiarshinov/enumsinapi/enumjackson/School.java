package dev.boiarshinov.enumsinapi.enumjackson;

import lombok.Builder;
import lombok.Data;
import org.enumapi.annotations.EnumApi;

@Data
@Builder
public class School {
    private String address;
    private String number;
    private Type type;

    @EnumApi(enumClass = SchoolType.class)
    public interface Type { }

    public enum SchoolType implements Type {
        KINDER_GARDEN,
        ELEMENTARY,
        MIDDLE,
        HIGH
    }
}
