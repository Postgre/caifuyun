package com.zepan.caifuyun.adapter;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Product;
import com.zepan.caifuyun.widget.ProgressTextView;
import com.zepan.caifuyun.widget.RoundProgressBar;

public class HotProductAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<Product> list;//
	public HotProductAdapter(Context context,List<Product>  list){
		this.inflater=LayoutInflater.from(context);
		this.list=list;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Product getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position,View convertView, ViewGroup parent) {
		HolderView holder=null;
		if(convertView==null){
			holder=new HolderView();
			convertView=inflater.inflate(R.layout.listitem_home, null);
			holder.product_name=(TextView)convertView.findViewById(R.id.tv_product_name);
			//holder.yield_num=(TextView)convertView.findViewById(R.id.yield_num);
			holder.tv_min_amount=(TextView)convertView.findViewById(R.id.tv_min_amount);
			holder.proportion=(TextView)convertView.findViewById(R.id.proportion);
			//holder.tv_deadline=(TextView)convertView.findViewById(R.id.tv_deadline);
			holder.loadProgressBar=(ProgressBar)convertView.findViewById(R.id.loadProgressBar);
			holder.tv_percent=(ProgressTextView)convertView.findViewById(R.id.tv_percent);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		if(list.get(position)!=null){
			if(list.get(position).getProductName()!=null){
				holder.product_name.setText(list.get(position).getProductName());
			}
			//holder.yield_num.setText(list.get(position).getAnnualBenefitRateMin()+"");
			//holder.tv_deadline.setText(list.get(position).getInvestmentDeadline()+"");
			//holder.tv_min_amount.setText(list.get(position).getTranchePoint()+"");
			//holder.proportion.setText(list.get(position).getTranchePoint()+"");
			holder.loadProgressBar.setMax(100);
			holder.tv_percent.setTag("tv_percent"+position);
			/*final float raisePercent=list.get(position).getTrancheTotal()/list.get(position).getRaiseTotal();
			final ProgressBar progressBar=holder.loadProgressBar;
			DecimalFormat df = new DecimalFormat("#");
			final String numberPercent=df.format((raisePercent*100));
			if(!numberPercent.equals("NaN")){
				holder.tv_percent.setProgress("%", Integer.parseInt(numberPercent));
				new Thread(new Runnable() {
					@Override
					public void run() {
						int progress=0;//
						while(progress <= (Integer.parseInt(numberPercent))){
							progress += 1;
							progressBar.setProgress(progress);
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}
				}).start();
			}else{
				progressBar.setProgress(0);
			}*/
		}


		return convertView;
	}
	class HolderView{
		TextView product_name;//产品的 名字
		TextView yield_num;//年化收益率
		ImageView  img_pic;//头像
		TextView tv_deadline;//投资期限
		TextView tv_min_amount;//起投金额
		TextView proportion;//返佣比例
		ProgressBar loadProgressBar;//募集比例进度条
		ProgressTextView tv_percent;//返佣比例
	}
}
