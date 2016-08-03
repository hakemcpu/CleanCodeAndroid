package test.com.cleancodesample.dagger;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This Scope define the life cycle of the component to be a per Fragment scope.
 */
@Scope
@Retention(RUNTIME)
public @interface PerFragment { }
