package stephane.castrec.spbox.objects;

import stephane.castrec.spbox.objects.Constants.Pers;
import stephane.castrec.spbox.util.SoundsGetter;

public class Sounds {
	private String name;
	private Pers persCode;
	private String pers;
	private Boolean isFavorite = false;
	private String fileName;
	private String folder ;
	private String extension;
	
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
		if(isFavorite){
			SoundsGetter.addFavorite(this);
		} else {
			SoundsGetter.removeFavorite(this);
		}
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
	public void setfileName(String fileName) {
		this.fileName = fileName;
		extension = (String) fileName.subSequence(fileName.length()-3, fileName.length());
	}
	public String getfileName() {
		return fileName;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public String getFolder() {
		return folder;
	}
	
	public String getPathToPlay(){
		if(folder == null || folder.equals("")){
			return fileName;
		} 
		return folder+"/"+fileName;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getExtension() {
		return extension;
	}

}
