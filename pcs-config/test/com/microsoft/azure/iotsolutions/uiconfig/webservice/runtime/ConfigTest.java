// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.uiconfig.webservice.runtime;

import com.microsoft.azure.iotsolutions.uiconfig.services.exceptions.InvalidConfigurationException;
import helpers.UnitTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigTest {

    private Config config = null;

    @Before
    public void setUp() throws InvalidConfigurationException {
        config = new Config();
    }

    @After
    public void tearDown() {
        // something after every test
    }

    @Test(timeout = 1000)
    @Category({UnitTest.class})
    public void getPortTest() throws NoSuchFieldException, IllegalAccessException {
        assertThat(config.getPort(), is(9005));
    }

    @Test(timeout = 1000)
    @Category({UnitTest.class})
    public void getCorsWhitelistTest() throws NoSuchFieldException, IllegalAccessException {
        assertThat(config.getCorsWhitelist(), is("{ 'origins': ['*'], 'methods': ['*'], 'headers': ['*'] }"));
    }
}
