package com.bmat.ella;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class JellaTest {

	private Jella jella;

	@Before public void setUp() throws IOException {
    Properties params = new Properties();
    InputStream inputStream = this.getClass().getResourceAsStream("/ws.conf");
    assertNotNull("could not load test configuration ('ws.conf')", inputStream);
    params.load(inputStream);
    
    jella = new Jella(params.getProperty("host"), params.getProperty("user"), params.getProperty("password"));
  }

  @After public void tearDown() {
    // nothing to tear down
  }

  @Test public void testSearchTrack() throws Exception {
	  // TEST 1 - search
	  String query1 = "Beautiful day";
	  String collection1 = "bmat";
	  String method1 = "search";
	  String artist1 = "u2";
	  String album1 = "leave behind";
	  ArrayList<Track> tracksResult1 = jella.searchTracks(method1, query1, collection1, true, null, new String[]{"artist:" + artist1, "release:" + album1}, null);
	  for(Track track : tracksResult1){
		  assertTrue(track.getArtist().getName().toLowerCase().indexOf(artist1.toLowerCase()) != -1);
		  assertTrue(track.getTitle().toLowerCase().indexOf(query1.toLowerCase()) != -1);
		  assertTrue(track.getCollection().equalsIgnoreCase(collection1));
	  }
	  
	  // TEST 2 - match
	  String collection2 = "bmat";
	  String method2 = "match";
	  String track2 = "Buffalo Soldier";
	  ArrayList<Track> tracksResult2 = jella.searchTracks(method2, "", collection2, true, null, new String[]{"track:" + track2}, new Long(0));
	  for(Track track : tracksResult2){
		  assertTrue(track.getTitle().toLowerCase().indexOf(track2.toLowerCase()) != -1);
		  assertTrue(track.getCollection().equalsIgnoreCase(collection2));
	  }
	  
	  // TEST 3 - resolve
	  String collection3 = "bmat";
	  HashMap<String, String> query = new HashMap<String, String>();
	  query.put("artist", "Red Hot Chili Peppers");
	  query.put("track", "Otherside");
	  ArrayList<Track> tracksResult3 = jella.searchTracks(query, collection3, true, null, new String[]{"track:" + track2}, null);
	  for(Track track : tracksResult3){
		  assertTrue(track.getTitle().toLowerCase().indexOf(query.get("track").toString().toLowerCase()) != -1);
		  assertTrue(track.getArtist().getName().toLowerCase().indexOf(query.get("artist").toString().toLowerCase()) != -1);
		  assertTrue(track.getCollection().equalsIgnoreCase(collection3));
	  }
  }
}
