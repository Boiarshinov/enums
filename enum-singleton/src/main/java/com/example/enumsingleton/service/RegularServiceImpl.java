package com.example.enumsingleton.service;

import org.springframework.stereotype.Component;

@Component
public class RegularServiceImpl implements RegularService {
    @Override
    public String doWork() {
        System.out.println(String.join("\n",
            "=====================================",
            "Enum dependency injection works well!",
            "====================================="
        ));
        return "Enum dependency injection works well!";
    }
}
