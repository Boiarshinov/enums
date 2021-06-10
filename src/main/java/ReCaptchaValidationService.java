import lombok.RequiredArgsConstructor;

import java.util.Set;

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


    private interface GoogleReCaptchaClient {
        Set<ErrorCode> validate(String recaptchaToken);
    }
}
