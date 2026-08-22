package com.vikram.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Applies the retry analyser to every test.
 *
 * Previously each test opted in with retryAnalyzer = Retry.class, which meant three of
 * seven classes had it and the rest silently did not. Registering the transformer in the
 * suite files makes the policy uniform and removes the per-test annotation entirely.
 */
public class RetryTransformer implements IAnnotationTransformer {

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		annotation.setRetryAnalyzer(Retry.class);
	}
}
