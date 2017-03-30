package com.goby.fishing.module.index;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Criteria;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.MyImageView;
import com.goby.fishing.common.utils.helper.android.app.RoundImageView;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.framework.AbsBaseActivity;
import com.umeng.socialize.UMShareAPI;

public class GameDetailActivity extends AbsBaseActivity implements OnClickListener {

    private LinearLayout ll_leftBack;

    private TextView tv_shareBtn;

    private ShareDialogUtils dialog_share;

    private UIHandler uiHandler;

    private RoundImageView civ_userHeader;

    private TextView tv_userName, tv_content, tv_message, tv_time;

    private MyImageView iv_contentPic;

    private int screenWidth;

    public static void launch(Activity activity, String userName, String userHeader, String content, String picUrl, String message, long time) {

        Intent intent = new Intent(activity, GameDetailActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userHeader", userHeader);
        intent.putExtra("content", content);
        intent.putExtra("picUrl", picUrl);
        intent.putExtra("message", message);
        intent.putExtra("time", time);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        // 获取屏幕宽高
        screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
        uiHandler = new UIHandler();
        initView();
    }

    public void initView() {

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        tv_shareBtn = (TextView) findViewById(R.id.share_btn);
        civ_userHeader = (RoundImageView) findViewById(R.id.user_header);
        tv_userName = (TextView) findViewById(R.id.user_name);
        tv_content = (TextView) findViewById(R.id.content);
        iv_contentPic = (MyImageView) findViewById(R.id.content_pic);
        tv_message = (TextView) findViewById(R.id.message);
        tv_time = (TextView) findViewById(R.id.time);

        ImageLoaderWrapper.getDefault().displayImage(getIntent().getStringExtra("userHeader"), civ_userHeader);
        ImageLoaderWrapper.getDefault().displayImage(getIntent().getStringExtra("picUrl"), iv_contentPic);
        iv_contentPic.setColour(Color.WHITE);
        iv_contentPic.setBorderWidth(10);
        tv_userName.setText(getIntent().getStringExtra("userName"));
        tv_content.setText(getIntent().getStringExtra("content"));
        //tv_content.setTextSize(screenWidth*30/dip2px(this,720));
        if (screenWidth >= 720) {
            tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        } else {
            tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
        tv_message.setText(getIntent().getStringExtra("message"));
        tv_time.setText(TimeUtil.getTimeString(getIntent().getLongExtra(("time"), 0) * 1000));

        tv_shareBtn.setOnClickListener(this);
        ll_leftBack.setOnClickListener(this);
    }

    public class UIHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            dialog_share.initPlatform();
            String mTitle = null;
            String shareUrl = null;
            switch (msg.what) {
                case 0:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(GameDetailActivity.this), 0);
                    break;
                case 1:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(GameDetailActivity.this), 1);
                    break;
                case 2:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(GameDetailActivity.this), 2);
                    break;
                case 3:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(GameDetailActivity.this), 3);
                    break;
                case 4:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(GameDetailActivity.this), 4);
                    break;
            }

        }
    }

    /**
     * 分享平台的选择
     *
     * @param position
     */
    public void selectSharePaltform(String content, Bitmap bitmap, int position) {
        dialog_share.startShare(content, bitmap, position);
    }

    // 获取指定Activity的截屏，保存到png文件
    private Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.share_btn:
                // 弹出分享
                dialog_share = new ShareDialogUtils(this, R.style.dialog, uiHandler, true);
                dialog_share.setCanceledOnTouchOutside(true);
                dialog_share.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
