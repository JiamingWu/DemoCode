package sax;

import java.util.ArrayList;
import java.util.List;

public class CatalogVO {

	private String title;
	private String desc;
	private String icon;
	private String playlist;
	private List<CatalogVO> subs = new ArrayList<CatalogVO>();
	
	public void addSub(CatalogVO vo) {
		subs.add(vo);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPlaylist() {
		return playlist;
	}
	public void setPlaylist(String playlist) {
		this.playlist = playlist;
	}
	public List<CatalogVO> getSubs() {
		return subs;
	}
	public void setSubs(List<CatalogVO> subs) {
		this.subs = subs;
	}
}
