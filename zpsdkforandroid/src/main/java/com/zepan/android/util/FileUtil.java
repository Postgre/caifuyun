package com.zepan.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Base64;
import android.util.Log;

public class FileUtil {
	public static final String TAG = "FileUtil";

	public static void createFile(String dir, String filename) throws Exception {

		// 判断是否有SD卡
		// String state = Environment.getExternalStorageState();
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(dir, filename);
		if (!file.exists()) {
			try {
				Log.i(TAG, "正在创建文件……");
				file.createNewFile();
			} catch (IOException e) {
				throw e;
			}
		}
		Log.i(TAG, "文件创建成功");
	}

	public static void createFile(String filename) throws Exception {

		File file = new File(filename);
		if (!file.exists()) {
			try {
				Log.i(TAG, "正在创建文件……");
				file.createNewFile();
			} catch (IOException e) {
				throw e;
			}
		}
		Log.i(TAG, "文件创建成功");
	}

	/**
	 * 获取本地图片
	 * 
	 * @param filepath
	 *            本地文件路径
	 * @param inSampleSize
	 *            图片缩放比例
	 * 
	 * */
	public static Bitmap getBitmapFromLocal(String filepath, int inSampleSize) {
		if (filepath == null) {
			return null;
		}
		// 取本地图片
		File file = new File(filepath);
		if (file != null && file.exists()) {
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = inSampleSize;
			// option.inJustDecodeBounds = true;
			Bitmap photo = BitmapFactory.decodeFile(file.getPath(), option);
			// option.inJustDecodeBounds = false;
			return photo;
		}
		return null;
	}

	public static Bitmap getBitmapFromLocal(File file, int inSampleSize) {
		if (file != null && file.exists()) {
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = inSampleSize;
			// option.inJustDecodeBounds = true;
			Bitmap photo = BitmapFactory.decodeFile(file.getPath(), option);
			// option.inJustDecodeBounds = false;
			return photo;
		}
		return null;
	}

	public static Bitmap getBitmap(InputStream is, int inSampleSize) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = inSampleSize;
		Bitmap photo = BitmapFactory.decodeStream(is, null, option);
		return photo;
	}

	public static void saveBitmapToSd(Bitmap bitmap, String dir, String path) {
		try {
			File file = new File(dir, path);
			if (!file.exists()) {
				createFile(dir, path);
			}

			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			Log.i(TAG, "图片保存成功！");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void saveBitmapToSd(Bitmap bitmap, File file) {
		try {
			if (file == null) {
				return;
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			Log.i(TAG, "图片保存成功！");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean deleteBitmapFromSd(String dirpath, String filename) {
		File file = new File(dirpath, filename);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串

		String string = null;

		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();

		string = Base64.encodeToString(bytes, Base64.DEFAULT);

		return string;

	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 将图片转化为圆角图片
	 * 
	 * @param bitmap待转化图片对象
	 * */
	public static Bitmap toRoundCorner(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int d = bitmap.getWidth();
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(d, d, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(d / 2, d / 2, d / 2, paint);
		/**
		 * 使用SRC_IN
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return target;
	}

	/**
	 * 删除文件夹下所有文件
	 * 
	 * */
	public static void deleteDirFiles(String dir) {
		File file = new File(dir);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] childFiles = file.listFiles();
				for (File f : childFiles) {
					deleteDirFiles(f.getPath());
				}
			} else {
				file.delete();
			}
		}
	}

	public static Bitmap revitionImageSize(Bitmap bm, int size)
			throws IOException {
		if (bm == null) {
			return null;
		}
		// 取得图片
		InputStream temp = bitmapToStream(bm);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		temp.close();

		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= size)
					&& (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp.reset();
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(temp, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static InputStream bitmapToStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}

	/** 图片压缩 */
	public static Bitmap compressBitmap(Bitmap bm, int compressSize) {
		try {
			return FileUtil.revitionImageSize(bm, compressSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bm != null && !bm.isRecycled()) {
			bm.recycle();
		}
		return null;
	}

	/**
	 * 获取文件大小 TODO
	 * 
	 * @param 路径
	 *            ，或者文件都支持
	 * @return
	 */
	public static long getFileSize(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 
	 * TODO 获取格式化后的文件大小
	 * 
	 * @param FileSize
	 * @return String(格式化后的文件大小)
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";

		if (fileS == 0) {
			fileSizeString = "0B";
		} else if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/*** 获取文件个数 ***/
	public long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
}
