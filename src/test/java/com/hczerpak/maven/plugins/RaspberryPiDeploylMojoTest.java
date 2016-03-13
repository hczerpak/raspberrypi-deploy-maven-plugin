package com.hczerpak.maven.plugins;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Hubert Czerpak on 12/03/2016
 * using 11" MacBook (can't see sh*t on this screen).
 */
public class RaspberryPiDeploylMojoTest {

    private RaspberryPiDeployMojo mojo;

    @Before
    public void setUp() throws Exception {
        mojo = new RaspberryPiDeployMojo();
    }

    @After
    public void tearDown() throws Exception {
        mojo = null;
    }

    @Ignore
    @Test
    public void testExecute() throws Exception {
        assertNotNull(mojo);

        Whitebox.setInternalState(mojo, "raspberrypi", "raspberrypi");
        Whitebox.setInternalState(mojo, "username", "pi");
        Whitebox.setInternalState(mojo, "password", "raspberry");
        Whitebox.setInternalState(mojo, "jarFileName", "README.md");

        mojo.execute();
    }
}