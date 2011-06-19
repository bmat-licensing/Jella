
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
/**
 * Java Class JellaTest.
 * Search for Artists.
 * @author Harrington Joseph (Harph)
 * */
public class JellaTest {
    /**
     * Jella instance.
     * */
    private Jella jella;

    /**
     * Loads the WS configuration file and initialize the Jella instance.
     * @throws IOException When it couldn't load the configuration file.
     * */
    @Before public void setUp() throws IOException {
        Properties params = new Properties();
        InputStream inputStream = this.getClass()
        .getResourceAsStream("/ws.conf");
        assertNotNull("could not load test configuration ('ws.conf')",
                inputStream);
        params.load(inputStream);
        jella = new Jella(params.getProperty("host"), params
                .getProperty("user"), params.getProperty("password"));
    }
    
    /**
     * tearDown method.
     * */
    @After public void tearDown() {
        // nothing to tear down
    }
    
    
    /**
     * Test Jella search track functions.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void testSearchTracks() throws ServiceException, IOException{
        // TEST 1 - search
        String query1 = "Beautiful day";
        String collection1 = "bmat";
        String artist1 = "u2";
        String album1 = "leave behind";
        TrackSearch trackSearch1 = jella.searchTracks(query1, collection1, false, null, new String[]{"artist:" + artist1, "release:" + album1});
        assertTrue(trackSearch1.getTotalResultCount() > 0);
        ArrayList<Track> tracksResult1 = trackSearch1.getNextPage();
        assertTrue(tracksResult1.size() > 0);
        for(Track track : tracksResult1) {
            assertTrue(track.getArtist().getName().toLowerCase().indexOf(artist1.toLowerCase()) != -1);
            assertTrue(track.getTitle().toLowerCase().indexOf(query1.toLowerCase()) != -1);
            assertTrue(track.getCollection().equalsIgnoreCase(collection1));
        }
        // TEST 2 - match
        String collection2 = "bmat";
        String query2 = "Marley";
        TrackSearch trackSearch2 = jella.searchTracks(query2, collection2, true, null, null);
        assertTrue(trackSearch2.getTotalResultCount() > 0);
        // TEST 3 - resolve
        String collection3 = "bmat";
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("artist", "Red Hot Chili Peppers");
        query.put("track", "Otherside");
        TrackSearch trackSearch3 = jella.searchTracks(query, collection3, true, null);
        assertTrue(trackSearch3.getTotalResultCount() > 0);
        ArrayList<Track> tracksResult3 = trackSearch3.getPage(-1);
        assertTrue(tracksResult3.size() > 0);
        for(Track track : tracksResult3) {
            assertTrue(track.getTitle().toLowerCase().indexOf(query.get("track").toString().toLowerCase()) != -1);
            assertTrue(track.getArtist().getName().toLowerCase().indexOf(query.get("artist").toString().toLowerCase()) != -1);
            assertTrue(track.getCollection().equalsIgnoreCase(collection3));
        }		
    }

    /**
     * Test Jella Track Object and Metadata functions.
     * */
    @Test public void  testTrack() {
        Track track = new Track(jella.getEllaConnection(), "9dd6f275-81f4-4b4d-9277-41dfb1dad7a7", "bmat");
        assertTrue(track.getTitle().equalsIgnoreCase("californication"));
        assertTrue(track.getArtist().getName().equalsIgnoreCase("Red Hot Chili Peppers"));
        assertTrue(track.getArtistName().equalsIgnoreCase(track.getArtist().getName()));
        assertTrue(track.getAlbumTitle().equalsIgnoreCase("Californication"));
        assertNotNull(track.getLinks());

    }

    /**
     * Test Jella search similar tracks from a Track instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void  testSimilarTracks() throws ServiceException, IOException {
        Track track = new Track(jella.getEllaConnection(), "9dd6f275-81f4-4b4d-9277-41dfb1dad7a7", "bmat");
        ArrayList<Track> similars = track.getSimilarTracks();
        assertTrue(similars.size() > 0);
        for(Track sim : similars) {			
            assertTrue(!sim.getId().equals(""));
            assertNotNull(sim.getTitle());
        }

        HashMap<String, HashMap<String, String>> seeds = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> artist = new HashMap<String, String>();
        artist.put("collection", "bmat");
        artist.put("entity", "artist");
        seeds.put("6b90be58-2540-41d8-8bce-17e7e19c2a97", artist);
        assertTrue(track.getSimilarTracks(30, new String[]{"speed:slow", "mood:blue"}, "bmat", seeds, null, null).size() > 0);
    }

    /**
     * Test Jella search artist functions.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void testSearchArtists() throws Exception {
        // TEST 1 - search
        String query1 = "Red Hot Chili Peppers";
        String collection1 = "bmat";
        ArtistSearch artistSearch1 = jella.searchArtists(query1, collection1, false, null);
        assertTrue(artistSearch1.getTotalResultCount() > 0);
        ArrayList<Artist> artistsResult1 = artistSearch1.getNextPage();
        assertTrue(artistsResult1.size() > 0);
        for (Artist artist : artistsResult1) {
            assertNotNull(artist.getName());
            assertNotNull(artist.getId());
        }

        // TEST 2 - match
        String query2 = "marley";
        String collection2 = "bmat";
        ArtistSearch artistSearch2 = jella.searchArtists(query2, collection2, true, null);
        assertTrue(artistSearch2.getTotalResultCount() > 0);
    }

    /**
     * Test Jella Artist Object and Metadata functions.
     * */
    @Test public void  testArtist() {
        Artist artist = new Artist(jella.getEllaConnection(), "0b7c17b1-02e0-49fa-84b4-af3c08362a19", "bmat");
        assertTrue(artist.getName().equalsIgnoreCase("The White Stripes"));
        assertTrue(artist.getLocation().equalsIgnoreCase("Detroit, Michigan, US"));
        String[] decades = artist.getDecades();
        assertTrue(decades[0].equals("1997"));
        assertTrue(decades[1].equals("2011"));
        assertNotNull(artist.getLatlng());
        assertNotNull(artist.getLinks());
    }
    
    /**
     * Test Jella search similar artists from an Artist instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void  testSimilarArtists() throws ServiceException, IOException {
        Artist artist = new Artist(jella.getEllaConnection(), "0b7c17b1-02e0-49fa-84b4-af3c08362a19", "bmat");
        ArrayList<Object[]> similars = artist.getSimilarArtists();
        assertTrue(similars.size() > 0);
        for(Object[] obj : similars) {
            Artist art = (Artist) obj[0];
            assertTrue(!art.getId().equals(""));
            assertNotNull(art.getName());
        }
    }

    /**
     * Test Jella search tracks from an Artist instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void  testArtistTracks() throws ServiceException, IOException {
        Artist artist = new Artist(jella.getEllaConnection(), "0b7c17b1-02e0-49fa-84b4-af3c08362a19", "bmat");
        ArrayList<Track> tracks = artist.getTracks();
        assertTrue(tracks.size() > 0);
        for (Track track:tracks) {
            assertNotNull(track.getId());
            assertNotNull(track.getTitle());
            assertTrue(track.getArtistId().equals(artist.getId()));
            assertTrue(track.getArtistName().equals(artist.getName()));
            assertTrue(track.getAlbumId().equals(track.getAlbum().getId()));
            assertTrue(track.getAlbumTitle().equals(track.getAlbum().getTitle()));
            assertNotNull(track.getLinks());
        }
    }

    /**
     * Test Jella search tags from an Artist instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void  testArtistTags() throws ServiceException, IOException {
        Artist artist = new Artist(jella.getEllaConnection(), "0b7c17b1-02e0-49fa-84b4-af3c08362a19", "bmat");
        ArrayList<Object[]> tags1 = artist.getTags();
        assertTrue(tags1.size() > 0);
        for (Object[] tag : tags1) {
            assertNotNull(tag[1]);
        }
        double tagWeight = 0.50;
        String tagType = "style";
        int limit = 20;
        ArrayList<Object[]> tags2 = artist.getTags(tagType, tagWeight, limit);
        assertTrue(tags2.size() > 0 && tags2.size() < limit);
        for (Object[] tag : tags2) {
            assertNotNull(((Double) tag[0]) >= tagWeight);
        }
    }
    
    /**
     * Test Jella Album Object and Metadata functions.
     * */
    @Test public void  testAlbum() {
        Album album = new Album(jella.getEllaConnection(), "2eb3b186-121f-4eaf-9ad6-f0f7730b57eb", "bmat");
        assertNotNull(album.getId());
        assertNotNull(album.getMbid());
        assertTrue(album.getTitle().equalsIgnoreCase("Conquest"));
        assertTrue(album.getArtist().getName().equalsIgnoreCase("the white stripes"));
        assertNotNull(album.getLinks());
        assertNotNull(album.getLabels());
    }

    /**
     * Test Jella search tracks from an Album instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void  testAlbumTrack() throws ServiceException, IOException {
        Album album = new Album(jella.getEllaConnection(), "2eb3b186-121f-4eaf-9ad6-f0f7730b57eb", "bmat");
        ArrayList<Track> tracks = album.getTracks();
        assertTrue(tracks.size() > 0);
        for (Track track : tracks) {
            assertNotNull(track.getId());
            assertNotNull(track.getTitle());
            assertTrue(track.getArtistId().equals(album.getArtist().getId()));
            assertTrue(track.getArtistName().equals(album.getArtist().getName()));
            assertTrue(track.getAlbumId().equals(album.getId()));
            assertTrue(track.getAlbumTitle().equals(album.getTitle()));
            assertNotNull(track.getLinks());
        }
    }

    /**
     * Test Jella search tags functions.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void testSearchTags() throws ServiceException, IOException {
        // TEST 1 - search
        String collection1 = "tags";
        TagSearch tagSearch1 = jella.searchTags("happy", collection1, false);
        assertTrue(tagSearch1.getTotalResultCount() > 0);
        for(Tag tag : tagSearch1.getNextPage()) {
            assertNotNull(tag.getName());
            assertTrue(tag.collection.equalsIgnoreCase(collection1));
        }

        // TEST 2 - match
        String collection2 = "tags";
        TagSearch tagSearch2 = jella.searchTags("happy", collection2, true);
        assertTrue(tagSearch2.getTotalResultCount() > 0);
    }

    /**
     * Test Jella search similar tags from a Tag instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void testSimilarTags() throws ServiceException, IOException {
        Tag tag = new Tag(jella.getEllaConnection(), "happy", "tags");
        ArrayList<Object[]> similars = tag.getSimilarTags();
        assertTrue(similars.size() > 0);
        for (Object[] similar : similars) {
            assertNotNull(similar[1]);
        }
    }
    
    /**
     * Test Jella search similar artists from an Artist instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void testSimilarArtistsByTags() throws ServiceException, IOException {
        Tag tag = new Tag(jella.getEllaConnection(), "happy", "tags");
        ArrayList<Artist> artists = tag.getSimilarArtists();
        assertTrue(artists.size() > 0);
        for (Artist artist : artists) {
            assertNotNull(artist.getName());
            assertTrue(artist.getCollection().equals("bmat"));
        }
    }
    
    /**
     * Test Jella search similar tracks from a Tag instance.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @Test public void testSimilarTracksByTags() throws ServiceException, IOException {
        Tag tag = new Tag(jella.getEllaConnection(), "happy", "tags");
        String collectionSim = "bmat";
        ArrayList<Track> tracks1 = tag.getSimilarTracks(20, null, collectionSim, null, false);
        assertTrue(tracks1.size() > 0);
        for (Track track : tracks1) {
            assertNotNull(track.getTitle());
            assertTrue(track.getCollection().equals(collectionSim));
        }
        String artistId = "6b90be58-2540-41d8-8bce-17e7e19c2a97";
        HashMap<String, HashMap<String, String>> seeds = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> artist = new HashMap<String, String>();
        artist.put("collection", "bmat");
        artist.put("entity", "artist");
        seeds.put(artistId, artist);
        ArrayList<Track> tracks2 = tag.getSimilarTracks(1, null, collectionSim, seeds, false);
        assertTrue(tracks2.size() == 1);
        if(tracks2.size() > 1) {
            assertTrue(tracks2.get(0).getArtistId().equals(artistId));
        }
        
    }
}
