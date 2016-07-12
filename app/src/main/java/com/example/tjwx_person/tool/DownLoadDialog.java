package com.example.tjwx_person.tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.cxcl.property.customer.Application;
import com.cxcl.property.customer.R;

public class DownLoadDialog {

	private TextView cancel, textData;
	private ProgressBar mProgressBar;
	private Context context;
	// private MyApplication app;
	private boolean isDestroy = true;
	private boolean isBinded;
	private DownloadService.DownloadBinder binder;
	Activity activity;
	String Url;

	private Dialog dialog = null;

	Application ismust;

	public Dialog alertDialog(final Context context,Activity acticity) {
		this.context = context;
		dialog = new Dialog(context, R.style.FullHeightDialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		ismust=(Application) acticity.getApplication();
		View view;

		view = inflater.inflate(R.layout.download_dialog, null);
		cancel = (TextView) view.findViewById(R.id.download_cancel);
		textData = (TextView) view.findViewById(R.id.dowload_text);
		mProgressBar = (ProgressBar) view.findViewById(R.id.downliad_progress);

		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(keylistener);
		startService();
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				isDestroy = false;
				binder.cancel();
				binder.cancelNotification();

				if (isBinded) {
					System.out.println(" onDestroy   unbindservice");
					context.unbindService(conn);
				}
				if (binder != null && binder.isCanceled()) {
					System.out.println(" onDestroy  stopservice");
					Intent it = new Intent(context, DownloadService.class);
					context.stopService(it);
				}

			}
		});

		return dialog;
	}

	OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};

	public void startService() {
		if (isDestroy) {
			Intent it = new Intent(context, DownloadService.class);
			it.putExtra("apkUrl", Url);
			context.startService(it);
			context.bindService(it, conn, Context.BIND_AUTO_CREATE);
		}
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBinded = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder = (DownloadService.DownloadBinder) service;
			System.out.println("服务启动!!!");
			// 开始下载
			isBinded = true;
			binder.addCallback(callback);
			binder.start();

		}
	};

	public ICallbackResult callback = new ICallbackResult() {

		@Override
		public void OnBackResult(Object result) {
			// TODO Auto-generated method stub
			if ("finish".equals(result)) {
				dialog.dismiss();
				return;
			}
			int i = (Integer) result;
			mProgressBar.setProgress(i);
			// tv_progress.setText("当前进度 =>  "+i+"%");
			// tv_progress.postInvalidate();
			mHandler.sendEmptyMessage(i);
		}

	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			textData.setText("当前进度 ： " + msg.what + "%");

		};
	};

	public interface ICallbackResult {
		public void OnBackResult(Object result);
	}





}
