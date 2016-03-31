package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.widget.DialogBottom;
import com.zepan.caifuyun.widget.DialogBottom.OnItemClick;

/*
 * 销售日报
 */
public class SalesJournalActivity extends BaseActivity {

	private BaseFragment commitjournalFragment;
	private BaseFragment readjournalFragment;
	ImageView image_back;
	
	private ListView listview;
	
	List<String> list=new ArrayList<String>();
	
	private ArrayList<BaseFragment> fragmentList=new ArrayList<BaseFragment>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sales_journal);
		
		initView();
	}
	
	private void initView(){
		
		final ViewPager viewPager=(ViewPager) findViewById(R.id.viewpager);
		final RadioGroup radioGroup=(RadioGroup) findViewById(R.id.rg);
		image_back=(ImageView) findViewById(R.id.iv_back);
		ImageView iv_add=(ImageView) findViewById(R.id.iv_add);
		iv_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SalesJournalActivity.this,WriteDetailsActivity.class);
        		startActivity(intent);
			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			ImageView iv_more=(ImageView) findViewById(R.id.iv_more);
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_commit://我提交的日报
					viewPager.setCurrentItem(0);
					iv_more.setVisibility(View.GONE);
					
					break;
				case R.id.rb_read://我批阅的日报
					viewPager.setCurrentItem(1);
					iv_more.setVisibility(View.VISIBLE);
					//销售日报 选择
					iv_more.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							final DialogBottom infoDailog=new DialogBottom(SalesJournalActivity.this);
							String[] nameArray={"全部标记已阅","未阅","已阅"};
							infoDailog.setArray(nameArray);
							infoDailog.setOnItemClick(new OnItemClick() {

								@Override
								public void itemClick(AdapterView<?> parent, View view, int position, long id) {
									switch (position) {
									case 0:
										infoDailog.dismiss();
										
										break;
									case 1:
										infoDailog.dismiss();

										Intent intent=new Intent(SalesJournalActivity.this,UnReadDetailsActivity.class);
						        		startActivity(intent);
										break;
									case 2:
										infoDailog.dismiss();
										Intent intent2=new Intent(SalesJournalActivity.this,ReadDetailsActivity.class);
						        		startActivity(intent2);
										break;
								
									default:
										break;
									}

								}
							});
							infoDailog.show();

						         
						}
					});
					break;
				default:
					break;
				}

			}
		});
		
		commitjournalFragment=new CommitJournalFragment();
	    readjournalFragment=new ReadJournalFragment();
		fragmentList.add(commitjournalFragment);
		fragmentList.add(readjournalFragment);
		
		viewPager.setAdapter(new FragViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position==1){
					((RadioButton)radioGroup.getChildAt(position+1)).setChecked(true);
				}else if(position==0){
					((RadioButton)radioGroup.getChildAt(position)).setChecked(true);
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
	
	public class FragViewPagerAdapter extends FragmentPagerAdapter{
		public FragViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BaseFragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return null;
		}


	}
	
	public void click(View v){
		switch(v.getId()){
		case R.id.iv_back:
			finish();
		}
	}
	

	
}
