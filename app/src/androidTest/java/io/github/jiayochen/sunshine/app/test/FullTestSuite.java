package io.github.jiayochen.sunshine.app.test;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by jiayochen on 9/12/2014.
 */
public class FullTestSuite extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class).includeAllPackagesUnderHere().build();
    }

    public FullTestSuite() {
        super();
    }
}
