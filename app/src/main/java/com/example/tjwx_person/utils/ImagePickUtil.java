package com.example.tjwx_person.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cxcl.property.customer.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ImagePickUtil {

    private static final String TAG = "ImagePickUtil";
    private static final String NO_AVAILABLE_APP = "System app missed !";

    private static final String CROP_CACHE_FILE_NAME = "crop_cache_file.jpg";
    private static final String CROP_CACHE_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator
            + CROP_CACHE_FILE_NAME;
    private static final Uri CACHE_FILE_URI = Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon()
            .appendPath(CROP_CACHE_FILE_NAME).build();

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

    public static final int PICK_FROM_CAMERA = 1;
    public static final int CROP_FROM_PICK = 2;
    public static final int PICK_FROM_FILE = 3;

    private static int aspectXX = 1;
    private static int aspectYY = 1;
    private static int outputXX = 200;
    private static int outputYY = 200;

    private static Uri imageCaptureUri;
    private static Activity userActivity;

    // private static PopupWindows popupWindows;

    public static void pickImageFromCamera(Activity activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        File tempfile = new File(createPhotoPath());
        if (tempfile.isFile()) {
            tempfile.delete();
        }
        imageCaptureUri = Uri.fromFile(tempfile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        try {
            intent.putExtra("return-data", true);
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, NO_AVAILABLE_APP, Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri getCameraPictureUri() {
        return imageCaptureUri;
    }

    public static void pickImageFromGallery(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(CROP_TYPE);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {

        }
    }

    public static void openPhotoZoom(Activity activity, int requestCode, Uri url) {
        Uri uri = url;
        if (uri == null) {
            Toast.makeText(activity, NO_AVAILABLE_APP, Toast.LENGTH_SHORT).show();
        }
        uri = converToContentUri(activity, uri);

        Intent intent = buildCropIntent(activity, "com.android.camera.action.CROP", uri);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {

        }

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

    public static void clearCache() {
        File cache = new File(CROP_CACHE_FILE_PATH);
        cache.delete();
        if (imageCaptureUri != null) {
            cache = new File(imageCaptureUri.getPath());
            cache.delete();
        }
    }

    public static Bitmap getCropedBitmap(Context context) {
        return getCropedBitmap(context, true);
    }

    public static Bitmap getCropedBitmap(Context context, boolean clear) {
        Bitmap tempPhoto = null;
        InputStream inputStream = getCacheFileInputStream(context);

        tempPhoto = BitmapFactory.decodeStream(inputStream);
        if (clear) {
            clearCache();
        }
        return tempPhoto;
    }

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

    @SuppressLint("NewApi")
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
            uri = Uri.withAppendedPath(Images.Media.EXTERNAL_CONTENT_URI, split[1]);

            return uri;
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getEncodedPath();
            if (path == null) {
                return uri;
            }
            path = Uri.decode(path);
            ContentResolver cr = context.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(").append(Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
            Cursor cursor = null;
            try {
                cursor =
                        cr.query(Images.Media.EXTERNAL_CONTENT_URI, new String[]{Images.ImageColumns._ID},
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

    /**
     * Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String createPhotoPath() {
        File datalDir = Environment.getExternalStorageDirectory();
        File myDir = new File(datalDir, "/DCIM/Camera");
        myDir.mkdirs();
        String directoryname = datalDir.toString() + "/DCIM/Camera";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hhmmss", Locale.SIMPLIFIED_CHINESE);

        String photoPath = directoryname + "IMG_" + sdf.format(new Date()) + ".jpg";
        return photoPath;
    }

    public static void setCropParameter(int aspectX, int aspectY, int outputX, int outputY) {
        aspectXX = aspectX;
        aspectYY = aspectY;
        outputXX = outputX;
        outputYY = outputY;
    }

    public static void showOptionsView(Activity activity, View parent) {
        userActivity = activity;
        new PopupWindows(activity, parent);
    }

    public static class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.pop_select_photo, null);
            //   LinearLayout popup = (LinearLayout) view.findViewById(R.id.bottom_popup);
            //   popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom));

            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            // 监听back物理键   
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        dismiss();

                    }
                    return false;
                }
            });
            TextView cameraButton = (TextView) view.findViewById(R.id.photograph);
            TextView photoButton = (TextView) view.findViewById(R.id.albums);
            LinearLayout cancelButton = (LinearLayout) view.findViewById(R.id.cancel);
            cameraButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    pickImageFromCamera(userActivity, PICK_FROM_CAMERA);
                    dismiss();
                }
            });
            photoButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    pickImageFromGallery(userActivity, PICK_FROM_FILE);
                    dismiss();
                }
            });
            cancelButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

}
