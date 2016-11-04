package com.bwie.comicbooks;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bwie.comicbooks.bean.ComicName;
import com.bwie.comicbooks.bean.ComicName.ChapterList;
import com.bwie.comicbooks.utils.ComicsUtil;
import com.google.gson.Gson;

public class PagerActivity extends Activity {

	private ListView lvpager;
	private Context context;
	private List<ChapterList> chapterLists;
	private String comicName1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager_layout);
		context = this;
		lvpager = (ListView) findViewById(R.id.lvpager);
		new Thread(){
			
			

			public void run() {
				Intent intent = getIntent();
				String name = intent.getStringExtra("comicName").trim();
				comicName1 = URLEncoder.encode(name);
				HttpClient client = new DefaultHttpClient();
				String str = "http://japi.juhe.cn/comic/chapter?comicName="+comicName1+"&key=b2bffd5346d3f1af4763256d5d819568";
				//请求方式
				HttpGet get = new HttpGet(str);
				
				try {
					HttpResponse response = client.execute(get);//响应
					
					StatusLine statusLine = response.getStatusLine();//响应行
					
					int code = statusLine.getStatusCode();
					
					if(code == 200){
						HttpEntity entity = response.getEntity();//获得实体
						
						InputStream inputStream = entity.getContent();//获得内容和流对象
						
						final String json = ComicsUtil.comicToString(inputStream);
						runOnUiThread(new Runnable() {
						

							public void run() {
								ComicName comicName = new Gson().fromJson(json, ComicName.class);
								if(comicName.result == null){
									Toast.makeText(context, "收费漫画 支付后查看  ...", 0).show();
								}else{
						
								chapterLists = comicName.result.chapterList;
								lvpager.setAdapter(new ArrayAdapter<ChapterList>(context, android.R.layout.simple_list_item_1, chapterLists));
							}
							}
						});
						}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			};
		}.start();
		lvpager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				   String name = comicName1;
				   int myId = chapterLists.get(position).id;
				Intent intent = new Intent(context, PhotoActivity.class);
				intent.putExtra("comicName", name);
				intent.putExtra("myId", myId);
				startActivity(intent);
			}
		});
	}
}
