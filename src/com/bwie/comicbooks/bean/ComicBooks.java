package com.bwie.comicbooks.bean;

import java.util.List;

public class ComicBooks {

	public int error_code;
	public String reason;
	public ComicResult result;
	public class ComicResult{
		public int total;
		public int limit;
	    public List<BookList>bookList;
	}
	public class BookList{
		public String name;
		public String type;
		public String area;
		public String des;
		public String finish;
		public String lastUpdate;
		public String coverImg;
	}
}
