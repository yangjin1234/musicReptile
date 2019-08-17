package com.rw;

public class Song {
	private int id;//歌曲数据编号
	private String artist;//歌手名称
	private String lyric_id;//歌词id
	private String lyric_info;//歌词信息
	private String album;//专辑
	private String name;//歌名
	private String song_id;//歌曲id
	private String url_id;//歌曲获取地址
	private String pic_id;//图片编号
	private String pic_info;//图片地址
	private String source;//资源获取来源
	private String createtime;//创建时间
	private String song_url;//歌曲地址
	
	public Song() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getLyric_id() {
		return lyric_id;
	}
	public void setLyric_id(String lyric_id) {
		this.lyric_id = lyric_id;
	}
	public String getLyric_info() {
		return lyric_info;
	}
	public void setLyric_info(String lyric_info) {
		this.lyric_info = lyric_info;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSong_id() {
		return song_id;
	}
	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}
	public String getUrl_id() {
		return url_id;
	}
	public void setUrl_id(String url_id) {
		this.url_id = url_id;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}
	public String getPic_info() {
		return pic_info;
	}
	public void setPic_info(String pic_info) {
		this.pic_info = pic_info;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getSong_url() {
		return song_url;
	}
	public void setSong_url(String song_url) {
		this.song_url = song_url;
	}
	@Override
	public String toString() {
		return "Song [id=" + id + ", artist=" + artist + ", lyric_id=" + lyric_id + ", lyric_info=" + lyric_info
				+ ", album=" + album + ", name=" + name + ", song_id=" + song_id + ", url_id=" + url_id + ", pic_id="
				+ pic_id + ", pic_info=" + pic_info + ", source=" + source + "]";
	}
}
