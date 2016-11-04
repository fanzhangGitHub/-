package com.bwie.comicbooks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bwie.comicbooks.bean.TypeComics;
import com.bwie.comicbooks.utils.ComicsUtil;
import com.google.gson.Gson;

public class MainActivity extends Activity {

	private ListView lvmian;
	private Context context;
	private List<String> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        lvmian = (ListView) findViewById(R.id.lvmain);
        new Thread(){
        	

			public void run() {
        		HttpClient client = new DefaultHttpClient();//HttpClient实例化
        		
        		HttpPost post = new HttpPost("http://japi.juhe.cn/comic/category");//post请求对象获得
        		
        		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        		
				HttpEntity entity;
				try {
					BasicNameValuePair nameValuePair1 = new BasicNameValuePair("null", "null");
					BasicNameValuePair nameValuePair2 = new BasicNameValuePair("key", "b2bffd5346d3f1af4763256d5d819568");
					parameters.add(nameValuePair1);
					parameters.add(nameValuePair2);
					entity = new UrlEncodedFormEntity(parameters);
					post.setEntity(entity);
					
					HttpResponse response = client.execute(post);//响应对象
					
					HttpEntity httpEntity = response.getEntity();//获得实体
					
					//获得状态行
					StatusLine statusLine = response.getStatusLine();
					//获得状态码
					int code = statusLine.getStatusCode();
					if(code == 200){
					InputStream inputStream = httpEntity.getContent();//获得流数据
					//读取数据
					String json = ComicsUtil.comicToString(inputStream);
					//解析
				
					TypeComics typeComics = new Gson().fromJson(json, TypeComics.class);
					datas = typeComics.result;
					runOnUiThread(new Runnable() {
						public void run() {
							lvmian.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, datas));
						}
					});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	};
        }.start();
        lvmian.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name = datas.get(position);
				Intent intent = new Intent(context, ComicsActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
    }
}
