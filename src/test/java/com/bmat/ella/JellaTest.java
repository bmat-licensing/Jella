
package com.bmat.ella;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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
     * Test Jella Util class.
     * */
    @Test public void  testUtils() {
        String text = "this that these those";
        String[] elements = text.split(" ");
        assertTrue(Util.joinArray(elements, " ").equals(text));
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
            assertNotNull(track.getMetadataLinks());
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
            assertNotNull(track.getMetadataLinks());
        }
        // TEST 4 - search
        String artist4 = "faith";
        TrackSearch trackSearch4 = jella.searchTracks(null, "bmat", false, null, new String[]{"artist:" + artist4, "mood:happy"});
        assertTrue(trackSearch4.getTotalResultCount() > 0);
        for (Track track : trackSearch4.getNextPage()) {
            assertTrue(track.getArtistName().toLowerCase().indexOf(artist4) != -1);
            assertNotNull(track.getMetadataLinks());
        }
        
    }

    /**
     * Test Jella Track Object and Metadata functions.
     * */
    @Test public void  testTrack() {
        Track track = new Track(jella.getEllaConnection(), "9dd6f275-81f4-4b4d-9277-41dfb1dad7a7", "bmat");
        assertTrue(track.getAlbumId().equals("d8c97ca3-3faa-4423-8c0f-61e2bb849066"));
        assertTrue(track.getAlbumTitle().equals("Californication"));
        assertNotNull(track.getAlbum());
        assertTrue(track.getArtistId().equals("85d09256-df5c-4af8-acfa-858df367b0a9"));
        assertTrue(track.getArtistName().equals("Red Hot Chili Peppers"));
        assertNotNull(track.getArtist());
        assertTrue(track.getAudio().equals("http://audio.bmat.com/audio/8/r/e/red_hot_chili_peppers/californication/californication.mp3"));
        assertTrue(track.getCollection().equals("bmat"));
        assertTrue(track.getFullTitle().equals("Red Hot Chili Peppers - Californication"));
        assertTrue(track.getId().equals("9dd6f275-81f4-4b4d-9277-41dfb1dad7a7"));
        assertNotNull(track.getImages());
        assertNotNull(track.getJson());
        assertNotNull(track.getLinks());
        assertTrue(track.getMbid().equals("084a24a9-b289-4584-9fb5-1ca0f7500eb3"));
        assertNotNull(track.getMetadata());
        assertNotNull(track.getMetadataLinks());
        assertTrue(track.getMethod().equals("/tracks/9dd6f275-81f4-4b4d-9277-41dfb1dad7a7.json"));
        assertTrue(track.getPopularity() > 0);
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

        Track track2 = new Track(jella.getEllaConnection(), "6748e70b-9c61-4cc3-b2fb-9aa32321a050", "bmat");
        ArrayList<Track> similars2 = track2.getSimilarTracks(30, new String[]{"track_popularity:[5.0 TO 10.0]", "artist_iso_country:ES"}, "bmat", null, null, null);
        assertTrue(similars2.size() > 0);
        for (Track sim : similars2) {
            double popularity = sim.getPopularity();
            assertTrue(popularity >= 5.0 && popularity <= 10.0);
        }

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
        assertTrue(artistSearch2.getPage(-1).size() > 0);
        
        // TEST 3 - resolve
        String artist3 = "Read Hot Chili Peppers";
        ArtistSearch artistSearch3 = new ArtistSearch(jella.getEllaConnection(), "resolve", artist3, "bmat", false, 0.4);
        assertTrue(artistSearch3.getTotalResultCount() > 0);
        assertTrue(artistSearch3.getPage(1).size() > 0);
    }

    /**
     * Test Jella Artist Object and Metadata functions.
     * */
    @Test public void  testArtist() {
        Artist artist = new Artist(jella.getEllaConnection(), "0b7c17b1-02e0-49fa-84b4-af3c08362a19", "bmat");
        assertTrue(artist.getCollection().equals("bmat"));
        assertTrue(artist.getDecades()[0].equals("1997"));
        assertTrue(artist.getDecades()[1].equals("2011"));
        assertTrue(artist.getId().equals("0b7c17b1-02e0-49fa-84b4-af3c08362a19"));
        assertTrue(artist.getLat() == 42.331427);
        assertNotNull(artist.getLatlng());
        assertNotNull(artist.getLinks());
        assertTrue(artist.getLng() == -83.045754);
        assertTrue(artist.getLocation().equalsIgnoreCase("Detroit, Michigan, US"));
        assertTrue(artist.getMbid().equals("11ae9fbb-f3d7-4a47-936f-4c0a04d3b3b5"));
        assertNotNull(artist.getMetadata());
        assertNotNull(artist.getMetadataLinks());
        assertTrue(artist.getMethod().equals("/artists/0b7c17b1-02e0-49fa-84b4-af3c08362a19.json"));
        assertTrue(artist.getName().equals("The White Stripes"));
        assertTrue(artist.getPopularity() > 0);
        assertNotNull(artist.getJson());
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
        assertNotNull(album.getArtist());
        assertTrue(album.getArtist().getName().equals("The White Stripes"));
        assertTrue(album.getCollection().equals("bmat"));
        assertTrue(album.getId().equals("2eb3b186-121f-4eaf-9ad6-f0f7730b57eb"));
        assertNotNull(album.getImages());
        assertTrue(album.getImages().size() > 0);
        assertNotNull(album.getJson());
        assertNotNull(album.getLabels());
        assertTrue(album.getLabels().size() > 0);
        assertNotNull(album.getLinks());
        assertTrue(album.getMbid().equals("4ca09b82-fbf2-4316-a45b-c11c60b46195"));
        assertNotNull(album.getMetadata());
        assertNotNull(album.getMetadataLinks());
        assertTrue(album.getMethod().equals("/releases/2eb3b186-121f-4eaf-9ad6-f0f7730b57eb.json"));
        assertTrue(album.getTitle().equals("Conquest"));
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
        assertTrue(tagSearch2.getPage(-1).size() > 0);
        assertTrue(tagSearch2.getPage(1).size() > 0);
    }

    /**
     * Test Jella Tag Object and Metadata functions.
     * */
    @Test public void  testTag() {
        Tag tag = new Tag(jella.getEllaConnection(), "happy", "tags");
        assertTrue(tag.getCollection().equals("tags"));
        assertTrue(tag.getId().equals("happy"));
        assertTrue(tag.getName().equals("happy"));
        assertNotNull(tag.getMetadata());
        assertNull(tag.getLinks());
        assertNull(tag.getMetadataLinks());
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

    /**
     * Test Jella compareTo function for Albums.
     * */
    @Test public void testCompareToAlbum() {
        String humanId = "eea44fb1-338b-4c51-92c8-888f8e6b6ae0";
        String thinkTankId = "4499bbd8-d847-4dc3-8c7a-19f4a20dbb96";
        String getBornId = "ba67761d-17fc-4fe3-b3cc-6ef662243789";
        String oakenfoldAnthemsId = "d2654764-aaa5-4678-aedb-5b2ad8925047";
        Album human = new Album(jella.getEllaConnection(), humanId, "bmat");
        Album thinkTank = new Album(jella.getEllaConnection(), thinkTankId, "bmat");
        Album getBorn = new Album(jella.getEllaConnection(), getBornId, "bmat");
        Album oakenfoldAnthems = new Album(jella.getEllaConnection(), oakenfoldAnthemsId, "bmat");
        Album[] artistAlbum = new Album[]{human, thinkTank, getBorn, oakenfoldAnthems};
        Arrays.sort(artistAlbum);
        assertTrue(artistAlbum[0].getId().equals(thinkTankId));
        assertTrue(artistAlbum[1].getId().equals(getBornId));
        assertTrue(artistAlbum[2].getId().equals(oakenfoldAnthemsId));
        assertTrue(artistAlbum[3].getId().equals(humanId));
    }

    /**
     * Test Jella equals function for Albums.
     * */
    @Test public void testEqualsAlbum() {
        String humanId = "eea44fb1-338b-4c51-92c8-888f8e6b6ae0";
        String thinkTankId = "4499bbd8-d847-4dc3-8c7a-19f4a20dbb96";
        Album album1 = new Album(jella.getEllaConnection(), humanId, "bmat");
        Album album2 = new Album(jella.getEllaConnection(), humanId, "bmat");
        Album album3 = new Album(jella.getEllaConnection(), thinkTankId, "bmat");
        assertTrue(album1.equals(album2));
        assertFalse(album1.equals(album3));
        assertFalse(album1.equals(null));
        assertFalse(album1.equals("test"));
    }

    /**
     * Test Jella compareTo function for Artists.
     * */
    @Test public void testCompareToArtist() {
        String u2Id = "75e4aefb-f594-4ed4-b8ff-9a83fbd54dd3";
        String jetId = "bd083d22-e5a4-4a56-a845-888e02c0f269";
        String blurId = "c47a47c7-ac47-4905-8c8a-9367cecba0b1";
        String theKillersId = "46fa0478-f5fc-4ff5-824d-686ef8e1af28";
        Artist u2 = new Artist(jella.getEllaConnection(), u2Id, "bmat");
        Artist jet = new Artist(jella.getEllaConnection(), jetId, "bmat");
        Artist blur = new Artist(jella.getEllaConnection(), blurId, "bmat");
        Artist theKillers = new Artist(jella.getEllaConnection(), theKillersId, "bmat");
        Artist[] artistArray = new Artist[]{u2, jet, blur, theKillers};
        Arrays.sort(artistArray);
        assertTrue(artistArray[0].getId().equals(theKillersId));
        assertTrue(artistArray[1].getId().equals(u2Id));
        assertTrue(artistArray[2].getId().equals(jetId));
        assertTrue(artistArray[3].getId().equals(blurId));
    }

    /**
     * Test Jella equals function for Artists.
     * */
    @Test public void testEqualsArtist() {
        String u2Id = "75e4aefb-f594-4ed4-b8ff-9a83fbd54dd3";
        String jetId = "bd083d22-e5a4-4a56-a845-888e02c0f269";
        Artist artist1 = new Artist(jella.getEllaConnection(), u2Id, "bmat");
        Artist artist2 = new Artist(jella.getEllaConnection(), u2Id, "bmat");
        Artist artist3 = new Artist(jella.getEllaConnection(), jetId, "bmat");
        assertTrue(artist1.equals(artist2));
        assertFalse(artist1.equals(artist3));
        assertFalse(artist1.equals(null));
        assertFalse(artist1.equals("test"));
    }

    /**
     * Test Jella compareTo function for Tags.
     * */
    @Test public void testCompareToTag() {
        Tag happy = new Tag(jella.getEllaConnection(), "happy", "tags");
        Tag sad = new Tag(jella.getEllaConnection(), "sad", "tags");
        Tag excited = new Tag(jella.getEllaConnection(), "excited", "tags");
        Tag motivated = new Tag(jella.getEllaConnection(), "motivated", "tags");
        Tag[] tagArray = new Tag[]{happy, sad, excited, motivated};
        Arrays.sort(tagArray);
        assertTrue(tagArray[0].getId().equals(excited.getId()));
        assertTrue(tagArray[1].getId().equals(happy.getId()));
        assertTrue(tagArray[2].getId().equals(motivated.getId()));
        assertTrue(tagArray[3].getId().equals(sad.getId()));
    }

    /**
     * Test Jella equals function for Tags.
     * */
    @Test public void testEqualsTag() {
        Tag tag1 = new Tag(jella.getEllaConnection(), "happy", "tags");
        Tag tag2 = new Tag(jella.getEllaConnection(), "happy", "tags");
        Tag tag3 = new Tag(jella.getEllaConnection(), "sad", "tags");
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
        assertFalse(tag1.equals(null));
        assertFalse(tag1.equals("test"));
    }

    /**
     * Test Jella compareTo function for Tracks.
     * */
    @Test public void testCompareToTrack() {
        String beautifulDayId = "19d4904c-f2f2-4a95-887c-7e5ddd3fb358";
        String areYouGonnaBeMyGirl = "3830e844-a9fd-43e1-8ca8-0eb5fd68ac54";
        String ambulanceId = "861cb75e-97be-44b1-b71b-1cfe4a07b831";
        String fellInLoveWithAGirlId = "8ef0c2f1-99a7-4b6b-86ba-534f0e38eb3a";
        Track track1 = new Track(jella.getEllaConnection(), beautifulDayId, "bmat");
        Track track2 = new Track(jella.getEllaConnection(), areYouGonnaBeMyGirl, "bmat");
        Track track3 = new Track(jella.getEllaConnection(), ambulanceId, "bmat");
        Track track4 = new Track(jella.getEllaConnection(), fellInLoveWithAGirlId, "bmat");
        Track[] trackArray = new Track[]{track1, track2, track3, track4};
        Arrays.sort(trackArray);
        assertTrue(trackArray[0].getId().equals(beautifulDayId));
        assertTrue(trackArray[1].getId().equals(areYouGonnaBeMyGirl));
        assertTrue(trackArray[2].getId().equals(ambulanceId));
        assertTrue(trackArray[3].getId().equals(fellInLoveWithAGirlId));
    }

    /**
     * Test Jella equals function for Tracks.
     * */
    @Test public void testEqualsTrack() {
        String beautifulDayId = "19d4904c-f2f2-4a95-887c-7e5ddd3fb358";
        String areYouGonnaBeMyGirl = "3830e844-a9fd-43e1-8ca8-0eb5fd68ac54";
        Track track1 = new Track(jella.getEllaConnection(), beautifulDayId, "bmat");
        Track track2 = new Track(jella.getEllaConnection(), beautifulDayId, "bmat");
        Track track3 = new Track(jella.getEllaConnection(), areYouGonnaBeMyGirl, "bmat");
        assertTrue(track1.equals(track2));
        assertFalse(track1.equals(track3));
        assertFalse(track1.equals(null));
        assertFalse(track1.equals("test"));
    }
    
    /**
     * Test Jella for Request.
     * @throws IOException When connection fails.
     * @throws ServiceException When a problems occurs with the WS.
     * */
    @Test public void testRequest() throws ServiceException, IOException {
        Request request = new Request(jella.getEllaConnection());
        assertNotNull(request.execute("/tracks/19d4904c-f2f2-4a95-887c-7e5ddd3fb358.json", "bmat", null));
    }
    
    /**
     * Test Jella for ServiceException.
     * @throws IOException When connection fails.
     * @throws ServiceException When a problems occurs with the WS.
     * */
    @Test (expected = ServiceException.class) 
    public void testServiceException() throws ServiceException, IOException {
        Request request = new Request(jella.getEllaConnection());
        try {
            request.execute("search.json", "bmat", null);
        } catch (ServiceException e) {
            assertTrue(e.getType().equals("ella.core.errors.NotFound"));
            assertNotNull(e.getMessage());
            assertNotNull(e.toString());
            throw e;
        }
    }
    
    /**
     * Test Jella Default attributes.
     * */
    @Test public void testDefault() {
        Jella myJella = new Jella("", "", "");
        // Sets
        boolean setCacheEnable = false;
        String setDefaultCollection = "myCollection";
        long setDefaultLimit = 200;
        int setDefaultTagLimit = 10;
        String setDefaultTagType = "myType";
        double setDefaultTagWeight = 30;
        String setJellaCacheDir = "../";
        int setResultsPerPage = 20;

        myJella.setCacheEnablet(setCacheEnable);
        myJella.setDefaultCollection(setDefaultCollection);
        myJella.setDefaultLimit(setDefaultLimit);
        myJella.setDefaultTagLimit(setDefaultTagLimit);
        myJella.setDefaultTagType(setDefaultTagType);
        myJella.setDefaultTagWeight(setDefaultTagWeight);
        myJella.setJellaCacheDir(setJellaCacheDir);
        myJella.setResultsPerPage(setResultsPerPage);

        // Asserts
        assertTrue(myJella.isCacheEnable() == setCacheEnable);
        assertTrue(myJella.getDefaultCollection().equals(setDefaultCollection));
        assertTrue(myJella.getDefaultLimit() == setDefaultLimit);
        assertTrue(myJella.getDefaultTagLimit() == setDefaultTagLimit);
        assertTrue(myJella.getDefaultTagType().equals(setDefaultTagType));
        assertTrue(myJella.getDefaultTagWeight() == setDefaultTagWeight);
        assertTrue(myJella.getJellaCacheDir().equals(setJellaCacheDir));
        assertTrue(myJella.getResultsPerPage() == setResultsPerPage);
    }
    
}
