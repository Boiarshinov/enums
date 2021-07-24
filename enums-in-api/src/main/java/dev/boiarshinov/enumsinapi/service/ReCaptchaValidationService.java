package dev.boiarshinov.enumsinapi.service;

import dev.boiarshinov.enumsinapi.api.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReCaptchaValidationService {

    private final GoogleReCaptchaClient googleReCaptchaClient;

    boolean validateReCaptcha(String recaptchaToken) {
        Set<ErrorCode> validationResult = googleReCaptchaClient.validate(recaptchaToken);
        if (validationResult.isEmpty()) {
            return true;
        }

        if(ErrorCode.RECOVERABLE_ERRORS.containsAll(validationResult)) {
            /* try to recover */
        }

        return false;
    }
}
