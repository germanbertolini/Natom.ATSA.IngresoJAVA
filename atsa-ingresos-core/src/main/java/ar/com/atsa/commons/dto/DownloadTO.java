package ar.com.atsa.commons.dto;

import java.io.File;

public class DownloadTO {
	private File file;
	private String fileName;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
