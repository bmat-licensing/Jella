package com.bmat.ella;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;

public class Track extends BaseObject{
	private String title;
	private Artist artist;
	private String artistId;
    private String artistName;
	private Album album;
	private String albumId;
    private String albumTitle;
	private String audio;
	private ArrayList<String> images;
	private Double popularity;
    private String tags;

    
	public Track(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
		this.method = "/tracks/" + this.id + ".json";
		this.metadataLinks = new String[]{"spotify_track_url", "grooveshark_track_url", "amazon_track_url","itms_track_url","hypem_track_url","musicbrainz_track_url"};
		this.metadata = "track,name,artist_service_id,artist,release_service_id,release,location,year,genre,track_popularity,track_small_image,recommendable,spotify_track_uri,";
		this.metadata += Util.joinArray(this.metadataLinks, ",");
	}

	
	public String getTitle(){
		if(this.title == null)
			this.title = this.getFieldValue("name");
		this.title = this.title.equals("") ? this.getFieldValue("track") : this.title;
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}
	
	public Artist getArtist() {
		if(this.artist == null){
			String metaid = this.getFieldValue("artist_service_id");
			if(!metaid.equals("")){
				this.artistId = metaid;
				this.artist = new Artist(this.request.getEllaConnection(), this.artistId, this.collection);
			}
		}
		return this.artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public String getArtistId() {
		if(this.artistId == null){
			String metaid = this.getFieldValue("artist_service_id");
			if(!metaid.equals(""))
				this.artistId = metaid;
		}
		return this.artistId;
	}
	
	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		if(this.artistName == null)
			this.artistName = this.getFieldValue("artist");
		return this.artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public Album getAlbum(){
		if(album == null){
			String metaid = this.getFieldValue("release_service_id");
			if(metaid != null){
				this.albumId = metaid;
				this.album = new Album(this.request.getEllaConnection(), this.albumId, this.collection);
			}
		}
		return this.album;
	}
	
	public void setAlbum(Album album){
		this.album = album;
	}
	
	public String getAlbumId() {
		if(this.albumId == null)
			this.albumId = this.getFieldValue("release_service_id");
		return this.albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumTitle() {
		if(this.albumTitle == null)
			this.albumTitle = this.getFieldValue("release");
		return this.albumTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public String getAudio() {
		if(this.audio == null)
			this.audio = this.getFieldValue("location");
		return audio;
	}


	public void setAudio(String audio) {
		this.audio = audio;
	}


	public ArrayList<String> getImages() {
		if(this.images == null){
			JSONArray meta = this.getFieldValues("track_small_image");
			if(meta != null)
				this.setImages(meta);
		}
		return images;
	}


	public void setImages(String image) {
		if(this.images == null)
			this.images = new ArrayList<String>();
		this.images.add(image);
	}

	public void setImages(JSONArray images) {
		for(int i = 0; i<images.size(); i++)
			this.setImages(images.get(i).toString());
	}
	
	public String getMbid(){
		if(this.mbid == null)
			this.setMbid(this.getFieldValue("musicbrainz_track_id"));
		return this.mbid;
	}
	
	public double getPopularity(){
		if(this.popularity == null){
			String meta = this.getFieldValue("track_popularity");
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	
}
