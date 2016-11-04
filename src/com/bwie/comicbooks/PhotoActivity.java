package com.bwie.comicbooks;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LayoutAnimationController.AnimationParameters;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bwie.comicbooks.bean.MyShow;
import com.bwie.comicbooks.bean.MyShow.ImageList;
import com.bwie.comicbooks.utils.ComicsUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

public class PhotoActivity extends Activity {

	private GridView lvphoto;
	private Context context;
	private List<ImageList> imageLists;
	private ViewPager svvp;
	private AlertDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_layout);
		context =this;
		lvphoto = (GridView) findViewById(R.id.lvphoto);
		new Thread(){
			public void run() {
				Intent intent = getIntent();
				int myId = intent.getIntExtra("myId", 0);
				String comicName = intent.getStringExtra("comicName").trim();
//				String name = URLEncoder.encode(comicName);
//				Log.d("TAG","传值++++++++++++++"+myId+ comicName);
				String str = "http://japi.juhe.cn/comic/chapterContent?comicName="+comicName+"&id="+myId+"&key=b2bffd5346d3f1af4763256d5d819568";
				HttpClient client = new DefaultHttpClient();
				
				HttpGet get = new HttpGet(str);
				
				try {
					HttpResponse response = client.execute(get);//网络请求
					
					StatusLine statusLine = response.getStatusLine();//状态改变行
					
					int code = statusLine.getStatusCode();
					
					
					if(code == 200){
						HttpEntity entity = response.getEntity();//获得网络数据实体
						
						InputStream inputStream = entity.getContent();//获得网络流数据
						
						final String json = ComicsUtil.comicToString(inputStream);
						
						runOnUiThread(new Runnable() {
							

							public void run() {
								MyShow myShow = new Gson().fromJson(json, MyShow.class);
								
								imageLists = myShow.result.imageList;
								MyAdapter adapter = new MyAdapter();
								lvphoto.setAdapter(adapter);
								TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -100, 0);
								translateAnimation.setDuration(500);
//								translateAnimation.setInterpolator(TranslateAnimation.START_ON_FIRST_FRAME)
								AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
								alphaAnimation.setDuration(500);
								AnimationSet set = new AnimationSet(true);
								set.addAnimation(translateAnimation);
								set.addAnimation(alphaAnimation);
								LayoutAnimationController controller = new LayoutAnimationController(set,1);
								controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
								lvphoto.setLayoutAnimation(controller);
							}
						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			};
		}.start();
		lvphoto.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				View view1 = View.inflate(context, R.layout.viewpager_layout, null);
				svvp = (ViewPager) view1.findViewById(R.id.svvp);
				MyPagerAdapter adapter1 = new 	MyPagerAdapter();
				svvp.setAdapter(adapter1);
				svvp.setCurrentItem(position);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view1);
			dialog = builder.create();
			dialog.show();
			}
		});
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageLists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imageLists.get(position);
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
				convertView = View.inflate(context, R.layout.photo_item, null);
				holder.image = (ImageView) convertView.findViewById(R.id.myimage);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			BitmapUtils bitmapUtils = new BitmapUtils(context);
			bitmapUtils.display(holder.image, imageLists.get(position).imageUrl);
			return convertView;
		}
	}
	class ViewHolder{
		ImageView image;
	}
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageLists.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
		
			return arg0 == arg1;
		}
		public Object instantiateItem(ViewGroup container, int position) {
			View view = LayoutInflater.from(context).inflate(R.layout.viewpager_item, null);
			ImageView image = (ImageView) view.findViewById(R.id.svvpitem);
			BitmapUtils bitmapUtils = new BitmapUtils(context);
			bitmapUtils.display(image, imageLists.get(position).imageUrl);
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
			container.removeView((View)object);
		}
	}
}
