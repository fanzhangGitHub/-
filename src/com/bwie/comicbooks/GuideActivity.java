package com.bwie.comicbooks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

public class GuideActivity extends Activity {

	private List<View>datas;
	private ViewPager guideSvvp;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_layout);
		context = this;
		datas = new ArrayList<View>();
		guideSvvp = (ViewPager) findViewById(R.id.guide_svvp);
		datas.add(View.inflate(context, R.layout.guide_item2, null));
		datas.add(View.inflate(context, R.layout.guide_item3, null));
		datas.add(View.inflate(context, R.layout.guide_item2, null));
		guideSvvp.setAdapter(new MyAdapter());
		guideSvvp.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int current) {
			if(current ==2){
				startActivity(new Intent(context, MainActivity.class));
				finish();
			}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 ==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(datas.get(position));
			return datas.get(position);
			
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(datas.get(position));
			super.destroyItem(container, position, object);
			
		}
	}
}
