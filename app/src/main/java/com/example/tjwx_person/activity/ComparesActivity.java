package com.example.tjwx_person.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.URLConstant;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.Theme;
import com.example.tjwx_person.utils.UserData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 评价
 *
 * @author zuo
 */
public class ComparesActivity extends BaseActivity {

    publishedData publised = null;
    UserAction userAction;
    Context mContext;
    String compares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_compares, 1, "匿名评价");
        initView();
    }

    TextView compares_rating2_Text;
    RatingBar compares_rating2, compares_reting1;
    LinearLayout satisfied, dissatisfied;
    EditText unclosed_edt;
    TextView unclosed_ok;
    TextView compares_name;
    ImageView compares_call, compares_img;

    TextView compares_RatingText;
    //坏评价
    CheckBox dissatisfied_behaveBadly, dissatisfied_goodBad, dissatisfied_cookingBad, dissatisfied_dislike, dissatisfied_fool, dissatisfied_highPrice;
    //好评价
    CheckBox satisfied_skillful, satisfied_high, satisfied_enthusiasticService, satisfied_Zambia,
            satisfied_earnest, satisfied_mature;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private void initView() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.worke_img)
                .showImageForEmptyUri(R.drawable.worke_img)
                .showImageOnFail(R.drawable.worke_img)
                // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(100)).cacheOnDisc(false)
                .considerExifParams(true).build();
        userAction = UserAction.getInstance();
        mContext = this;
        compares_img = (ImageView) findViewById(R.id.compares_img);
        dissatisfied_behaveBadly = (CheckBox) findViewById(R.id.dissatisfied_behaveBadly);
        dissatisfied_goodBad = (CheckBox) findViewById(R.id.dissatisfied_goodBad);
        dissatisfied_cookingBad = (CheckBox) findViewById(R.id.dissatisfied_cookingBad);
        dissatisfied_dislike = (CheckBox) findViewById(R.id.dissatisfied_dislike);
        dissatisfied_fool = (CheckBox) findViewById(R.id.dissatisfied_fool);
        dissatisfied_highPrice = (CheckBox) findViewById(R.id.dissatisfied_highPrice);
        satisfied_skillful = (CheckBox) findViewById(R.id.satisfied_skillful);
        satisfied_high = (CheckBox) findViewById(R.id.satisfied_high);
        satisfied_enthusiasticService = (CheckBox) findViewById(R.id.satisfied_enthusiasticService);
        satisfied_Zambia = (CheckBox) findViewById(R.id.satisfied_Zambia);
        satisfied_earnest = (CheckBox) findViewById(R.id.satisfied_earnest);
        satisfied_mature = (CheckBox) findViewById(R.id.satisfied_mature);
        compares_RatingText = (TextView) findViewById(R.id.compares_RatingText);

        compares_rating2_Text = (TextView) findViewById(R.id.compares_rating2_Text);
        compares_rating2 = (RatingBar) findViewById(R.id.compares_rating2);
        compares_reting1 = (RatingBar) findViewById(R.id.compares_reting1);

/*      Theme.setViewSize(compares_reting1, Theme.pix(155), Theme.pix(30));
        Theme.setViewSize(compares_rating2, Theme.pix(458), Theme.pix(85));*/

        satisfied = (LinearLayout) findViewById(R.id.satisfied);
        dissatisfied = (LinearLayout) findViewById(R.id.dissatisfied);
        publised = (publishedData) getIntent().getSerializableExtra("publised");
        unclosed_edt = (EditText) findViewById(R.id.unclosed_edt);
        unclosed_ok = (TextView) findViewById(R.id.unclosed_ok);
        compares_name = (TextView) findViewById(R.id.compares_name);
        compares_name.setText(publised.getProcessorName());
        compares_call = (ImageView) findViewById(R.id.compares_call);
       if (publised.getProcessorEvaluateCount() == 0) {
            compares_reting1.setAlpha(1);
            compares_RatingText.setText("5");
        } else {
            compares_reting1.setAlpha(publised.getProcessorEvaluateScope() / publised.getProcessorEvaluateCount());
            compares_RatingText.setText(String.valueOf(publised.getProcessorEvaluateScope() / publised.getProcessorEvaluateCount()));

        }
        String access_token = UserData.getSettingString(mContext,
                UserData.access_token);
        imageLoader.displayImage(URLConstant.FULLURL + publised.getProcessorHeadPortraitUrl()+"&access_token="+access_token, compares_img, options, null);
        unclosed_ok.setOnClickListener(this);
        compares_call.setOnClickListener(this);
        compares_rating2
                .setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromUser) {
                        if (rating > 3) {
                            compares_rating2_Text.setText("满意");
                            satisfied.setVisibility(View.VISIBLE);
                            dissatisfied.setVisibility(View.GONE);
                            setBooleanFalse1();//重置所有坏评价
                        } else {
                            compares_rating2_Text.setText("不满意");
                            satisfied.setVisibility(View.GONE);
                            dissatisfied.setVisibility(View.VISIBLE);
                            setBooleanFalse2();//重置所有好评价
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.unclosed_ok:
                if (Utils.isNetworkAvailable(mContext)) {
                    if (dissatisfied_behaveBadly.isChecked()) {
                        compares += "态度恶劣";
                    }
                    if (dissatisfied_goodBad.isChecked()) {
                        compares += "把好的整坏了";
                    }
                    if (dissatisfied_cookingBad.isChecked()) {
                        compares += "技术太差了";
                    }
                    if (dissatisfied_dislike.isChecked()) {
                        compares += "看不顺眼";
                    }
                    if (dissatisfied_fool.isChecked()) {
                        compares += "纯属糊弄";
                    }
                    if (dissatisfied_highPrice.isChecked()) {
                        compares += "价格要高了";
                    }
                    if (satisfied_skillful.isChecked()) {
                        compares += "手艺高超";
                    }
                    if (satisfied_high.isChecked()) {
                        compares += "速度快质量高";
                    }
                    if (satisfied_enthusiasticService.isChecked()) {
                        compares += "热情服务";
                    }
                    if (satisfied_Zambia.isChecked()) {
                        compares += "赞就一个字";
                    }
                    if (satisfied_earnest.isChecked()) {
                        compares += "认真细致";
                    }


                    compares += unclosed_edt.getText().toString();
                    if (!"".equals(compares) && !"null".equals(compares)) {
                        evaluate();
                    } else {
                        BaseToast.makeShortToast(mContext, "请输入或者选择评价");
                    }

                } else {
                    BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                }

                break;
            case R.id.compares_call:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:" + publised.getProcessorMobilePhone()));
                startActivity(intent1);

            default:
                break;
        }

    }

    private void setBooleanFalse1() {
        dissatisfied_behaveBadly.setChecked(false);
        dissatisfied_goodBad.setChecked(false);
        dissatisfied_cookingBad.setChecked(false);
        dissatisfied_dislike.setChecked(false);
        dissatisfied_fool.setChecked(false);
        dissatisfied_highPrice.setChecked(false);


    }

    private void setBooleanFalse2() {
        satisfied_skillful.setChecked(false);
        satisfied_high.setChecked(false);
        satisfied_enthusiasticService.setChecked(false);
        satisfied_Zambia.setChecked(false);
        satisfied_earnest.setChecked(false);
        satisfied_mature.setChecked(false);
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    private void evaluate() {


        userAction.evaluate(mContext, UserData.getSettingString(mContext,
                UserData.access_token), publised.getId(), String
                        .valueOf(compares_rating2.getNumStars()), compares_rating2_Text
                        .getText().toString(), unclosed_edt.getText().toString(),
                new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        ComparesActivity.this.finish();
                        BaseToast.makeShortToast(mContext, "评价成功");
                        //发送广播  ;
                        Intent mIntent = new Intent(OrderFinishedActivity.ACTION_NAME);
                        sendBroadcast(mIntent);
                        Intent mInten2 = new Intent(OrderListActivity.ACTION_NAME);
                        sendBroadcast(mInten2);
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        BaseToast.makeShortToast(mContext, "评价失败，请您稍后再试");

                    }
                });
    }

}
