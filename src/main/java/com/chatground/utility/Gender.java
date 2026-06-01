package com.chatground.utility;

public enum Gender {
	MALE("男"),
	FEMALE("女"),
	X("X");
	
	private final String chineseName;
	
	Gender(String chineseName) {
		this.chineseName = chineseName;
	}
	
	//Getter方法
	public String getChineseName() {
		return chineseName;
	}
}
