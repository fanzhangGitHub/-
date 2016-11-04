package com.bwie.comicbooks.bean;

import java.util.List;

public class MyShow {

	public int error_code;
	public String reason;
	public PhotoResult result;
	public class PhotoResult{
		public String comicName;
		public int chapterId;
		public List<ImageList>imageList;
		
	}
	public class ImageList{
		public String imageUrl;
		public int id;
	}
}
