package com.bmat.ella;

public class Album extends BaseObject{
	private String title;
	private Artist artist;
	private String image;
	private String label;
    private String tracks;
	
	public Album(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getTracks() {
		return tracks;
	}

	public void setTracks(String tracks) {
		this.tracks = tracks;
	}
	
	public String getMbid(){
		if(this.mbid == null)
			this.setMbid(this.getFieldValue("musicbrainz_release_id"));
		return this.mbid;
	}
}
