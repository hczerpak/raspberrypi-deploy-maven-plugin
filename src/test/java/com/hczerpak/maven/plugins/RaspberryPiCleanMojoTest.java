package com.hczerpak.maven.plugins;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    }

    @Test
    public void testExecute() throws Exception {
        Assert.assertNotNull(mojo);



        mojo.execute();
    }
}