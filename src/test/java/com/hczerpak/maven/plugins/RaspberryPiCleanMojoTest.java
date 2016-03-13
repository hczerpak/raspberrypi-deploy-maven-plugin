package com.hczerpak.maven.plugins;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

/**
 * Created by Hubert Czerpak on 06/03/2016
 * using 11" MacBook (can't see sh*t on this screen).
 */
public class RaspberryPiCleanMojoTest {

    private RaspberryPiCleanMojo mojo;

    @Before
    public void setUp() throws Exception {
        mojo = new RaspberryPiCleanMojo();
    }

    @After
    public void tearDown() throws Exception {
        mojo = null;
    }

    @Test
    public void testExecute() throws Exception {
        Assert.assertNotNull(mojo);

        Whitebox.setInternalState(mojo, "raspberrypi", "raspberrypi");
        Whitebox.setInternalState(mojo, "username", "pi");
        Whitebox.setInternalState(mojo, "password", "raspberry");
        Whitebox.setInternalState(mojo, "jarFileName", "README.md");

        mojo.execute();
    }
}