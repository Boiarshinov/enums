package dev.boiarshinov.enumsinapi.service;

import dev.boiarshinov.enumsinapi.api.ErrorCode;

import java.util.Set;

public interface GoogleReCaptchaClient {
    Set<ErrorCode> validate(String recaptchaToken);
}
