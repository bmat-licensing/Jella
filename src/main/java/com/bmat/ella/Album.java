package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Album extends BaseObject{
	private String title;
	private Artist artist;
	private ArrayList<String> images;
	private ArrayList<String> labels;
    private ArrayList<Track> tracks;
    //private ArrayList<Tag> tags;
    
	
	public Album(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
		this.method = "/releases/" + this.id + ".json";
		
		this.metadataLinks = new String[]{"spotify_release_url","amazon_release_url","itms_release_url","rhapsody_release_url","emusic_release_url","limewire_release_url","trackitdown_release_url","juno_release_url","rateyourmusic_release_url","metacritic_release_url","pitchfork_release_url","bbc_co_uk_release_url","rollingstone_release_url","cloudspeakers_url"};
		this.metadata = "release,name,artist_service_id,release_small_image,release_label,musicbrainz_release_id";
		this.metadata += Util.joinArray(this.metadataLinks, ",");
	}

	public String getTitle() {
		if(this.title == null)
			this.title = this.getFieldValue("release");
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Artist getArtist() {
		if(this.artist == null){
			String artistId = this.getFieldValue("artist_service_id");
			if(artistId != null)
				this.artist = new Artist(this.request.getEllaConnection(), artistId, this.collection);
		}
		return this.artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	
	public ArrayList<String> getImages() {
		if(this.images == null){
			try{
				JSONArray meta = this.getFieldValues("release_small_image");
				if(meta != null){
					for(int i=0; i<meta.size(); i++)
						this.setImage(meta.toJSONString());
				}
			}
			catch(Exception e){
				String meta = this.getFieldValue("release_small_image");
				if(meta != null)
					this.setImage(meta);
			}
		}
		return images;
	}

	public void setImage(String image) {
		if(this.images == null)
			this.images = new ArrayList<String>();
		this.images.add(image);
	}
	
	public void setImages(ArrayList<String> images) {
		this.images = images;
	}
	
	public String getMbid(){
		if(this.mbid == null)
			this.setMbid(this.getFieldValue("musicbrainz_release_id"));
		return this.mbid;
	}

	public ArrayList<String> getLabels() {
		if(this.labels == null){
			try{
				JSONArray meta = this.getFieldValues("release_label");
				if(meta != null){
					for(int i=0; i<meta.size(); i++)
						this.setLabel(meta.get(i).toString());
				}
			}
			catch(Exception e){
				String meta = this.getFieldValue("release_label");
				if(meta != null)
					this.setLabel(meta);
			}
		}
		return this.labels;
	}

	public void setLabel(String label){
		if(this.labels == null)
			this.labels = new ArrayList<String>();
		this.labels.add(label);
	}
	
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	public ArrayList<Track> getTracks() throws ServiceException, IOException {
		if(this.tracks != null)
			return this.tracks;
		
		this.tracks = new ArrayList<Track>();
		String mtd = "/releases/" + this.id + "/tracks.json";
		String trackMetadata = "track,artist_service_id,artist,release_service_id,release,location,year,genre,track_popularity,track_small_image,recommendable,artist_decades1,artist_decades2,musicbrainz_track_id,";
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
            track.setAlbum(this);
            track.setAlbumId(this.getId());
            track.setAlbumTitle(this.getTitle());
            track.setMbid((String)jsonMetadata.get("musicbrainz_track_id"));
            
            Object tpop = jsonMetadata.get("track_popularity");
			double trackPopularity = tpop != null && !tpop.toString().equals("") ? new Double(tpop.toString()) : 0.0;
			track.setPopularity(trackPopularity);
			
			Object recommend = jsonMetadata.get("recommendable");
			track.setRecommend(recommend);
			
			Object trackSmallImages = jsonMetadata.get("track_small_image");
			if(trackSmallImages instanceof String)
				track.setImages((String)trackSmallImages);
			else if(trackSmallImages != null)
				track.setImages((JSONArray)trackSmallImages);
			
			if(this.artist != null){
				track.setArtist(this.artist);
				track.setArtistId(this.artist.getId());
				track.setArtistName(this.artist.getName());
			}
			
			for(String link : trackMetadataLinks){
				Object linkObject = jsonMetadata.get(link);
				if(linkObject instanceof String)
					track.setLinks(link, (String)jsonMetadata.get(link));
				else if(linkObject !=null)
					track.setLinks(link, (JSONArray) jsonMetadata.get(link));
			}
			this.tracks.add(track);
		}
		return this.tracks;
	}

	public void setTracks(ArrayList<Track> tracks) {
		this.tracks = tracks;
	}
}
