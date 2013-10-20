package com.quesity.test.testrunner;

import android.app.Application;
import android.content.Context;
import android.test.InstrumentationTestRunner;

public class CustomTestRunner extends InstrumentationTestRunner {
	@Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Application newApplication = super.newApplication(cl, "com.quesity.test.mocks.QuesityApplicationMock", context);
		return newApplication;
	}

}
