package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.entity.Product;
import com.zepan.caifuyun.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 交易记录的第二个adapter
 * @author duanjie
 *
 */
public class TransactionRecordItemAdapter extends BaseAdapter{
	List<Product> list;
	Context context;
	LayoutInflater mInflater;

	public TransactionRecordItemAdapter(Context c,List<Product> list){
		this.context = c;
		this.list = list;
		mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{

		Holder holder;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.listitem_transaction_record_two, null);
			holder = new Holder();
			//holder.text = (TextView) convertView.findViewById(R.id.text2);

			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}


		//holder.text.setText(list.get(position).getCardName());




		return convertView;
	}

	class Holder
	{
		TextView text;
	}



}
