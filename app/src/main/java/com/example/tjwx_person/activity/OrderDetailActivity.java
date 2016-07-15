package com.example.tjwx_person.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.adapter.Order_imgAdapter;
import com.example.tjwx_person.bean.OrderData;
import com.example.tjwx_person.tool.voice.SoundMeter;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.CrashHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * 详细信息（图片、描述、录音）
 *
 * @author zuo
 */
public class OrderDetailActivity extends BaseActivity {

    /**
     * 录音
     */
    private SoundMeter mSensor;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort, voice_isNo;
    private TextView voice_time;
    private LinearLayout del_re;
    private View rcChat_popup;
    private int flag = 1;
    private boolean isShosrt = false;
    private Context mContext;

    // private long startVoiceT, endVoiceT;
    private String voiceName;
    private ImageView volume;
    private Handler mHandler = new Handler();
    TimeCount time;
    int timeData;
    boolean isfinish;// 倒计时和抬起手势在倒计时到时间以后假如抬起手会再次发送
    boolean isfirstTime = false;// 处理多次点击录音倒计时不正确问题
    boolean isstartTime = false;

    /**
     * 照片
     */
    public static int imgNumber = 0;// 当前第几个图片
    Uri uritempFile;//剪切以后返回的url
    Order_imgAdapter imgAdapter;
    private PopupWindow popWindow;// 选择照片方式
    private PopupWindow popDelect;// 选择照片方式
    private String photoSaveName;// 图片名
    private String photoSavePath;// 保存路径
    private TextView photograph, albums, delect;// popwindow中的相册，拍照选项
    private LinearLayout cancel;// popwindow中的取消选项
    public static final int PHOTOZOOM = 8; // 拍照或相册选择后结果
    private static final int PHOTOTAKE = 7;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private int position;//删除第几张图片
    /**
     * 播放录音
     */
    private MediaPlayer mediaPlayer; /* 播放器 */
    boolean isStartPlaying = false;
    /**
     * 上传参数
     */
    String textData;
    String voiceData = null;
    private ArrayList<String> listImg = new ArrayList<String>();
    OrderData orderData = new OrderData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        addContentView(R.layout.activity_order_details, 1, "维修附加信息填写");
        initView();
        if (getIntent().getSerializableExtra("orderData") != null) {
            orderData = (OrderData) getIntent().getSerializableExtra(
                    "orderData");
            initViewData();
        }
    }

    private void initViewData() {
        if (orderData.getImgNumber() != 0) {
            listImg.clear();
            imgNumber = orderData.getImgNumber();
        }

        textData = orderData.getComment();
        if (textData != null && !"".equals(textData)) {
            orderDetails_text_text.setText("文字描述（"
                    + (String.valueOf(textData.length())) + "/50）");
            orderDetails_text.setText(textData);
        }
        orderDetails_img_text.setText("物品照片（" + imgNumber + "/3)");
        voice_text.setText(String.valueOf(orderData.getTimeData()));
        if (orderData.getPictures() != null) {
            listImg.addAll(orderData.getPictures());
            // if (imgNumber <= 2) {
            // String drawableUrl = "drawable://" + R.drawable.order_img_add;
            // listImg.add(drawableUrl);
            // }
        }

        imgAdapter.notifyDataSetChanged();
        voiceData = orderData.getAudio();
        if (voiceData != null) {
            orderDetails_voice_text.setText("语音说明（1/1）");
            order_voice_text.setVisibility(View.GONE);
            voice_text.setVisibility(View.VISIBLE);
            voice_img.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();

    }

    private TextView orderDetails_img_text, orderDetails_text_text,
            orderDetails_voice_text, voice_text;
    private TextView voice_btn;
    private TextView orderDetail_ok, order_voice_text;
    private GridView orderDetails_gridView;
    private ImageView voice_img;
    private EditText orderDetails_text;
    private ScrollView order_scroll;

    private void initView() {
        imgNumber = 0;
        voice_rcd_hint_rcding = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_rcding);
        voice_rcd_hint_loading = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_loading);
        voice_rcd_hint_tooshort = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_tooshort);
        voice_isNo = (LinearLayout) this.findViewById(R.id.voice_isNo);
        voice_time = (TextView) findViewById(R.id.voice_time);
        volume = (ImageView) this.findViewById(R.id.volume);
        del_re = (LinearLayout) this.findViewById(R.id.del_re);
        order_scroll = (ScrollView) findViewById(R.id.order_scroll);
        mSensor = new SoundMeter(this);
        orderDetails_text = (EditText) findViewById(R.id.orderDetails_text);
        rcChat_popup = this.findViewById(R.id.rcChat_popup);
        orderDetail_ok = (TextView) findViewById(R.id.orderDetail_ok);
        orderDetails_img_text = (TextView) findViewById(R.id.orderDetails_img_text);
        orderDetails_text_text = (TextView) findViewById(R.id.orderDetails_text_text);
        orderDetails_voice_text = (TextView) findViewById(R.id.orderDetails_voice_text);
        voice_btn = (TextView) findViewById(R.id.voice_btn);
        voice_btn.setOnTouchListener(onTouchlisten);
        voice_text = (TextView) findViewById(R.id.voice_text);
        orderDetails_gridView = (GridView) findViewById(R.id.orderDetails_gridView);
        voice_img = (ImageView) findViewById(R.id.voice_img);
        order_voice_text = (TextView) findViewById(R.id.order_voice_text);
        orderDetail_ok.setOnClickListener(this);
        voice_img.setOnClickListener(this);
        String drawableUrl = "drawable://" + R.drawable.order_img_add;
        listImg.add(drawableUrl);
        imgAdapter = new Order_imgAdapter(mContext, listImg);
        orderDetails_gridView.setAdapter(imgAdapter);
        // orderDetails_gridView.measure(0, 0);
        // int height = orderDetails_gridView.getMeasuredHeight();
        // LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
        // height);
        // orderDetails_gridView.setLayoutParams(params);

        orderDetails_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textData = s.toString();
                orderDetails_text_text.setText("文字描述（"
                        + (String.valueOf(textData.length())) + "/50）");

            }
        });

        orderDetails_gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("position1", position + "");
                if (position == imgNumber && imgNumber < 3) {
                    showPopupWindow(orderDetails_gridView);

                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File removeImg = new File(listImg.get(position).replace("file://", ""));
                    intent.setDataAndType(Uri.fromFile(removeImg), "image/*");
                    startActivity(intent);
                }
            }
        });
        orderDetails_gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                OrderDetailActivity.this.position = position;
                if (position == imgNumber && imgNumber < 3) {
                } else {
                    showPopupDelect(orderDetails_gridView);
                }

                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.orderDetail_ok:

                Intent detail = new Intent();
                orderData.setAudio(voiceData);
                orderData.setComment(textData);
                if (imgNumber == 0) {
                    orderData.setPictures(null);
                    orderData.setImgNumber(imgNumber);
                } else {
                    orderData.setPictures(listImg);
                    orderData.setImgNumber(imgNumber);
                }

                orderData.setTimeData(timeData);
                detail.putExtra("orderData", orderData);
                setResult(3, detail);
                this.finish();
                break;
            case R.id.voice_img:
                if (voiceData != null && !"".equals("voiceData")) {
                    if (isStartPlaying) {
                        stopVideo();
                    } else {
                        playVideo();
                    }

                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        String path = "";
        String imageUrl = "";
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case PHOTOZOOM:// 从相册中选择
                try {

                    openPhotoZoom(this, PHOTO_REQUEST_CUT, data == null ? null : data.getData());
                } catch (IllegalArgumentException e) {
                    Log.e("选择图片失败", e.getMessage());
                }

                break;
            case PHOTOTAKE:// 拍照后返回的结果
                try {

                    openPhotoZoom(this, PHOTO_REQUEST_CUT, imageCaptureUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PHOTO_REQUEST_CUT:
                try {
                    if (resultCode != -1) {
                        return;
                    }


                    try {
                        Bitmap photo = null;
                        photo = getCropedBitmap(this, false);

                        String uuid2 = UUID.randomUUID().toString().replace("-", "");
                        //  path = photoSavePath + photoSaveName;
                        if (photoSavePath != null && !"".equals(photoSavePath)) {
                            photoSavePath = Environment.getExternalStorageDirectory() + "/" + "WX" + "/";
                        }
                        File myCaptureFile = new File(photoSavePath + uuid2
                                + ".jpg");

                        try {
                            if (myCaptureFile != null && !myCaptureFile.exists()) {

                                File folderPath = new File(photoSavePath);
                                if (!folderPath.exists()) {
                                    folderPath.mkdirs();
                                }
                                myCaptureFile.delete();
                            }
                            BufferedOutputStream bos = new BufferedOutputStream(
                                    new FileOutputStream(myCaptureFile));
                            /* 采用压缩转档方法 */
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            /* 调用flush()方法，更新BufferStream */
                            bos.flush();
                            /* 结束OutputStream */
                            bos.close();

                            FileInputStream inputFile = new FileInputStream(
                                    myCaptureFile);
                            byte[] buffer = new byte[(int) myCaptureFile.length()];
                            inputFile.read(buffer);
                            inputFile.close();
                            listImg.remove(imgNumber);
                            imageUrl = "file://" + photoSavePath + uuid2
                                    + ".jpg";
                            // String imageUrl = photoSavePath + uuid2 + ".jpg";
                            listImg.add(imageUrl);
                            if (imgNumber < 2) {
                                String drawableUrl2 = "drawable://"
                                        + R.drawable.order_img_add;
                                listImg.add(drawableUrl2);

                            }
                            if (imgNumber < 3) {
                                imgNumber++;
                            }

                            imgAdapter.notifyDataSetChanged();
                            orderDetails_img_text.setText("物品照片（" + imgNumber
                                    + "/3)");


                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            CrashHandler crash = CrashHandler.getInstance();
                            crash.saveCrashInfo2File(e);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            CrashHandler crash = CrashHandler.getInstance();
                            crash.saveCrashInfo2File(e);
                        } finally {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CrashHandler crash = CrashHandler.getInstance();
                        crash.saveCrashInfo2File(e);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        try {
            Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(imageUrl)));
            OrderDetailActivity.this.sendBroadcast(media);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * 显示popwindow的方法
     *
     * @param parent 触发popwindow显示的控件
     */
    @SuppressWarnings("deprecation")
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
            popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, true);
            photoSavePath = Environment.getExternalStorageDirectory() + "/" + "WX" + "/";
            // photoSavePath="sdcard/bx/";
            photoSaveName = "myHeadImage.png";
            initPop(view);
        }
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    private void showPopupDelect(View parent) {

        if (popDelect == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.pop_delete_photo, null);
            popDelect = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, true);
            delect = (TextView) view.findViewById(R.id.delect);// 删除
            cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取消
            delect.setOnClickListener(new OnClickListener() {// 点击popwindow中的拍照的响应事件
                @Override
                public void onClick(View arg0) {
                    popDelect.dismiss();
                    try {
                        File removeImg = new File(listImg.get(position).replace("file://", ""));
                        if (removeImg.exists()) {
                            removeImg.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imgNumber = imgNumber - 1;
                    listImg.remove(position);
                    String drawableUrl = "drawable://" + R.drawable.order_img_add;

                    if (imgNumber == 2) {
                        listImg.add(drawableUrl);

                    }
                    orderDetails_img_text.setText("物品照片（" + imgNumber
                            + "/3)");
                    imgAdapter.notifyDataSetChanged();
                    Log.d("position2", position + "");
                    for (int i = 0; i < listImg.size(); i++) {
                        Log.d("picture", listImg.get(i).toString());
                    }
                }
            });
            cancel.setOnClickListener(new OnClickListener() { // 触发popwindow中取消按钮时的响应事件
                @Override
                public void onClick(View arg0) {
                    popDelect.dismiss();

                }
            });
        }
        popDelect.setAnimationStyle(android.R.style.Animation_InputMethod);
        popDelect.setFocusable(true);
        popDelect.setOutsideTouchable(true);
        popDelect.setBackgroundDrawable(new BitmapDrawable());
        popDelect
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popDelect.showAtLocation(parent, Gravity.CENTER, 0, 0);

    }

    /**
     * 初始化popwindow中的控件
     *
     * @param view popwindow的布局
     */
    public void initPop(View view) {
        photograph = (TextView) view.findViewById(R.id.photograph);// 拍照
        albums = (TextView) view.findViewById(R.id.albums);// 相册
        cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取消

        photograph.setOnClickListener(new OnClickListener() {// 点击popwindow中的拍照的响应事件
            @Override
            public void onClick(View arg0) {

                popWindow.dismiss();
                pickImageFromCamera(PHOTOTAKE);
            }
        });
        albums.setOnClickListener(new OnClickListener() { // 触发popwindow中本地上传(相册)时的响应事件
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();


                pickImageFromGallery(PHOTOZOOM);
            }
        });

        // 触发popwindow中取消按钮时的响应事件
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();

            }
        });
    }

    /**
     * 语音功能
     */

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            voice_btn.setBackgroundResource(R.drawable.text_fillet_bg);
            isfinish = true;
            voice_text.setText(timeData + "");
            time = null;

            voice_rcd_hint_rcding.setVisibility(View.GONE);
            voice_time.setVisibility(View.GONE);
            stop();
            flag = 1;
            // int time = (int) ((endVoiceT - startVoiceT) / 1000);
            Log.d("time", String.valueOf(time));
            if (timeData < 1) {
                isShosrt = true;
                voice_rcd_hint_loading.setVisibility(View.GONE);
                voice_rcd_hint_rcding.setVisibility(View.GONE);
                voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        voice_rcd_hint_tooshort.setVisibility(View.GONE);
                        rcChat_popup.setVisibility(View.GONE);
                        isShosrt = false;
                    }
                }, 500);

            } else {
                voiceData = Environment.getExternalStorageDirectory() + "/"
                        + voiceName;
                orderDetails_voice_text.setText("语音说明（1/1）");

            }

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            timeData = (int) (60 - (millisUntilFinished / 1000));
            if (millisUntilFinished / 1000 < 20) {
                voice_time.setVisibility(View.VISIBLE);
                voice_time.setText(String.valueOf(millisUntilFinished / 1000)
                        + "秒");

            }

        }
    }

    // 按下语音录制按钮时
    OnTouchListener onTouchlisten = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (!Environment.getExternalStorageDirectory().exists()) {

                return false;
            }

            order_scroll.requestDisallowInterceptTouchEvent(true);
            System.out.println("1");
            int[] location = new int[2];
            voice_btn.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
            int btn_rc_Y = location[1];
            int btn_rc_X = location[0];
            int[] del_location = new int[2];
            del_re.getLocationInWindow(del_location);
            int del_Y = del_location[1];
            int del_x = del_location[0];
            if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                if (!Environment.getExternalStorageDirectory().exists()) {

                    return false;
                }
                System.out.println("2");

                if (event.getRawY() > btn_rc_Y && event.getRawX() > btn_rc_X) {// 判断手势按下的位置是否是语音录制按钮的范围内
                    System.out.println("3");
                    if (time == null) {
                        time = new TimeCount(60 * 1000, 50);
                        time.start();
                        if (voiceData != null) {
                            File file = new File(voiceData);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                    }
                    if (time == null) {

                        time = new TimeCount(60 * 1000, 500);
                        time.start();
                        timeData = 0;
                    }
                    isstartTime = true;
                    voice_btn
                            .setBackgroundResource(R.drawable.text_fillet_deep_bg);
                    isfinish = false;
                    rcChat_popup.setVisibility(View.VISIBLE);
                    voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    voice_rcd_hint_tooshort.setVisibility(View.GONE);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (!isShosrt) {
                                voice_rcd_hint_loading.setVisibility(View.GONE);
                                voice_rcd_hint_rcding
                                        .setVisibility(View.VISIBLE);
                            }
                        }
                    }, 300);

                    del_re.setVisibility(View.GONE);
                    long startVoiceT = SystemClock.currentThreadTimeMillis();
                    voiceName = startVoiceT + ".amr";
                    start(voiceName);
                    flag = 2;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// 松开手势时执行录制完成
                isfirstTime = true;
                isstartTime = false;
                time.cancel();
                voice_btn.setBackgroundResource(R.drawable.text_fillet_bg);
                System.out.println("4");
                if (!isfinish) {// 假如倒计时完成已发送一遍就不再进行发送
                    if (event.getRawY() < btn_rc_Y
                            || event.getRawX() < btn_rc_X) {

                        stop();
                        flag = 1;
                        File file = new File(
                                android.os.Environment
                                        .getExternalStorageDirectory()
                                        + "/"
                                        + voiceName);
                        if (file.exists()) {
                            file.delete();
                        }
                        voice_rcd_hint_loading.setVisibility(View.GONE);
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        voice_rcd_hint_tooshort.setVisibility(View.GONE);
                        del_re.setVisibility(View.GONE);
                    } else {

                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        stop();
                        // endVoiceT = SystemClock.currentThreadTimeMillis();
                        flag = 1;
                        // int time = (int) ((endVoiceT - startVoiceT) / 1000);
                        if (timeData < 1) {
                            isShosrt = true;
                            voice_rcd_hint_loading.setVisibility(View.GONE);
                            voice_rcd_hint_rcding.setVisibility(View.GONE);
                            voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    voice_rcd_hint_tooshort
                                            .setVisibility(View.GONE);
                                    rcChat_popup.setVisibility(View.GONE);
                                    isShosrt = false;
                                }
                            }, 500);

                            time.cancel();
                            time = null;
                            return false;
                        } else {
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                                }
                            }, 300);
                            voiceData = Environment
                                    .getExternalStorageDirectory()
                                    + "/"
                                    + voiceName;
                            order_voice_text.setVisibility(View.GONE);
                            voice_text.setVisibility(View.VISIBLE);
                            voice_img.setVisibility(View.VISIBLE);
                            voice_text.setText(timeData + "");
                            orderDetails_voice_text.setText("语音说明（1/1）");
                            time.cancel();
                            time = null;
                        }

                    }
                }
            }
            if (event.getRawY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内

                del_re.setVisibility(View.VISIBLE);
                voice_isNo.setVisibility(View.GONE);
                voice_time.setVisibility(View.GONE);
                if (event.getRawY() >= del_Y
                        && event.getRawY() <= del_Y + del_re.getHeight()
                        && event.getRawX() >= del_x
                        && event.getRawX() <= del_x + del_re.getWidth()) {

                }
            } else {

                del_re.setVisibility(View.GONE);
                voice_isNo.setVisibility(View.VISIBLE);

            }

            return true;
        }
    };
    private static final int POLL_INTERVAL = 300;
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    private void start(String name) {
        mSensor.start(name);

        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();

        volume.setImageResource(R.drawable.amp1);
    }

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.amp2);

                break;
            case 4:
            case 5:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp6);
                break;
        }
    }

    /**
     * 播放录音
     */

    private void playVideo() {
        if (isStartPlaying) {
            mediaPlayer.stop();
        } else {

            voice_img.setImageResource(R.drawable.voice_big_play);
            mediaPlayer = new MediaPlayer(); /* 创建 MediaPlayer 对象 */
            mediaPlayer.setAudioStreamType(2); /* 设置播放音量 */
            /* 设置 MediaPlayer 错误监听器, 如果出现错误就会回调该方法打印错误代码 */
            mediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer arg0, int what, int extra) {
                    System.out.println("MediaPlayer 出现错误 what : " + what
                            + " , extra : " + extra);
                    return false;
                }
            });
            /* 设置播放完毕监听器 */
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer arg0) {

                    voice_img.setImageResource(R.drawable.voice_big);
                }
            });
            /* 设置准备完毕监听器 */
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer arg0) {
                    System.out.println("准备完毕");
                    /* 设置播放状态 */
                    mediaPlayer.start();

                }
            });
            try {
                mediaPlayer.setDataSource(voiceData);
                mediaPlayer.prepare();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

			/* 设置 MediaPlayer 开始播放标识为 true */
            isStartPlaying = true;
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            stopVideo();
        super.onDestroy();
    }

    private void stopVideo() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        isStartPlaying = false;

    }

    private Uri imageCaptureUri;
    private static final String CROP_CACHE_FILE_NAME = "crop_cache_file.jpg";
    private static final Uri CACHE_FILE_URI = Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon()
            .appendPath(CROP_CACHE_FILE_NAME).build();
    private static final String CROP_CACHE_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator
            + CROP_CACHE_FILE_NAME;

    public void pickImageFromCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        File tempfile = new File(createPhotoPath());
        if (tempfile.isFile()) {
            tempfile.delete();
        }
        imageCaptureUri = Uri.fromFile(tempfile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {

        }
    }

    public String createPhotoPath() {
        File datalDir = Environment.getExternalStorageDirectory();
        File myDir = new File(datalDir, "/DCIM/Camera");
        myDir.mkdirs();
        String directoryname = datalDir.toString() + "/DCIM/Camera";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hhmmss", Locale.SIMPLIFIED_CHINESE);

        String photoPath = directoryname + "IMG_" + sdf.format(new Date()) + ".jpg";
        return photoPath;
    }

    public void pickImageFromGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(CROP_TYPE);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {

        }
    }

    public static Uri converToContentUri(Context context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            if (!isMediaDocument(uri)) {
                return uri;
            }

            final String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if (!"image".equals(split[0])) {
                return uri;
            }
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Uri.parse(Environment.getExternalStorageDirectory() + "/"
                            + split[1]);
                }
            }
            if(isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return Uri.parse(getDataColumn(context, contentUri, null, null));
            }

            uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, split[1]);

            return uri;
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getEncodedPath();
            if (path == null) {
                return uri;
            }
            path = Uri.decode(path);
            ContentResolver cr = context.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
            Cursor cursor = null;
            try {
                cursor =
                        cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID},
                                buff.toString(), null, null);
                if (cursor.moveToFirst()) {
                    int index = cursor.getInt(0);
                    uri = Uri.parse("content://media/external/images/media/" + index);
                }
                return uri;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return uri;
    }
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public Bitmap getCropedBitmap(Context context, boolean clear) {
        Bitmap tempPhoto = null;
        InputStream inputStream = getCacheFileInputStream(context);

        tempPhoto = BitmapFactory.decodeStream(inputStream);
        if (clear) {
            clearCache();
        }
        return tempPhoto;
    }

    public static InputStream getCacheFileInputStream(Context context) {
        try {
            return context.getContentResolver().openInputStream(CACHE_FILE_URI);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void clearCache() {
        File cache = new File(CROP_CACHE_FILE_PATH);
        cache.delete();
        if (imageCaptureUri != null) {
            cache = new File(imageCaptureUri.getPath());
            cache.delete();
        }
    }

    public static void openPhotoZoom(Activity activity, int requestCode, Uri url) {
        Uri uri = url;
        if (uri == null) {
            Toast.makeText(activity, "System app missed !", Toast.LENGTH_SHORT).show();
        }
        uri = converToContentUri(activity, uri);

        Intent intent = buildCropIntent(activity, "com.android.camera.action.CROP", uri);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "跳转剪切界面错误", Toast.LENGTH_SHORT).show();
        }

    }

    private static int aspectXX = 1;
    private static int aspectYY = 1;
    private static int outputXX = 200;
    private static int outputYY = 200;
    private static final String CROP_TYPE = "image/*";
    private static final String KEY_CROP = "crop";
    private static final String KEY_SCALE = "scale";
    private static final String KEY_ASPECT_X = "aspectX";
    private static final String KEY_ASPECT_Y = "aspectY";
    private static final String KEY_OUTPUT_X = "outputX";
    private static final String KEY_OUTPUT_Y = "outputY";
    private static final String KEY_RETURN_DATA = "return-data";
    private static final String KEY_OUTPUT_FORMAT = "outputFormat";
    private static final String KEY_NO_FACE_DETECTION = "noFaceDetection";
    private static final String KEY_SCALE_UP_IF_NEEDED = "scaleUpIfNeeded";
    private static final String OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG.toString();

    private static Intent buildCropIntent(Activity activity, String action, Uri uri) {
        Intent intent = new Intent(action);
        intent.setType(CROP_TYPE);

        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, 0);

        if (uri != null) {
            intent.setData(uri);
        }
        intent.putExtra(KEY_CROP, "true");
        intent.putExtra(KEY_SCALE, true);
        intent.putExtra(KEY_ASPECT_X, aspectXX);
        intent.putExtra(KEY_ASPECT_Y, aspectYY);
        intent.putExtra(KEY_OUTPUT_X, outputXX);
        intent.putExtra(KEY_OUTPUT_Y, outputYY);
        intent.putExtra(KEY_RETURN_DATA, false);
        intent.putExtra(KEY_OUTPUT_FORMAT, OUTPUT_FORMAT);
        intent.putExtra(KEY_NO_FACE_DETECTION, true);
        intent.putExtra(KEY_SCALE_UP_IF_NEEDED, true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CACHE_FILE_URI);

        Intent i = new Intent(intent);
        int size = list.size();
        if (size > 0) {
            ResolveInfo res = list.get(0);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        }
        return i;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putStringArrayList("listImg", listImg);
            outState.putInt("imgNumber", imgNumber);
            if (imageCaptureUri != null) {
                outState.putString("imageCaptureUri", imageCaptureUri.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            ArrayList listImgs = savedInstanceState.getStringArrayList("listImg");
            int imgNumbers = savedInstanceState.getInt("imgNumber", -1);
            if (listImgs != null && listImgs.size() > 0) {
                listImg.addAll(listImgs);
                imgAdapter.notifyDataSetChanged();
            }
            if (imgNumbers != -1) {
                imgNumber = imgNumbers;
            }

            File tempfile = new File(savedInstanceState.getString("imageCaptureUri"));
            imageCaptureUri = Uri.fromFile(tempfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
