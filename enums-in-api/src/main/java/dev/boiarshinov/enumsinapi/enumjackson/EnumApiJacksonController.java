package dev.boiarshinov.enumsinapi.enumjackson;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jackson")
public class EnumApiJacksonController {

    @GetMapping
    public School getSchool() {
        return School.builder()
                .address("Moscow, Lenina 3")
                .number("123G")
                .type(School.SchoolType.ELEMENTARY)
                .build();
    }

    @PostMapping
    public void createSchool(@RequestBody School school) {
        System.out.println(school.toString());
    }
}
