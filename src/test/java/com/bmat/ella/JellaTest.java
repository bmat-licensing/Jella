package com.bmat.ella;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class JellaTest {

  private Jella jella;

  @Before public void setUp() {
    jella = new Jella("http://ella.bmat.me/");
  }

  @After public void tearDown() {
    // nothing to tear down
  }

  @Test public void testApp() {
    String id = jella.getArtistId("the rolling stones");
    assertEquals(id, "abc123");
  }
}
