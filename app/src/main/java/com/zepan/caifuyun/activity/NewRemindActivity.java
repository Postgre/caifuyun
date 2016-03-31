package com.zepan.caifuyun.activity;

import com.zepan.android.util.StringUtil;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.entity.Remind;
import com.zepan.caifuyun.widget.DateTimeDialog;
import com.zepan.caifuyun.widget.DateTimeDialog.PriorityListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
/**
 * 新建提醒
 * @author duanjie
 *
 */
public class NewRemindActivity extends BaseActivity  implements OnClickListener{

	private TextView deadline;
	String dateTime=StringUtil.longToFormatTimeUtcStr(System.currentTimeMillis()/1000+"", "yyyy-MM-dd HH:mm");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_remind);
		setHeaderFields(0, R.string.remind, 
				0,R.drawable.ic_action_back, 0,0);
		initView();
	}


	private void initView() {
		deadline=(TextView)findViewById(R.id.et_deadline);
		findViewById(R.id.btn_save).setOnClickListener(this);
		findViewById(R.id.rl_remind_reinvent).setVisibility(View.GONE);
		findViewById(R.id.rl_remind_time).setVisibility(View.GONE);
		findViewById(R.id.rl_repeat).setVisibility(View.GONE);

	}

	public void show(View v) {
		getDate();
	}
	public void getDate() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		String curDate =deadline.getText().toString();
		String[] dates = dateTime.split(" ");
		int[] date = getYMDArray(dates[0]+"", "-");
		int[] time = getYMDArray(dates[1]+"", ":");
		DateTimeDialog  dateTimeDialog= new DateTimeDialog(this,
				new PriorityListener() {
			@Override
			public void refreshPriorityUI(String year, String month,
					String day, String hours, String mins) {
				dateTime=year + "-" + month + "-" + day+" "+hours + ":" + mins;
				deadline.setText(dateTime);
				if(TextUtils.isEmpty(deadline.getText())){
					findViewById(R.id.rl_remind_reinvent).setVisibility(View.GONE);
					findViewById(R.id.rl_remind_time).setVisibility(View.GONE);
					findViewById(R.id.rl_repeat).setVisibility(View.GONE);
				}else{
					findViewById(R.id.rl_remind_reinvent).setVisibility(View.VISIBLE);
					findViewById(R.id.rl_remind_time).setVisibility(View.VISIBLE);
				}
				
			}
		}, date[0], date[1], date[2], time[0], time[1], width,
		height, "选择时间");
		Window window = dateTimeDialog.getWindow();
		window.setGravity(Gravity.BOTTOM); //
		window.setWindowAnimations(R.style.dialogstyle); //
		dateTimeDialog.setCancelable(true);
		dateTimeDialog.show();
	}
	public static int[] getYMDArray(String datetime, String splite) {
		int date[] = { 0, 0, 0, 0, 0 };
		if (datetime != null && datetime.length() > 0) {
			String[] dates = datetime.split(splite);
			int position = 0;
			for (String temp : dates) {
				date[position] = Integer.valueOf(temp);
				position++;
			}
		}
		return date;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.et_deadline:

			break;
		 */
		case R.id.btn_save:
			if(!((CheckableEditText)findViewById(R.id.et_remind_name)).check()){
				return;
			}
			if(TextUtils.isEmpty(((TextView)findViewById(R.id.et_remind_time)).getText())){
				printToast("提醒时间没有选择");
				return;
			}
			int buttonId=((RadioGroup)this.findViewById(R.id.rg_type)).getCheckedRadioButtonId();
			Remind remind=new Remind();
			remind.setRemindName(((CheckableEditText)findViewById(R.id.et_remind_name)).getText().toString());
//			((RadioButton)findViewById(buttonId)).getText();
			if(buttonId==R.id.rb_reminder_anniversary){
				remind.setRemindType(1);
			}else if(buttonId==R.id.rb_reminder_finance){
				remind.setRemindType(2);
			}else if(buttonId==R.id.rb_reminder_visit){
				remind.setRemindType(3);
			}
			Intent intent=new Intent();
			intent.putExtra("remind", remind);
			this.setResult(RESULT_OK,intent);
			finish();
			break;

		default:
			break;
		}

	}


}
