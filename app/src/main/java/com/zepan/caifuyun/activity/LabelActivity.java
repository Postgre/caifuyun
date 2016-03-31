package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.TagItem;
import com.zepan.caifuyun.entity.Tags;
import com.zepan.caifuyun.widget.FlowLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 标签
 * @author duanjie
 *
 */
public class LabelActivity extends BaseActivity implements OnClickListener{
	private List<String> labelList=new ArrayList<String>();
	private FlowLayout labelLineayLayout;
	private final static int LABEL=0x101;
	View[] views;
	FlowLayout mTagLayout, mAddTagLayout;
	EditText mEditText;
	private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();
	private int MAX_TAG_CNT = 5;
	private int accountId;
	private ArrayList<Tags> tagsList=new ArrayList<Tags>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label);
		setHeaderFields(0, R.string.title_label, 
				0,R.drawable.ic_action_back, 0,0);
		initView();
		initData();
		getRecentlyTags();
	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_left)).setOnClickListener(this);
		mTagLayout = (FlowLayout) findViewById(R.id.tag_layout);
		mAddTagLayout = (FlowLayout) findViewById(R.id.addtag_layout);
		mEditText = (EditText) findViewById(R.id.add_edit);
		mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String text = mEditText.getEditableText()
							.toString().trim();
					if (text.length() > 0) {
						if (idxTextTag(text) < 0) {
							if (mAddTags.size() == MAX_TAG_CNT) {
								Toast.makeText(LabelActivity.this, "最多选择" + MAX_TAG_CNT + "个标签", Toast.LENGTH_SHORT).show();
								return false;
							}
							getCreateTags(text,0,0);

						}
						mEditText.setText("");
					}
					return true;
				}
				return false;
			}
		});
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("accountId")){
			accountId=intent.getIntExtra("accountId", -1);
			tagsList=intent.getParcelableArrayListExtra("tagsList");
			for(Tags tags:tagsList){
				doAddText(tags.getName(), true, -1);
			}

		}
	}
	private void getRecentlyTags(){
		try {
			String token =URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid =getStringLocalData("uid");
			String tid =getStringLocalData("tid");
			String url=Url.GetRecentlyTags+"?token="+token+"&size="+20+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					try {
						if(response.getInt("status")==0){
							recentlyTagsResult(response);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void recentlyTagsResult(JSONObject response)throws JSONException{
		JSONArray jsonArray=response.getJSONArray("tags");
		List<Tags> tagsList=new ArrayList<Tags>();
		for(int i=0;i<jsonArray.length();i++){
			Tags tags=(Tags) JsonUtil.jsonToEntity(jsonArray.getJSONObject(i), Tags.class); 
			tagsList.add(tags);
		}
		initLayout(tagsList);
	}
	private void getCreateTags(final String text,final int tag,final int pos){
		try {
			String token =URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid =getStringLocalData("uid");
			String tid =getStringLocalData("tid");
			String textEncoder =URLEncoder.encode(text, "UTF-8");
			String url=Url.CreateTags+"?token="+token+"&accountId="+accountId+"&uid="+uid+"&tid="+tid+"&tag="+textEncoder;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					try {
						if(response.getInt("status")==0){
							Tags tags=new Tags();
							tags.setId(response.getJSONObject("info").getInt("id"));
							tags.setName(text);
							tagsList.add(tags);
							if(tag==0x100){
								boolean bResult = doAddText(text, false, pos);
								for(int i=0;i<mTagLayout.getChildCount();i++){
									if(mTagLayout.getChildAt(i)!=null&&mTagLayout.getChildAt(i).getTag()!=null
											&&mTagLayout.getChildAt(i).getTag().equals(text)){
										mTagLayout.getChildAt(i).setActivated(bResult);
									}
								}
							}else{
								doAddText(text, true, -1);
							}

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void getDeleteTag(final String str,final int index){
		try {
			String token =URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid =getStringLocalData("uid");
			String tid =getStringLocalData("tid");
			String url=Url.DeleteTags+"?token="+token+"&tagId="+tagsList.get(index).getId()+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					try {
						if(response.getInt("status")==0){
							for(int i=0;i<tagsList.size();i++){
								if(str.equals(tagsList.get(i).getName())){
									tagsList.remove(tagsList.get(i));
									doDelText(str);
								}
							}
							
						}
						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


}



private void initLayout(final List<Tags> textList) {
	for (int i = 0; i < textList.size(); i++) {
		final int pos = i;
		final TextView text = (TextView) LayoutInflater.from(this).inflate(
				R.layout.tag_text, mTagLayout, false);
		text.setText(textList.get(i).getName());
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text.setActivated(true);
				for(int i=0;i<tagsList.size();i++){
					if(text.getText().toString().equals(tagsList.get(i).getName())){
						text.setActivated(false);
					}
				}
				if (text.isActivated()) {
					text.setTag(textList.get(pos).getName());
					int tag=0x100;
					getCreateTags(textList.get(pos).getName(),tag,pos);
				} else {
					for(Tags tags:tagsList){
						if(text.getText().toString().equals(tags.getName())){
							getDeleteTag(textList.get(pos).getName(),tagsList.indexOf(tags));
						}
					}
					//doDelText(textList.get(pos).getName());
				}
			}
		});
		mTagLayout.addView(text);
	}
	((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
}

protected void doResetAddTagsStatus() {
	int cnt = mAddTags.size();
	for (int i = 0; i < cnt; i++) {
		TagItem item = mAddTags.get(i);
		if(mAddTags.get(i).tagText.equals(tagsList.get(i).getName())){
			item.mView.setActivated(false);
		}else{
			item.mView.setActivated(true);
		}
		item.mView.setText(item.tagText);
	}
}	
protected void doDelText(String string) {
	int mTagCnt = mAddTags.size();
	mEditText.setVisibility(View.VISIBLE);
	for (int i = 0; i < mTagCnt; i++) {
		TagItem item = mAddTags.get(i);
		if (string.equals(item.tagText)) {
			mAddTagLayout.removeViewAt(i);
			mAddTags.remove(i);
			if (!item.tagCustomEdit) {
				mTagLayout.getChildAt(item.idx).setActivated(false);
			}
			return;
		}
	}
}

private boolean doAddText(final String str, boolean bCustom, int idx) {
	int tempIdx = idxTextTag(str);
	if (tempIdx >= 0) {
		TagItem item = mAddTags.get(tempIdx);
		item.tagCustomEdit = false;
		item.idx = tempIdx;

		return true;
	}

	int tagCnt = mAddTags.size();
	TagItem item = new TagItem();
	item.tagText = str;
	item.tagCustomEdit = bCustom;
	item.idx = idx;
	mAddTags.add(item);

	final TextView view = (TextView) LayoutInflater.from(this).inflate(
			R.layout.addtag_text, mAddTagLayout, false);
	item.mView = view;
	view.setText(str);
	view.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (view.isActivated()) {
				for(int i=0;i<mAddTagLayout.getChildCount();i++){
					if(view.equals(mAddTagLayout.getChildAt(i))){
						getDeleteTag(str,i);//删除标签
					}
				}


			} else {
				doResetAddTagsStatus();
				view.setText(view.getText() + "x");
				view.setActivated(true);
			}
		}
	});
	mAddTagLayout.addView(view, tagCnt);
	tagCnt++;
	if (tagCnt == MAX_TAG_CNT) {
		mEditText.setVisibility(View.GONE);
	}
	return true;
}
protected int idxTextTag(String text) {
	int mTagCnt = mAddTags.size();
	for (int i = 0; i < mTagCnt; i++) {
		TagItem item = mAddTags.get(i);
		if (text.equals(item.tagText)) {
			return i;
		}
	}
	return -1;
}
@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.iv_left:
		Intent intentlabel=new Intent();
		String lableString="";
		if(mAddTagLayout.getChildCount()==5){
			for(int i=0;i<mAddTagLayout.getChildCount();i++){
				if(i==mAddTagLayout.getChildCount()-1){
					lableString+=((TextView)mAddTagLayout.getChildAt(i)).getText().toString();
				}else{
					lableString+=((TextView)mAddTagLayout.getChildAt(i)).getText().toString()+" ";
				}
			}
		}else{
			for(int i=0;i<mAddTagLayout.getChildCount()-1;i++){
				if(i==mAddTagLayout.getChildCount()-2){
					lableString+=((TextView)mAddTagLayout.getChildAt(i)).getText().toString();
				}else{
					lableString+=((TextView)mAddTagLayout.getChildAt(i)).getText().toString()+" ";
				}
			}
		}
		intentlabel.putExtra("label",lableString);
		LabelActivity.this.setResult(RESULT_OK, intentlabel);
		finish(); 
		break;

	default:
		break;
	}

}

}
