package dev.boiarshinov.enumsinapi.service;

import dev.boiarshinov.enumsinapi.api.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GoogleReCaptchaClientImpl implements GoogleReCaptchaClient {
    @Override
    public Set<ErrorCode> validate(String recaptchaToken) {
        return null;
    }
}
