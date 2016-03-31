package com.zepan.caifuyun.adapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Product;

import com.zepan.caifuyun.widget.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 交易记录的第一个adapter
 * @author duanjie
 *
 */
public class TransactionRecordAdapter extends BaseAdapter{
	Context context;
	LayoutInflater mInflater;
	TreeSet<Product> set;
	List<Product> temp;
	List<Product> list = new ArrayList<Product>();
	Map<String, List<Product>> map = new HashMap<String, List<Product>>();
	int count;

	public TransactionRecordAdapter(Context c, List<Product> list)
	{
		this.context = c;
		this.list = list;

		mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		set = new TreeSet<Product>(new Comparator<Product>()
				{
			@Override
			public int compare(Product lhs, Product rhs)
			{
				// TODO Auto-generated method stub
				return lhs.getProductName().compareToIgnoreCase(rhs.getProductName());
			}
				});
		set.addAll(list);
		temp = new ArrayList<Product>(set);
		count = set.size(); 
		for (Product Product : list)//循环对传递进来的数据进行分类，将同一种类的Product分配到同一个值中
		{
			if (map.containsKey(Product.getProductType()))//判断map中有没有这个种类  有的话直接将Product 添加到这个种类下
			{
				map.get(Product.getProductType()).add(Product);
			}
			else//没有这个种类  就创建这个种类 将Product 添加到这个种类下
			{
				List<Product> ds = new ArrayList<Product>();
				ds.add(Product);
				map.put(Product.getProductName(), ds);
			}
		}
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return count;
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
			convertView = mInflater.inflate(R.layout.listitem_transaction_record, null);
			holder = new Holder();
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.list = (ListView) convertView.findViewById(android.R.id.list);

			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}

		if (temp.get(position) != null)
		{
			holder.tv_type.setText(temp.get(position).getProductName());

			TransactionRecordItemAdapter adapter = new TransactionRecordItemAdapter(context, map.get(temp.get(position).getProductName()));

			holder.list.setAdapter(adapter);
			Utility.setListViewHeightBasedOnChildren(holder.list);
		}
		return convertView;
	}

	class Holder
	{
		TextView tv_type;
		ListView list;
	}

}
