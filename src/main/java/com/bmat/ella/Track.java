package com.bmat.ella;

import java.util.ArrayList;

import org.json.simple.JSONArray;

public class Track extends BaseObject{
	private String title;
	private Artist artist;
	private Album album;
	private String audio;
	private String method;
	private ArrayList<String> images;
    private String mbid;
    private ArrayList<String[]> links;
    private String label;
    private String tags;
    private String tracks;
	
	
	public Track(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
		this.method = "/release/" + this.id + ".json";
		this.metadataLinks = new String[]{"spotify_release_url","amazon_release_url","itms_release_url","rhapsody_release_url","emusic_release_url","limewire_release_url","trackitdown_release_url","juno_release_url","rateyourmusic_release_url","metacritic_release_url","pitchfork_release_url","bbc_co_uk_release_url","rollingstone_release_url","cloudspeakers_url"};
		this.metadata = "release,name,artist_service_id,release_small_image,release_label,musicbrainz_release_id";
		this.metadata += Util.joinArray(this.metadataLinks, ",");
	}

	
	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}
	
	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Album getAlbum(){
		return album;
	}
	
	public void setAlbum(Album album){
		this.album = album;
	}
	public String getAudio() {
		return audio;
	}


	public void setAudio(String audio) {
		this.audio = audio;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public ArrayList<String> getImages() {
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
	

	public String getMbid() {
		return mbid;
	}


	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public ArrayList<String[]> getLinks() {
    	if(this.links == null){
    		this.links = new ArrayList<String[]>();
    		try{
    			//TO-DO checkout pyella
    		}
    		catch(Exception e){
    			return this.links;
    		}
    	}
		return links;
	}

	public void setLink(String service, String link) {
		if(this.links == null)
			this.links = new ArrayList<String[]>();
		this.links.add(new String[]{service, link});
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getTags() {
		return tags;
	}


	public void setTags(String tags) {
		this.tags = tags;
	}


	public String getTracks() {
		return tracks;
	}


	public void setTracks(String tracks) {
		this.tracks = tracks;
	}
}
