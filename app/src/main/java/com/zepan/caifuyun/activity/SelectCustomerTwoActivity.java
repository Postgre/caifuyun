package com.zepan.caifuyun.activity;

import java.util.ArrayList;

import com.zepan.android.widget.AlphaImageView;
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

/**
 * 选择个人    和机构  add
 * @author duanjie
 *
 */
public class SelectCustomerTwoActivity extends BaseActivity implements OnClickListener{

	private BaseFragment personFragment;
	private BaseFragment organizationFragment;
	//页面列表
	private ArrayList<BaseFragment> fragmentList=new ArrayList<BaseFragment>();
	private static final int INDIVIDUAL=0x2000;
	private static final int ORIGANIZATION=0x2001;
	private int currentTag=INDIVIDUAL;
	public static final int SELECTCUSTOM=0x2002;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_customer_two);
		initView();
		initData();
	}
	private void initData(){
		Intent intent = getIntent();
		if(intent.hasExtra("selectCustom")){
			if(personFragment instanceof PersonFragment){
				((PersonFragment)personFragment).mHandler.sendEmptyMessage(SELECTCUSTOM);
				((OrganizationFragment)organizationFragment).mHandler.sendEmptyMessage(SELECTCUSTOM);
			}
		}
		
	}

	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_right)).setImageResource(R.drawable.ic_action_add);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		findViewById(R.id.iv_right).setOnClickListener(this);
		final ViewPager viewPager=(ViewPager) findViewById(R.id.vPager);
		final RadioGroup radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_all:
					viewPager.setCurrentItem(0);
					currentTag=INDIVIDUAL;
					break;
				case R.id.rbtn_anonymous:
					viewPager.setCurrentItem(1);
					currentTag=ORIGANIZATION;
					break;
				default:
					break;
				}

			}
		});
		
		
		personFragment=new PersonFragment();
		
		organizationFragment=new OrganizationFragment();
		fragmentList.add(personFragment);
		fragmentList.add(organizationFragment);
		viewPager.setAdapter(new FragViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				((RadioButton)radioGroup.getChildAt(position)).setChecked(true);
				if(position==0){
					currentTag=INDIVIDUAL;
				}else if(position==1){
					currentTag=ORIGANIZATION;
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


	//IndividualCustomersActivity
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right:
			Intent intentNewIndividualCustomers=new Intent(SelectCustomerTwoActivity.this,NewIndividualCustomersActivity.class);
			if(currentTag==INDIVIDUAL){
				intentNewIndividualCustomers.putExtra("newIndividual", "newIndividual");
			}else if(currentTag==ORIGANIZATION){
				intentNewIndividualCustomers.putExtra("newOrganization", "newOrganization");
			}
			startActivity(intentNewIndividualCustomers);
			break;

		default:
			break;
		}
		/*	Intent SelectCustomerIntent=new Intent(SelectCustomerTwoActivity.this,SelectCustomerSearchActivity.class);
		startActivity(SelectCustomerIntent);*/

	}


}
