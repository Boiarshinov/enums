package dev.boiarshinov.enumsinapi.api;

import dev.boiarshinov.enumsinapi.service.ReCaptchaValidationService;
import org.springframework.beans.factory.annotation.Autowired;

public enum EnumComponent {
    INSTANCE;

    private ReCaptchaValidationService reCaptchaValidationService;

    public void setReCaptchaValidationService(
            @Autowired ReCaptchaValidationService reCaptchaValidationService
    ) {
        this.reCaptchaValidationService = reCaptchaValidationService;
    }
}
