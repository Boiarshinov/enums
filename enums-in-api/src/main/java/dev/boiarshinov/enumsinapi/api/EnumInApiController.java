package dev.boiarshinov.enumsinapi.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;

@Slf4j
@RestController
public class EnumInApiController {

    @GetMapping("/simple")
    public Lesson getModelWithSimpleEnum() {
        return Lesson.builder()
            .discipline("Biology")
            .dayOfWeek(DayOfWeek.MONDAY)
            .order(5)
            .cabinet("215A").build();
    }

    @PostMapping("/jsonValue")
    public TestResult postAndGetWithConversion(@RequestBody TestResult testResult) {
        log.info(testResult.toString());
        return testResult;
    }
}
