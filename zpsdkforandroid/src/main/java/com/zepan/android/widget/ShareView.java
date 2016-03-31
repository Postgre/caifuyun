package com.zepan.android.widget;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.zepan.android.sdk.R;

/**
 * Scroller应用
 * @author Ra  com.zepan.library.widget.ShareView
 * blog : http://blog.csdn.net/vipzjyno1/
 */
public class ShareView extends RelativeLayout implements OnClickListener{
	/** Scroller 拖动类 */
	private Scroller mScroller;
	/** 屏幕高度  */
	private int mScreenHeigh = 0;
	/** 屏幕宽度  */
	private int mScreenWidth = 0;
	/** 点击时候Y的坐标*/
	private int downY = 0;
	/** 拖动时候Y的坐标*/
	private int moveY = 0;
	/** 拖动时候Y的方向距离*/
	private int scrollY = 0;
	/** 松开时候Y的坐标*/
	private int upY = 0;
	/** 是否在移动*/
	private Boolean isMoving = false;
	/** 布局的高度*/
	private int viewHeight = 0;
	/** 是否打开*/	
	public boolean isShow = false;
	/** 是否可以拖动*/	
	public boolean mEnabled = true;
	/** 点击外面是否关闭该界面*/	
	public boolean mOutsideTouchable = true;
	/** 上升动画时间 */
	private int mDuration = 800;
	private final static String TAG = "LoginView";
	public static final String DESCRIPTOR = "com.umeng.share";
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);
	private SHARE_MEDIA mPlatform =null;
	Context context;
	private int tag=-1;
	View view ;
	String productDetailUrl;
	public ShareView(Context context) {
		super(context);
		init(context);
	}

	public ShareView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		this.context=context;
		addCustomPlatforms();
	}

	public ShareView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		setFocusable(true);
		mScroller = new Scroller(context);
		mScreenHeigh = BaseTools.getWindowHeigh(context);
		mScreenWidth = BaseTools.getWindowWidth(context);
		// 背景设置成透明
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		view= LayoutInflater.from(context).inflate(R.layout.view_share,null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);// 如果不给他设这个，它的布局的MATCH_PARENT就不知道该是多少
		addView(view, params);// ViewGroup的大小，
		// 背景设置成透明
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		view.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				viewHeight = view.getHeight();
			}
		});
		view.findViewById(R.id.ly_qq).setOnClickListener(ShareView.this);;
		view.findViewById(R.id.ly_qzone).setOnClickListener(ShareView.this);
		view.findViewById(R.id.ly_email).setOnClickListener(this);
		view.findViewById(R.id.ly_wechat).setOnClickListener(this);
		view.findViewById(R.id.ly_moments).setOnClickListener(this);
		view.findViewById(R.id.ly_weibo).setOnClickListener(this);
		view.findViewById(R.id.tv_delete_product).setOnClickListener(this);
		view.findViewById(R.id.tv_edit_product).setOnClickListener(this);
		ShareView.this.scrollTo(0, mScreenHeigh);
		//头文件
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(!mEnabled){
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) event.getY();
			Log.d(TAG, "downY = " + downY);
			//如果完全显示的时候，让布局得到触摸监听，如果不显示，触摸事件不拦截，向下传递
			if(isShow){
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			moveY = (int) event.getY();
			scrollY = moveY - downY;
			//向下滑动
			if (scrollY > 0) {
				if(isShow){
					scrollTo(0, -Math.abs(scrollY));
				}
			}else{
				if(mScreenHeigh - this.getTop() <= viewHeight && !isShow){
					scrollTo(0, Math.abs(viewHeight - scrollY));
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			upY = (int) event.getY();
			if(isShow){
				if( this.getScrollY() <= -(viewHeight /2)){
					startMoveAnim(this.getScrollY(),-(viewHeight - this.getScrollY()), mDuration);
					isShow = false;
					Log.d("isShow", "false");
				} else {
					startMoveAnim(this.getScrollY(), -this.getScrollY(), mDuration);
					isShow = true;
					Log.d("isShow", "true");
				}
			}
			Log.d("this.getScrollY()", ""+this.getScrollY());
			changed();
			break;
		case MotionEvent.ACTION_OUTSIDE:
			Log.d(TAG, "ACTION_OUTSIDE");
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 拖动动画
	 * @param startY  
	 * @param dy  移动到某点的Y坐标距离
	 * @param duration 时间
	 */
	public void startMoveAnim(int startY, int dy, int duration) {
		isMoving = true;
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();//通知UI线程的更新
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 更新界面
			postInvalidate();
			isMoving = true;
		} else {
			isMoving = false;
		}
		super.computeScroll();
	}

	/** 开打界面 */
	public void show(){
		if(!isShow && !isMoving){
			ShareView.this.startMoveAnim(-viewHeight,   viewHeight, mDuration);
			isShow = true;
			Log.d("isShow", "true");
			changed();
		}
	}

	/** 关闭界面 */
	public void dismiss(){
		if(isShow && !isMoving){
			ShareView.this.startMoveAnim(0, -viewHeight, mDuration);
			isShow = false;
			Log.d("isShow", "false");
			changed();
		}
	}

	/** 是否打开 */
	public boolean isShow(){
		return isShow;
	}

	/** 获取是否可以拖动*/
	public boolean isSlidingEnabled() {
		return mEnabled;
	}

	/** 设置是否可以拖动*/
	public void setSlidingEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	/**
	 * 设置监听接口，实现遮罩层效果
	 */
	public void setOnStatusListener(onStatusListener listener){
		this.statusListener = listener;
	}

	public void setOutsideTouchable(boolean touchable) {
		mOutsideTouchable = touchable;
	}
	/**
	 * 显示状态发生改变时候执行回调借口
	 */
	public void changed(){
		if(statusListener != null){
			if(isShow){
				statusListener.onShow();
			}else{
				statusListener.onDismiss();
			}
		}
	}

	/** 监听接口*/
	public onStatusListener statusListener;

	/**
	 * 监听接口，来在主界面监听界面变化状态
	 */
	public interface onStatusListener{
		/**  开打状态  */
		public void onShow();
		/**  关闭状态  */
		public void onDismiss();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ly_qq){
			choiceThirdParty(R.id.ly_qq);
		}else if(v.getId()==R.id.ly_qzone){
			choiceThirdParty(R.id.ly_qzone);
		}else if(v.getId()==R.id.ly_email){
			choiceThirdParty(R.id.ly_email);
		}else if(v.getId()==R.id.ly_wechat){
			choiceThirdParty(R.id.ly_wechat);
		}else if(v.getId()==R.id.ly_moments){
			choiceThirdParty(R.id.ly_moments);
		}else if(v.getId()==R.id.ly_weibo){
			choiceThirdParty(R.id.ly_weibo);
		}else if(v.getId()==R.id.tv_delete_product){
			if(mOnPopDialog!=null){
				mOnPopDialog.popDialog();
			}
			dismiss();
		}else if(v.getId()==R.id.tv_edit_product){
			if(monModifyProduct!=null){
				monModifyProduct.modifyProduct();
			}
			dismiss();
		}else if(v.getId()==R.id.btn_cancel){
			dismiss();
		}

	}




	private void choiceThirdParty(int id){
		dismiss();
		UMImage Image=null;
		/*try {
			Bitmap bitmap = util.getPathBitmap(Uri.fromFile(new File(fileList.get(0))), 200, 200);
			Image= new UMImage(this,bitmap);

		} catch (FileNotFoundException e) {
			Image= new UMImage(this,
					BitmapFactory.decodeResource(getResources(), R.drawable.device));
			e.printStackTrace();
		}*/
		//Log.i("---------------------------------", list.get(position).isHasLiked()+"");
		//http://119.254.101.12:8081/product/
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		Image= new UMImage(context, bitmap);
		if(id==R.id.ly_qq){
			// 设置QQ空间分享内容
			QQShareContent qq = new QQShareContent();
			qq.setTitle("欢乐财神-qq");
			qq.setShareContent("欢乐财神-qq"+productDetailUrl);
			qq.setShareImage(Image);
			qq.setTargetUrl(productDetailUrl);
			mController.setShareMedia(qq);
			mPlatform=SHARE_MEDIA.QQ;
		}
		if(id==R.id.ly_qzone){
			// 设置QQ空间分享内容
			QZoneShareContent qzone = new QZoneShareContent();
			qzone.setShareContent("欢乐财神-qzone"+productDetailUrl);
			qzone.setTargetUrl(productDetailUrl);
			qzone.setTitle("欢乐财神-qq空间");
			qzone.setShareImage(Image);
			mController.setShareMedia(qzone);
			mPlatform=SHARE_MEDIA.QZONE;
		}
		if(id==R.id.ly_wechat){
			WeiXinShareContent weixinContent = new WeiXinShareContent();
			weixinContent.setShareContent("欢乐财神-weixin"+productDetailUrl);
			weixinContent.setTitle("欢乐财神-微信");
			weixinContent.setTargetUrl(productDetailUrl);
			weixinContent.setShareMedia(Image);
			mController.setShareMedia(weixinContent);
			mPlatform=SHARE_MEDIA.WEIXIN;
		}

		if(id==R.id.ly_moments){
			// 设置朋友圈分享的内容
			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setShareContent("欢乐财神-朋友圈"+productDetailUrl);
			circleMedia.setTitle("欢乐财神");
			circleMedia.setShareMedia(Image);
			// circleMedia.setShareMedia(uMusic);
			// circleMedia.setShareMedia(video);
			circleMedia.setTargetUrl(productDetailUrl);
			mController.setShareMedia(circleMedia);
			mPlatform=SHARE_MEDIA.WEIXIN_CIRCLE;
		}
		if(id==R.id.ly_email){
			// 设置邮件分享内容， 如果需要分享图片则只支持本地图片
			MailShareContent mail = new MailShareContent();
			mail.setTitle("欢乐财神");
			mail.setShareContent("欢乐财神-email"+productDetailUrl);
			//mail.setShareContent("欢乐财神-email。http://www.umeng.com/social");
			// 设置tencent分享内容
			mController.setShareMedia(mail);
			mPlatform=SHARE_MEDIA.EMAIL;
		}	
		if(id==R.id.ly_weibo){
			// 设置QQ空间分享内容
			SinaShareContent sina = new SinaShareContent();
			sina.setShareImage(Image);
			sina.setShareContent("欢乐财神-weibo"+productDetailUrl);
			//sina.setTargetUrl(productDetailUrl);
			mController.setShareMedia(sina);
			mPlatform=SHARE_MEDIA.SINA;
		}
		shareThirdParty();


	}
	private void addCustomPlatforms() {
		// 添加微信平台
		addWXPlatform();
		// 添加QQ平台
		addQQQZonePlatform();
		//email
		addEmail();
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}
	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx7aaecca64230ee73";
		String appSecret = "0c79e9da4c19f88688356bb2a830a66e";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

	}
	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)context,
				appId, appKey);
		qqSsoHandler.setTargetUrl(productDetailUrl);
		qqSsoHandler.addToSocialSDK();

//		// 添加QZone平台
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity)context, appId, appKey);
//		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 添加Email平台</br>
	 */
	private void addEmail() {
		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
	}

	private void shareThirdParty(){
		mController.postShare(context, mPlatform, new SnsPostListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				String showText = "分享成功";
				if (eCode != StatusCode.ST_CODE_SUCCESSED) {
					showText = "分享失败 [" + eCode + "]";
				}
				Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();

			}
		});
	}	

	public void setCurrentView(int tag){
		if(tag==0){
			view.findViewById(R.id.ly_product).setVisibility(View.GONE);
		}
	}
	public void setProductId(String id){
		//productDetailUrl=Urls.getUrl+id;
	}
	public interface onPopDialog{
		void popDialog();     
	}
	private onPopDialog mOnPopDialog=null;
	public void setOnPopDialog (onPopDialog mOnPopDialog){
		this.mOnPopDialog=mOnPopDialog;
	}
	public interface onModifyProduct{
		void modifyProduct();     
	}
	private onModifyProduct monModifyProduct=null;
	public void setOnModifyProduct (onModifyProduct monModifyProduct){
		this.monModifyProduct=monModifyProduct;
	}

}
