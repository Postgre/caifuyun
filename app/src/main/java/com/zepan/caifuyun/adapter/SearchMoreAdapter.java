package com.zepan.caifuyun.adapter;
import com.zepan.caifuyun.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchMoreAdapter extends BaseAdapter {
	private Context ctx;
	private String[] text;
	private int position = 0;
	private int layout = R.layout.search_more_morelist_item;

	public SearchMoreAdapter(Context ctx, String[] text) {
		this.ctx = ctx;
		this.text = text;
	}

	public SearchMoreAdapter(Context ctx, String[] text, int layout) {
		this.ctx = ctx;
		this.text = text;
		this.layout = layout;
	}

	public int getCount() {
		return text.length;
	}

	public Object getItem(int arg0) {
		return text[arg0];
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, layout, null);
			hold.txt = (TextView) arg1
					.findViewById(R.id.Search_more_moreitem_txt);
			hold.layout = (LinearLayout) arg1
					.findViewById(R.id.More_list_lishi);
			hold.iv_action_select=(ImageView)arg1
					.findViewById(R.id.iv_action_select);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		hold.txt.setText(text[arg0]);
		/*hold.layout.setBackgroundResource(R.drawable.my_list_txt_background);*/
		hold.iv_action_select.setBackgroundResource(R.drawable.my_list_txt_background_d);
		//#1988db
		hold.txt.setTextColor(Color.parseColor("#393c3d"));
		if (arg0 == position) {
			/*hold.layout
			.setBackgroundResource(R.drawable.search_more_morelisttop_bkg);*/
			hold.iv_action_select.setBackgroundResource(R.drawable.search_more_morelisttop_bkg);
			hold.txt.setTextColor(Color.parseColor("#1988db"));
		}
		return arg1;
	}

	public void setSelectItem(int i) {
		position = i;
	}

	private static class Holder {
		LinearLayout layout;
		ImageView  iv_action_select;
		TextView txt;
	}
}
