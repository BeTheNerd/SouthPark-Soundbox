package stephane.castrec.spbox.objects;

import stephane.castrec.spbox.objects.Constants.Pers;

public class Sounds {
	private String name;
	private Pers persCode;
	private String pers;
	private Boolean isFavorite = false;
	private String path;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setPers(String pers) {
		this.pers = pers;
	}
	public String getPers() {
		return pers;
	}
	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public Boolean getIsFavorite() {
		return isFavorite;
	}
	public void setPersCode(Pers persCode) {
		this.persCode = persCode;
	}
	public Pers getPersCode() {
		return persCode;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}

}
