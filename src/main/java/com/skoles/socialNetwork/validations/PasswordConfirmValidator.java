package com.skoles.socialNetwork.validations;

import com.skoles.socialNetwork.annotations.PasswordConfirm;
import com.skoles.socialNetwork.payload.request.SignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        SignUpRequest signUpRequest = (SignUpRequest) value;
        return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
    }

    @Override
    public void initialize(PasswordConfirm constraintAnnotation) {}
}
