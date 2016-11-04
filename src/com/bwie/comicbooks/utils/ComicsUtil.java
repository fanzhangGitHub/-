package com.bwie.comicbooks.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ComicsUtil {
    public static String comicToString(InputStream stream){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	int len;
    	byte[] b = new byte[1024];
    	try {
			while((len = stream.read(b))!=-1){
				bos.write(b, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return bos.toString();
    }
}
