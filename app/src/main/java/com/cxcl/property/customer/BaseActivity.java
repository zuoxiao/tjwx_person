package com.cxcl.property.customer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tjwx_person.tool.CustomProgressDialog;
import com.example.tjwx_person.utils.Theme;

public class BaseActivity extends Activity implements OnClickListener {

    LinearLayout baseLiner;
    LayoutInflater inflater;
    private CustomProgressDialog progressDialog = null;
    /**
     * 只有title和返回键的actionBar
     */
    public final int BackTitle = 1;

    /**
     * 只有标题
     */
    public final int Title = 2;

    /**
     * 取消和标题
     */
    public final int CancelTitle = 3;

    /**
     * 刷新和返回
     */
    public final int backRefresh = 4;

    /**
     * 登录后退出
     */
    public final int signOut = 5;

    /**
     * 保存数据
     */
    public final int keep = 6;

    /**
     * 提交
     */
    public final int submit = 7;

    /**
     * @param layoutResID
     * @param type        1只有title和返回键的actionBar,2只有标题,3取消和标题
     * @param titleName
     */
    public void addContentView(int layoutResID, int type, String titleName) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        baseLiner = (LinearLayout) findViewById(R.id.baseActivity_ll);
        inflater = LayoutInflater.from(this);// 初始化inflate
        View layoutView = LayoutInflater.from(this).inflate(layoutResID, null);
        LayoutParams lp = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutView.setLayoutParams(lp);
        switch (type) {
            case BackTitle:
                back_title(titleName);
                break;
            case Title:
                title(titleName);
                break;
            case CancelTitle:
                CancelTitle(titleName);
                break;
            case backRefresh:
                backRefresh(titleName);
                break;
            case signOut:

                signOut(titleName);
                break;
            case keep:
                keep(titleName);
                break;
            case submit:
                submit(titleName);
                break;

        }
        baseLiner.addView(layoutView);

    }

    /**
     * 只有title和返回键的actionBar
     *
     * @param titleName
     */
    private void back_title(String titleName) {
        View back_title = inflater.inflate(R.layout.title_back, null);
        RelativeLayout rel_back_title = (RelativeLayout) back_title
                .findViewById(R.id.rel_back_title);
        LinearLayout back = (LinearLayout) back_title
                .findViewById(R.id.ibt_rel_back);
        ImageView ImgBack = (ImageView) back_title.findViewById(R.id.ibt_back);
        TextView ibt_title = (TextView) back_title.findViewById(R.id.ibt_title);
        TextView text = (TextView) back_title.findViewById(R.id.text_title);
        Theme.setTextSize(ibt_title, Theme.title_small);
        Theme.setTextSize(text, Theme.title_big);
        Theme.setViewSize(ImgBack, Theme.pix(50), Theme.pix(40));

        Theme.setViewSize(rel_back_title, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(90));
        text.setText(titleName);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                backButton();
            }
        });
        baseLiner.addView(back_title);
    }

    /**
     * 只有title
     *
     * @param titleName
     */
    private void title(String titleName) {
        View back_title = inflater.inflate(R.layout.title_title, null);
        LinearLayout rel_back_title = (LinearLayout) back_title
                .findViewById(R.id.rel_back_title);

        TextView text = (TextView) back_title.findViewById(R.id.text_title);

        Theme.setTextSize(text, Theme.title_big);

        Theme.setViewSize(rel_back_title, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(90));
        text.setText(titleName);

        baseLiner.addView(back_title);
    }

    private void backRefresh(String titleName) {
        View back_title = inflater.inflate(R.layout.title_back_refresh, null);
        RelativeLayout rel_back_title = (RelativeLayout) back_title
                .findViewById(R.id.rel_back_title);
        LinearLayout back = (LinearLayout) back_title
                .findViewById(R.id.ibt_rel_back);
        ImageView ImgBack = (ImageView) back_title.findViewById(R.id.ibt_back);
        TextView ibt_title = (TextView) back_title.findViewById(R.id.ibt_title);
        TextView text = (TextView) back_title.findViewById(R.id.text_title);
        TextView refresh = (TextView) back_title.findViewById(R.id.refresh_title);
        Theme.setTextSize(ibt_title, Theme.title_small);
        Theme.setTextSize(text, Theme.title_big);
        Theme.setTextSize(refresh, Theme.title_small);
        Theme.setViewSize(ImgBack, Theme.pix(50), Theme.pix(40));

        Theme.setViewSize(rel_back_title, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(90));
        text.setText(titleName);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                backButton();
            }
        });
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh();
            }
        });
        baseLiner.addView(back_title);
    }


    private void CancelTitle(String titleName) {

        View back_title = inflater.inflate(R.layout.title_cancel, null);
        RelativeLayout rel_back_title = (RelativeLayout) back_title
                .findViewById(R.id.rel_back_title);
        LinearLayout back = (LinearLayout) back_title
                .findViewById(R.id.ibt_rel_back);
        ImageView ImgBack = (ImageView) back_title.findViewById(R.id.ibt_back);
        TextView ibt_title = (TextView) back_title.findViewById(R.id.ibt_title);
        TextView text = (TextView) back_title.findViewById(R.id.text_title);
        Theme.setTextSize(ibt_title, Theme.title_small);
        Theme.setTextSize(text, Theme.title_big);
        Theme.setViewSize(ImgBack, Theme.pix(50), Theme.pix(40));

        Theme.setViewSize(rel_back_title, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(90));
        text.setText(titleName);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                backButton();
            }
        });
        baseLiner.addView(back_title);

    }

    private void signOut(String titleName) {
        View back_title = inflater.inflate(R.layout.title_sign_out, null);
        RelativeLayout rel_back_title = (RelativeLayout) back_title
                .findViewById(R.id.rel_back_title);
        LinearLayout back = (LinearLayout) back_title
                .findViewById(R.id.ibt_rel_back);
        TextView ibt_title = (TextView) back_title.findViewById(R.id.ibt_title);
        ImageView ImgBack = (ImageView) back_title.findViewById(R.id.ibt_back);
        TextView text = (TextView) back_title.findViewById(R.id.text_title);
        final TextView signOut = (TextView) back_title.findViewById(R.id.title_signOut);
        Theme.setTextSize(text, Theme.title_big);
        Theme.setTextSize(signOut, Theme.title_small);
        Theme.setTextSize(ibt_title, Theme.title_small);
        Theme.setViewSize(rel_back_title, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(90));
        Theme.setViewSize(ImgBack, Theme.pix(50), Theme.pix(40));
        text.setText(titleName);
        signOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });
        baseLiner.addView(back_title);
    }

    private void keep(String titleName) {
        {
            View back_title = inflater.inflate(R.layout.title_keep, null);
            RelativeLayout rel_back_title = (RelativeLayout) back_title
                    .findViewById(R.id.rel_back_title);
            LinearLayout back = (LinearLayout) back_title
                    .findViewById(R.id.ibt_rel_back);
            TextView ibt_title = (TextView) back_title.findViewById(R.id.ibt_title);
            ImageView ImgBack = (ImageView) back_title.findViewById(R.id.ibt_back);
            TextView text = (TextView) back_title.findViewById(R.id.text_title);
            final TextView keep = (TextView) back_title.findViewById(R.id.title_keep);
            Theme.setTextSize(text, Theme.title_big);
            Theme.setTextSize(keep, Theme.title_small);
            Theme.setTextSize(ibt_title, Theme.title_small);
            Theme.setViewSize(rel_back_title, ViewGroup.LayoutParams.MATCH_PARENT,
                    Theme.pix(90));
            Theme.setViewSize(ImgBack, Theme.pix(50), Theme.pix(40));
            text.setText(titleName);
            keep.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    keep();
                }
            });
            back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    backButton();
                }
            });
            baseLiner.addView(back_title);
        }
    }

    private void submit(String titleName) {
        {
            {
                View back_title = inflater.inflate(R.layout.title_submit, null);
                RelativeLayout rel_back_title = (RelativeLayout) back_title
                        .findViewById(R.id.rel_back_title);
                LinearLayout back = (LinearLayout) back_title
                        .findViewById(R.id.ibt_rel_back);
                TextView ibt_title = (TextView) back_title.findViewById(R.id.ibt_title);
                ImageView ImgBack = (ImageView) back_title.findViewById(R.id.ibt_back);
                TextView text = (TextView) back_title.findViewById(R.id.text_title);
                final TextView keep = (TextView) back_title.findViewById(R.id.title_keep);
                Theme.setTextSize(text, Theme.title_big);
                Theme.setTextSize(keep, Theme.title_small);
                Theme.setTextSize(ibt_title, Theme.title_small);
                Theme.setViewSize(rel_back_title, ViewGroup.LayoutParams.MATCH_PARENT,
                        Theme.pix(90));
                Theme.setViewSize(ImgBack, Theme.pix(50), Theme.pix(40));
                text.setText(titleName);
                keep.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitBtn();
                    }
                });
                back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backButton();
                    }
                });
                baseLiner.addView(back_title);
            }
        }
    }

    /**
     * 提交
     */
    public void submitBtn() {

    }

    /**
     * 监听返回键
     */
    public void backButton() {
        // TODO Auto-generated method stub

    }

    /**
     * 刷新数据
     */
    public void Refresh() {

    }

    /**
     * 退出登录
     */
    public void signOut() {

    }

    /**
     * 保存
     */
    public void keep() {

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    /**
     * 通信等待开始
     */
    public void startProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = CustomProgressDialog.createDialog(this);
                progressDialog.setMessage("正在加载，请稍后！");
            }

            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通信等待完成
     */
    protected void stopProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
