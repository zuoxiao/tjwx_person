package com.example.tjwx_person.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Theme {

	// 界面相关数值
	public static float m_Scale;

	private static int m_ScreenWidth;
	private static int m_ScreenHeight;

	/**
	 * 前景
	 */
	public static int colorAllBg = 0xffEEEEEE; // 整个界面的背景
	public static int colorAllBgBlue = 0xff0085d0; // 整个界面的背景

	public static int colorTitleBgBlue = 0xff0085d0; // 蓝色按钮背景
	public static int colorTitleBgGreen = 0xff0194dc; // 灰色标题条的背景
	public static int colorTitleWhite = 0xffffffff; // 灰色标题条的文字颜色

	public static int colorTitleBg = 0xffF8F8F8; // 灰色标题条的背景
	public static int colorTitle = 0xff0194dc; // 灰色标题条的文字颜色

	public static int colorMainBg = 0xffffffff; // 文本显示区域的白色背景
	public static int colorMain = 0xff333333; // 文本显示区域主文本颜色:黑色
	public static int colorContent = 0xff959595; // 文本显示区域辅助文本颜色：灰色

	public static int colorHint = 0xff0194dc; // 用于一些地方的绿色提示文字，改为蓝色
	public static int colorBlue = 0xff0084d0; // 用于一些地方的蓝色提示文字
	public static int colorStroke = Color.LTGRAY; // 线条颜色
	public static int colorHintGreen = 0xff0194dc; // 改为蓝色
	public static int colorHintOrange = 0xffff9c28;

	public static int colorDialogTitleBg = 0xff0194dc; // 对话框标题栏背景颜色
	public static int colorTextBg = 0xff0194dc; // 文本字体颜色
	public static int colorButtonBg = 0xff0194dc; // 按钮背景颜色

	/**
	 * 大小
	 */
	public static int sizeTitleButton; // 主要用于大按钮中的文字
	public static int sizeMain; // 主要用于内容区域中的大字体部分
	public static int sizeContent; // 主要用于内容区域中的小字体部分
	public static int sizeHint; // 主要用于一些辅助显示的小字体

	public static int[] pix;
	public static int size60;
	public static int size40;
	public static int size36;
	public static int size32;
	public static int size30;
	public static int size28;
	public static int size26;
	public static int size24;
	public static int size22;
	public static int size20;
	public static int size18;
	public static int size16;
	public static int size14;
	public static int size12;

	public static int heightButton; // 界面中超大按钮的高度
	public static int sizeIcon; // 列表中每一单项中的图标大小

	public static int title_big;// 标题大字
	public static int title_small;// 标题小字
	/**
	 * 边距
	 */
	public static int padding; // 界面中控件内部的留白
	public static int margin0; // 720屏幕控件外部的留白
	public static int margin; // 界面中控件外部的留白
	public static float radii; // 用代码画圆角背景时圆角的大小

	/**
	 * 业务专用的可以定义在这下面
	 */

	public static int colorWhite = 0xffffffff;// 白色字
	public static int color666666 = 0xff666666;// 精彩用量颜色

	public static void init(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		m_ScreenWidth = dm.widthPixels;
		m_ScreenHeight = dm.heightPixels;

		if (m_ScreenWidth > m_ScreenHeight) {
			// 保证屏幕宽度小于高度
			int tmp = m_ScreenWidth;
			m_ScreenWidth = m_ScreenHeight;
			m_ScreenHeight = tmp;
		}
		m_Scale = context.getResources().getDisplayMetrics().density;

		pix = new int[720];
		for (int i = 0; i < pix.length; ++i)
			pix[i] = pix2(i);

		sizeTitleButton = pix(40);
		sizeMain = pix(28);
		sizeContent = pix(24);
		sizeHint = pix(24);

		size60 = pix(60);
		size40 = pix(40);
		size36 = pix(36);
		size32 = pix(32);
		size30 = pix(30);
		size28 = pix(28);
		size26 = pix(26);
		size24 = pix(24);
		size22 = pix(22);
		size20 = pix(20);
		size18 = pix(18);
		size16 = pix(16);
		size14 = pix(14);
		size12 = pix(12);

		heightButton = pix(76);
		sizeIcon = pix(100);
		padding = pix(10);
		margin0 = 12;
		margin = pix(margin0);
		radii = 10;

		title_big = pix(40);
		title_small = pix(35);
	}

	public static int px2dip(float pxValue) {
		return (int) (pxValue / m_Scale + 0.5f);
	}

	public static int dip2px(float dpValue) {
		return (int) (dpValue * m_Scale + 0.5f);
	}

	public static final int pix(int px) {
		// return px * m_ScreenWidth / 720;
		if (px > 720)
			return pix2(px);
		else
			return pix[px];
	}

	private static final int pix2(int px) {
		return px * m_ScreenWidth / 720;
	}

	public static int getScreenWidth(Context context) {
		if (m_ScreenWidth == 0)
			init(context);
		return m_ScreenWidth;
	}

	public static int getScreenHeight(Context context) {
		if (m_ScreenHeight == 0)
			init(context);
		return m_ScreenHeight;
	}

	public static int getTitleHeight(Context context) {
		if (m_ScreenWidth == 0)
			init(context);
		return pix[100];
	}

	public static int getTabHeight(Context context) {
		if (m_ScreenWidth == 0)
			init(context);

		int height;
		height = 100;

		return m_ScreenWidth * height / 180 / 4;
	}

	public static void drawBgMain(View view, int color, boolean round,
			boolean stroke) {
		GradientDrawable d;
		d = new GradientDrawable();
		d.setColor(color);
		if (round)
			d.setCornerRadius(Theme.radii);
		if (stroke)
			d.setStroke(1, Theme.colorStroke);

		view.setBackgroundDrawable(d);
	}

	public static void drawBgMain(View view, int color, boolean round,
			boolean stroke, int strokeColor) {
		GradientDrawable d;
		d = new GradientDrawable();
		d.setColor(color);
		if (round)
			d.setCornerRadius(Theme.radii);
		if (stroke)
			d.setStroke(1, strokeColor);

		view.setBackgroundDrawable(d);
	}

	public static void drawButton(View view, int color, boolean stroke) {
		GradientDrawable d;
		d = new GradientDrawable();
		d.setColor(color);
		d.setCornerRadius(45);
		if (stroke)
			d.setStroke(1, Theme.colorStroke);

		view.setBackgroundDrawable(d);
	}

	public static void drawButton(View view, int color, boolean stroke,
			int strokeColor) {
		GradientDrawable d;
		d = new GradientDrawable();
		d.setColor(color);
		d.setCornerRadius(45);
		if (stroke)
			d.setStroke(1, strokeColor);

		view.setBackgroundDrawable(d);
	}

	public static void drawBgMain(View view, int color, float r0, float r1,
			float r2, float r3, int strokeColor, int strokeSize) {
		GradientDrawable d;
		d = new GradientDrawable();
		d.setColor(color);
		d.setCornerRadii(new float[] { r0, r0, r1, r1, r2, r2, r3, r3 });
		if (strokeColor != Color.TRANSPARENT)
			d.setStroke(strokeSize, strokeColor);

		view.setBackgroundDrawable(d);
	}

	public static int errorXML = -1;

	public static void setViewSize(View v, int w, int h) {
		ViewGroup.LayoutParams lpm = v.getLayoutParams();
		lpm.height = h;
		lpm.width = w;
		v.setLayoutParams(lpm);
	}

	public static void setViewMargin(View v, int left, int top, int right,
			int bottom) {
		ViewGroup.MarginLayoutParams lpm = (MarginLayoutParams) v
				.getLayoutParams();
		lpm.leftMargin = left;
		lpm.rightMargin = right;
		lpm.topMargin = top;
		lpm.bottomMargin = bottom;
		v.setLayoutParams(lpm);
	}

	public static void setViewLeftMargin(View v, int left) {
		ViewGroup.MarginLayoutParams lpm = (MarginLayoutParams) v
				.getLayoutParams();
		lpm.leftMargin = left;
		v.setLayoutParams(lpm);
	}

	public static void setViewRightMargin(View v, int right) {
		ViewGroup.MarginLayoutParams lpm = (MarginLayoutParams) v
				.getLayoutParams();
		lpm.rightMargin = right;
		v.setLayoutParams(lpm);
	}

	public static void setViewTopMargin(View v, int top) {
		ViewGroup.MarginLayoutParams lpm = (MarginLayoutParams) v
				.getLayoutParams();
		lpm.topMargin = top;
		v.setLayoutParams(lpm);
	}

	public static void setViewBottomMargin(View v, int bottom) {
		ViewGroup.MarginLayoutParams lpm = (MarginLayoutParams) v
				.getLayoutParams();
		lpm.bottomMargin = bottom;
		v.setLayoutParams(lpm);
	}

	public static void setTextSize(TextView v, int size) {
		v.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
	}

	public static void setTextSize(Button v, int size) {
		v.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
	}

}
