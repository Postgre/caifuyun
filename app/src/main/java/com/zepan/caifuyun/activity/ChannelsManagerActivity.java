package com.zepan.caifuyun.activity;

import java.util.ArrayList;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.base.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
/***
 * 渠道管理
 * @author long
 *
 */
public class ChannelsManagerActivity extends BaseActivity implements OnClickListener {
	private BaseFragment institutionsFragment;
	private BaseFragment guestChannelsFragment;
	private static final int INDIVIDUAL=0x2000;
	private static final int ORIGANIZATION=0x2001;
	private int currentTag = ORIGANIZATION;
	//页面列表
	private ArrayList<BaseFragment> fragmentList=new ArrayList<BaseFragment>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channels_manager);
		initView();
	}
	private void initView(){
		setHeaderFields(0,R.string.channelManager,0,R.drawable.ic_action_back,0,R.drawable.ic_action_add);
		findViewById(R.id.iv_right).setOnClickListener(this);
		final ViewPager viewPager=(ViewPager) findViewById(R.id.vPager);
		final RadioGroup radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_institutions:
					viewPager.setCurrentItem(0);
					currentTag=ORIGANIZATION;
					break;
				case R.id.rb_guestChannels:
					currentTag=INDIVIDUAL;
					
					viewPager.setCurrentItem(1);
					break;
				default:
					break;
				}

			}
		});
		
		
		institutionsFragment=new InstitutionsAcceptFragment();
		guestChannelsFragment=new GuestChannelsFragment();
		fragmentList.add(institutionsFragment);
		fragmentList.add(guestChannelsFragment);
		viewPager.setAdapter(new FragViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position==1){
					currentTag=INDIVIDUAL;
					((RadioButton)radioGroup.getChildAt(position+1)).setChecked(true);
				}else if(position==0){
					currentTag=ORIGANIZATION;
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right:
			Intent intent=new Intent(this, NewChannelsActivity.class);
			if(currentTag==INDIVIDUAL){//有客渠道
				intent.putExtra("newChannelIndividualId","newChannelIndividualId");
			}else if(currentTag==ORIGANIZATION){//机构渠道
				intent.putExtra("newChannelOrganization","newChannelOrganization");
			}
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
}
