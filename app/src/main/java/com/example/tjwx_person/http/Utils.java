package com.example.tjwx_person.http;



import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;

public class Utils {
	// 生成日志记录文件
	private static SimpleDateFormat simFor = new SimpleDateFormat("yyyyMMdd");
	private static String LOG_FILE = Environment.getExternalStorageDirectory()
			.getPath()
			+ "/travel/temp/log_"
			+ simFor.format(new Date())
			+ ".txt";
	private static String version = "";// 版本号

	public static void writeLog(String tag, String log) {
		// if (Constant.ISDEBUG) {
		File file = new File(LOG_FILE);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter out = null;
		try {
			// Create file
			FileWriter fstream = new FileWriter(LOG_FILE, true);
			out = new BufferedWriter(fstream);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss SSS ");
			String time = sdf.format(new Date(System.currentTimeMillis()));
			out.append(time);
			out.append(tag + "\t");
			out.append(log);
			out.append("\r\n\r\n");
		} catch (Exception e) {// Catch exception if any
			// System.err.println("Error: " + e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		// }
	}

	// 写文件
	public static void witeFile(String filePath, String content) {
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter out = null;
		try {
			// Create file 覆盖写入
			FileWriter fstream = new FileWriter(filePath);
			out = new BufferedWriter(fstream);
			out.append(content);
		} catch (Exception e) {// Catch exception if any
			// System.err.println("Error: " + e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	// 读文件
	public static String readFile(String filePath) {
		String result = "";
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				return null;
			}
			if (!file.exists()) {
				return null;
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] tem = toByteArray(fis);
			result = new String(tem);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 判断网络是否可用
	 */

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			// 如果仅仅是用来判断网络连接
			// 则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * inputString to byte
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	/**
	 * 获得版本号 versionCode
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int version = 0;
		try {
			ComponentName comp = new ComponentName(context, "");
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			version = pinfo.versionCode;
			System.out.println(version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获得版本 名versionName
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		if (!"".equals(version)) {
			return version;
		}
		// get current version
		try {
			ComponentName comp = new ComponentName(context, "");
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			version = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 
	 * @title: imageZoom
	 * @description: TODO(把图片压缩到指定大小，如这里的512kb)
	 * @param: @param bitMap
	 * @param: @return 设定文件
	 * @return: Bitmap 返回类型
	 * @throws
	 */
	public static Bitmap imageZoom(Bitmap bitMap) {
		// 图片允许最大空间 单位：KB
		double maxSize = 1024.00;
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
					bitMap.getHeight() / Math.sqrt(i));
		}
		return bitMap;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	private static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 
	 * @param date
	 * @return String long 时间戳转换成 规定字符串
	 */
	public static String formatDate(Long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String dat = format.format(date);
		return dat;
	}

	public static Date formateDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dat = null;
		try {
			dat = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dat;
	}

	public static String formatDay(Long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dat = format.format(date);
		return dat;
	}

	/**
	 * 生成二维码 要转换的地址或字符串,可以是中文
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 */
	// public static Bitmap createQRImage(String url, final int width, final int
	// height) {
	// try {
	// // 判断URL合法性
	// if (url == null || "".equals(url) || url.length() < 1) {
	// return null;
	// }
	// Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType,
	// String>();
	// hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	// // 图像数据转换，使用了矩阵转换
	// BitMatrix bitMatrix = new QRCodeWriter().encode(url,
	// BarcodeFormat.QR_CODE, width, height, hints);
	// int[] pixels = new int[width * height];
	// // 下面这里按照二维码的算法，逐个生成二维码的图片，
	// // 两个for循环是图片横列扫描的结果
	// for (int y = 0; y < height; y++) {
	// for (int x = 0; x < width; x++) {
	// if (bitMatrix.get(x, y)) {
	// pixels[y * width + x] = 0xff000000;
	// } else {
	// pixels[y * width + x] = 0xffffffff;
	// }
	// }
	// }
	// // 生成二维码图片的格式，使用ARGB_8888
	// Bitmap bitmap = Bitmap.createBitmap(width, height,
	// Bitmap.Config.ARGB_8888);
	// bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	// return bitmap;
	// } catch (WriterException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/*
	 * public static String descByCode(BaseEnity enity) { if (enity == null)
	 * return "未知错误"; if (!TextUtils.isEmpty(enity.msg)) return enity.msg;
	 * 
	 * switch (enity.code) { case -1: return "服务器返回协议解析失败"; case 100: return
	 * "手机号码不对"; case 101: return "动态密码是空的"; case 102: return "用户要获得动态密码"; case
	 * 103: return "提交动态密码和服务端保存的不一致"; case 201: return "权限不够"; case 202: return
	 * "用户不存在"; case 203: return "参数错误"; case 204: return "查询无结果"; case 205:
	 * return "服务端异常205"; case 500: return "服务端异常500"; case 206: return
	 * "该IMEI已经出库"; case 301: return "用户没有设定地市和门店信息"; case 403: return
	 * "3次没有消费手机短信密码而被禁止"; case 405: return "打卡不在有效位置范围内"; default: return
	 * "未知错误"; } }
	 */
}
