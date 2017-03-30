package com.goby.fishing.common.utils.helper.android.util;//package com.umeng.soexample;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.shareboard.SnsPlatform;
//import com.umeng.soexample.model.AuthAdapter;
//import com.umeng.soexample.model.ShareAdapter;
//
//import java.util.ArrayList;
//
///**
// * Created by wangfei on 16/11/9.
// */
//public class AuthActivity extends Activity{
//    private ListView listView;
//    private AuthAdapter shareAdapter;
//    public ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
//    private SHARE_MEDIA[] list = {SHARE_MEDIA.QQ,SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,
//            SHARE_MEDIA.FACEBOOK,SHARE_MEDIA.TWITTER,SHARE_MEDIA.LINKEDIN,SHARE_MEDIA.DOUBAN,SHARE_MEDIA.RENREN,SHARE_MEDIA.KAKAO};
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.umeng_auth);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.umeng_blue));
//
//        }
//        listView = (ListView) findViewById(R.id.list);
//        initPlatforms();
//        shareAdapter  = new AuthAdapter(this,platforms);
//        listView.setAdapter(shareAdapter);
//
//        ((TextView)findViewById(R.id.umeng_title)).setText(R.string.umeng_auth_title);
//        findViewById(R.id.umeng_back).setVisibility(View.VISIBLE);
//        findViewById(R.id.umeng_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }
//    private void initPlatforms(){
//        platforms.clear();
//        for (SHARE_MEDIA e :list) {
//            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())){
//                platforms.add(e.toSnsPlatform());
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
//    }
//}
//
//package com.umeng.soexample.model;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.umeng.socialize.UMAuthListener;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.common.ResContainer;
//import com.umeng.socialize.linkin.LISession;
//import com.umeng.socialize.shareboard.SnsPlatform;
//import com.umeng.soexample.R;
//
//import java.util.ArrayList;
//import java.util.Map;
//
///**
// * Created by wangfei on 16/11/9.
// */
//public class AuthAdapter extends BaseAdapter {
//    private ArrayList<SnsPlatform> list;
//    private Context context;
//    public AuthAdapter(Context context, ArrayList<SnsPlatform> list){
//        this.list = list;
//        this.context = context;
//    }
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView ==null){
//            convertView =  LayoutInflater.from(context).inflate(R.layout.app_authadapter, null);
//        }
//        final boolean isauth = UMShareAPI.get(context).isAuthorize((Activity) context,list.get(position).mPlatform);
//        ImageView img = (ImageView) convertView.findViewById(R.id.adapter_image);
//        img.setImageResource(ResContainer.getResourceId(context,"drawable",list.get(position).mIcon));
//        TextView tv = (TextView)convertView.findViewById(R.id.name);
//        tv.setText(ResContainer.getResourceId(context,"string",list.get(position).mShowWord));
//         TextView authBtn = (TextView) convertView.findViewById(R.id.auth_button);
//        if (isauth){
//            authBtn.setText("删除授权");
//        }else {
//            authBtn.setText("授权");
//        }
//       authBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isauth){
//
//                    UMShareAPI.get(context).deleteOauth((Activity) context,list.get(position).mPlatform,authListener);
//
//                }else {
//                    UMShareAPI.get(context).doOauthVerify((Activity) context,list.get(position).mPlatform,authListener);
//
//                }
//            }
//        });
//        if (position == list.size()-1){
//            convertView.findViewById(R.id.divider).setVisibility(View.GONE);
//        }else {
//            convertView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
//        }
////
//        return convertView;
//    }
//    UMAuthListener authListener = new UMAuthListener() {
//        @Override
//        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(context,"成功了",Toast.LENGTH_LONG).show();
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            Toast.makeText(context,"失败："+t.getMessage(),Toast.LENGTH_LONG).show();
//
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText(context,"取消了",Toast.LENGTH_LONG).show();
//
//        }
//    };
//}
//
//
