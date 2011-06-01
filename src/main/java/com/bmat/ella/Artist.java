package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Artist extends BaseObject{
	private String name;
	private String location;
	private Double lat;
	private Double lng;
	private ArrayList<HashMap<String, Double>> latlng;
	private Double popularity;
	private String[] decades;
	private ArrayList<Object[]> similarArtists;
	private ArrayList<Track> tracks;
	
	public Artist(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
		this.method = "/artists/" + this.id + ".json";
		
		this.metadataLinks = new String[]{"official_homepage_artist_url","wikipedia_artist_url","lastfm_artist_url","myspace_artist_url","spotify_artist_url","itms_artist_url","discogs_artist_url"};
		this.metadata = "artist,name,artist_popularity,artist_location,recommendable,artist_decades1,artist_decades2,artist_latlng,musicbrainz_artist_id,";
		this.metadata += Util.joinArray(this.metadataLinks, ",");
	}

	public String getName() {
		if(name == null)
			this.name = this.getFieldValue("name");
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		if(this.location == null)
			this.location = this.getFieldValue("artist_location");
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ArrayList<HashMap<String, Double>> getLatlng() {
		if(this.latlng == null){
			try{
				JSONArray latlngValues = this.getFieldValues("artist_latlng");
				if(latlngValues != null){
					this.setLatlng(latlngValues);
				}
			}
			catch(ClassCastException e){
				String latlngValues = this.getFieldValue("artist_latlng");
				if(latlngValues != null){
					this.setLatlng(latlngValues);
				}
			}
		}
		return latlng;
	}

	public void setLatlng(String latlng) {
		if(this.latlng == null)
			this.latlng = new ArrayList<HashMap<String, Double>>();
		
		if(!latlng.equals("")){
			String[] lat_lng = latlng.split(",");
			HashMap<String, Double> map = new HashMap<String, Double>();
			map.put("lat", new Double(lat_lng[0]));
			map.put("lng", new Double(lat_lng[1]));
			this.latlng.add(map);
		}
	}
	
	public void setLatlng(JSONArray latlngs) {
		for(int i = 0; i<latlngs.size(); i++)
			this.setLatlng(latlngs.get(i).toString());
	}
	
	public String getMbid(){
		if(this.mbid == null)
			this.setMbid(this.getFieldValue("musicbrainz_artist_id"));
		return this.mbid;
	}
	
	public double getPopularity(){
		if(this.popularity == null){
			String meta = this.getFieldValue("artist_popularity");
			if(meta != null)
				this.popularity = new Double(meta);
			else
				this.popularity = new Double(0);
		}
		return this.popularity;
	}
	
	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	
	public String[] getDecades(){
		if(this.decades == null)
			this.setDecades(this.getFieldValue("artist_decades1"), this.getFieldValue("artist_decades2"));
		return this.decades;
	}
	
	public void setDecades(String decade1, String decade2){
		if(this.decades == null)
			this.decades = new String[2];
		this.decades[0] = decade1 == null? "" : decade1;
		this.decades[1] = decade2 == null? "" : decade2;
	}
	
	public ArrayList<Track> getTracks() throws ServiceException, IOException{
		if(this.tracks != null)
			return this.tracks;
		
		this.tracks = new ArrayList<Track>();
		String mtd = "/artists/" + this.id + "/tracks.json";
		String trackMetadata = "track,artist_service_id,artist,release_service_id,release,location,year,genre,track_popularity,track_small_image,recommendable,musicbrainz_track_id,spotify_track_uri,";
		String[] trackMetadataLinks = new String[]{"spotify_track_url", "grooveshark_track_url", "amazon_track_url","itms_track_url","hypem_track_url","musicbrainz_track_url"};
		trackMetadata += Util.joinArray(trackMetadataLinks, ",");
		
		HashMap<String, String> fetchMetadata = new HashMap<String, String>();
		fetchMetadata.put("fetch_metadata", trackMetadata);
		JSONObject response = (JSONObject) this.request(mtd, fetchMetadata);
		JSONArray results = (JSONArray) response.get("results");
		for(Object json : results){
			JSONObject jsonArtist = (JSONObject) json;
			JSONObject jsonEntity = (JSONObject) jsonArtist.get("entity");
			JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
			String trackId = (String) jsonEntity.get("id");
			if(trackId == null || trackId.equals(""))
				continue;
			Track track = new Track(this.request.getEllaConnection(), trackId, this.collection);
			
			track.setTitle((String)jsonMetadata.get("track"));
            track.setAudio((String)jsonMetadata.get("location"));
            track.setArtist(this);
            track.setArtistName(this.getName());
            track.setArtistId(this.getId());
            track.setMbid((String)jsonMetadata.get("musicbrainz_track_id"));
            
            Object tpop = jsonMetadata.get("track_popularity");
			double trackPopularity = tpop != null && !tpop.toString().equals("") ? new Double(tpop.toString()) : 0.0;
			track.setPopularity(trackPopularity);
			
			Object recommend = jsonMetadata.get("recommendable");
			track.setRecommend(recommend);
			
			for(String link : trackMetadataLinks){
				Object linkObject = jsonMetadata.get(link);
				if(linkObject instanceof String)
					track.setLinks(link, (String)jsonMetadata.get(link));
				else if(linkObject !=null)
					track.setLinks(link, (JSONArray) jsonMetadata.get(link));
			}
			
			Object trackSmallImages = jsonMetadata.get("track_small_image");
			if(trackSmallImages instanceof String)
				track.setImages((String)trackSmallImages);
			else if(trackSmallImages != null)
				track.setImages((JSONArray)trackSmallImages);
            
            Object albumId = jsonMetadata.get("release_service_id");
			if(albumId != null){
				Album album = new Album(this.request.getEllaConnection(), (String) albumId, this.collection);
				album.setTitle((String)jsonMetadata.get("release"));
				album.setArtist(this);
				
				track.setAlbum(album);
				track.setAlbumTitle(album.getTitle());
				track.setAlbumId(album.getId());
			}
            this.tracks.add(track);
		}
		return this.tracks;
	}
	
	public ArrayList<Object[]> getSimilarArtists() throws ServiceException, IOException {
		if(!this.isRecommend())
			return null;
		else if(this.similarArtists != null)
			return this.similarArtists;
		
		this.similarArtists = new ArrayList<Object[]>();
		String mtd = "/artists/" + this.id + "/similar/artists.json";
		HashMap<String, String> fetchMetadata = new HashMap<String, String>();
		fetchMetadata.put("fetch_metadata", this.metadata);
		JSONObject response = (JSONObject)this.request(mtd, fetchMetadata);
		JSONArray results = (JSONArray) response.get("results");
		for(Object json : results){
			JSONObject jsonArtist = (JSONObject) json;
			JSONObject jsonEntity = (JSONObject) jsonArtist.get("entity");
			JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
			String artistId = (String) jsonEntity.get("id");
			if(artistId == null || artistId.trim().equals(""))
				continue;
			Artist artist = new Artist(this.request.getEllaConnection(), artistId, this.collection);
			artist.setName((String)jsonMetadata.get("name"));
			
			Object apop = jsonMetadata.get("artist_popularity");
			double artistPopularity = apop != null && !apop.toString().equals("") ? new Double(apop.toString()) : 0.0;
			artist.setPopularity(artistPopularity);
			
			String artistLocation = (String) jsonMetadata.get("artist_location");
			artistLocation = artistLocation != null ? artistLocation : "";
			
			Object recommend = jsonMetadata.get("recommendable");
			artist.setRecommend(recommend);
			
			Object score = jsonArtist.get("score");
			if(score == null || score.equals(""))
				score = "0";
			this.similarArtists.add(new Object[]{artist, new Double(score.toString())});
		}
		
		return this.similarArtists;
	}

	public Double getLat() {
		if(this.lat == null){
			String latValue = this.getFieldValue("artist_lat");
			this.lat = latValue == null ? null : new Double(latValue);
		}
		return this.lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		if(this.lng == null){
			String lngValue = this.getFieldValue("artist_lng");
			this.lng = lngValue == null ? null : new Double(lngValue);
		}
		return this.lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
}
