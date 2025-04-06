package com.topay.common.annotation;

import com.topay.user.domain.Level;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface CurrentUser {

    Level authority() default Level.UN_AUTH;

}
