package org.network.devicemon.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Pattern(regexp = "^([0-9a-fA-F]{2})(:[0-9a-fA-F]{2}){5}$")
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
@ReportAsSingleViolation
public @interface MacAddress {
    String message() default "must be a valid MAC address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
