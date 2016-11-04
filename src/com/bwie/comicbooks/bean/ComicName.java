package com.bwie.comicbooks.bean;

import java.util.List;

public class ComicName {

	public int error_code;
	public String reason;
	public ComicNameResult result;
	public class ComicNameResult{
		public int total;
		public String comicName;
		public List<ChapterList>chapterList;
		public int limit;
	}
	public class ChapterList{
		public String name;
		public int id;
		@Override
		public String toString() {
			return "      " + name  ;
		}
		
	}
}
