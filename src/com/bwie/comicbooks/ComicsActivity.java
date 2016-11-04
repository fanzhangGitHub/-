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
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.comicbooks.bean.ComicBooks;
import com.bwie.comicbooks.bean.ComicBooks.BookList;
import com.bwie.comicbooks.utils.ComicsUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

public class ComicsActivity extends Activity {

	private GridView lvcomics;
	private Context context;
	private List<BookList> bookLists;
	private Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			ComicBooks books = (ComicBooks) msg.obj;
			bookLists =books.result.bookList;
			MyAdapter adapter = new MyAdapter();
			lvcomics.setAdapter(adapter);
			TranslateAnimation translateAnimation = new TranslateAnimation(-100, 0, 0, 0);
			translateAnimation.setDuration(1000);
			AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
			alphaAnimation.setDuration(1000);
			AnimationSet set = new AnimationSet(true);
			set.addAnimation(translateAnimation);
			set.addAnimation(alphaAnimation);
			LayoutAnimationController controller = new LayoutAnimationController(set,1);
//			controller.setDelay(1);
			controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
			lvcomics.setLayoutAnimation(controller);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comic_layout);
		context = this;
		lvcomics = (GridView) findViewById(R.id.lvcomics);
		new Thread(){
			private String newName;

			public void run() {
				Intent intent = getIntent();
				String typeName = intent.getStringExtra("name");
				newName = URLEncoder.encode(typeName);
				try {
				HttpClient client = new DefaultHttpClient();
				String str ="http://japi.juhe.cn/comic/book?"+ "type="+typeName+"&key=b2bffd5346d3f1af4763256d5d819568";
				//GET«Î«Û
				HttpGet get = new HttpGet(str);
//				HttpPost post = new HttpPost("http://japi.juhe.cn/comic/book");
//				
//				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
////				BasicNameValuePair nameValuePair1 = new BasicNameValuePair("type",newName);
//				BasicNameValuePair nameValuePair2 = new BasicNameValuePair("key", "b2bffd5346d3f1af4763256d5d819568");
////				parameters.add(nameValuePair1);
//				parameters.add(nameValuePair2);
//				HttpEntity
//					entity = new UrlEncodedFormEntity(parameters);
//					post.setEntity(entity);
//					
					HttpResponse response = client.execute(get);
					
					StatusLine statusLine = response.getStatusLine();
					
					int code = statusLine.getStatusCode();
					
					if(code == 200){
						HttpEntity httpEntity = response.getEntity();
						
						InputStream inputStream = httpEntity.getContent();
						
						String json = ComicsUtil.comicToString(inputStream);
						
						ComicBooks comicBooks = new Gson().fromJson(json, ComicBooks.class);
						
						
						
						Message msg = handler.obtainMessage();
						
						msg.obj = comicBooks;
						
						handler.sendMessage(msg);
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};
		}.start();
		lvcomics.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name = bookLists.get(position).name;
				Intent intent = new Intent(context, PagerActivity.class);
				intent.putExtra("comicName", name);
				startActivity(intent);
//				overridePendingTransition(, exitAnim)
			}
		});
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bookLists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bookLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.comics_item, null);
				holder.image = (ImageView) convertView.findViewById(R.id.iamge);
				holder.tvname = (TextView) convertView.findViewById(R.id.tv1);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			BitmapUtils bitmapUtils = new BitmapUtils(context);
			bitmapUtils.display(holder.image, bookLists.get(position).coverImg);
			holder.tvname.setText(bookLists.get(position).name);
			return convertView;
		}
	}
	class ViewHolder{
		TextView tvname;
		ImageView image;
	}
}
