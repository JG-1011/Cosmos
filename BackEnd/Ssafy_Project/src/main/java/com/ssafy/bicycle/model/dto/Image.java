package com.ssafy.bicycle.model.dto;

public class Image {
	private int image_type;
	private int image_boardNum;
	private String image_oriName;
	private String image_saveName;
	
	public Image() {
		// TODO Auto-generated constructor stub
	}

	public Image(int image_type, int image_boardNum, String image_oriName, String image_saveName) {
		super();
		this.image_type = image_type;
		this.image_boardNum = image_boardNum;
		this.image_oriName = image_oriName;
		this.image_saveName = image_saveName;
	}

	public int getImage_type() {
		return image_type;
	}

	public void setImage_type(int image_type) {
		this.image_type = image_type;
	}

	public int getImage_boardNum() {
		return image_boardNum;
	}

	public void setImage_boardNum(int image_boardNum) {
		this.image_boardNum = image_boardNum;
	}

	public String getImage_oriName() {
		return image_oriName;
	}

	public void setImage_oriName(String image_oriName) {
		this.image_oriName = image_oriName;
	}

	public String getImage_saveName() {
		return image_saveName;
	}

	public void setImage_saveName(String image_saveName) {
		this.image_saveName = image_saveName;
	}
	
}
