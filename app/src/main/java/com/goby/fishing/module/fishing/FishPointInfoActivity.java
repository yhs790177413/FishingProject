package com.goby.fishing.module.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.framework.AbsBaseActivity;

public class FishPointInfoActivity extends AbsBaseActivity implements
		OnClickListener {

	private TextView tv_fishpointChooseOne;

	private TextView tv_fishpointChooseTwo;

	private TextView tv_fishpointChooseThree;

	private TextView tv_fishpointChooseFour;

	private LinearLayout ll_leftBack;

	private String chooseString;

	private TextView tv_nextBtn;
	
	private final static int POSITION = 1009;
	
	private double longitude;
	
	private double latitude;
	
	private int quality = 0;


	public static void launch(Activity activity, int requestCode,double longitude,
			double latitude) {

		Intent intent = new Intent(activity, FishPointInfoActivity.class);
		intent.putExtra("requestCode", requestCode);
		intent.putExtra("longitude", longitude);
		intent.putExtra("latitude", latitude);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fishpoint_info_step_two);

		longitude = getIntent().getDoubleExtra("longitude", -1);
		latitude = getIntent().getDoubleExtra("latitude", -1);
		initView();
	}

	public void initView() {

		tv_fishpointChooseOne = (TextView) findViewById(R.id.fishpoint_choose_one);
		tv_fishpointChooseTwo = (TextView) findViewById(R.id.fishpoint_choose_two);
		tv_fishpointChooseThree = (TextView) findViewById(R.id.fishpoint_choose_three);
		tv_fishpointChooseFour = (TextView) findViewById(R.id.fishpoint_choose_four);
		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		tv_nextBtn = (TextView) findViewById(R.id.next_btn);

		tv_fishpointChooseOne.setOnClickListener(this);
		tv_fishpointChooseTwo.setOnClickListener(this);
		tv_fishpointChooseThree.setOnClickListener(this);
		tv_fishpointChooseFour.setOnClickListener(this);
		tv_nextBtn.setOnClickListener(this);
		ll_leftBack.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(request, result, data);
		if (result == RESULT_OK) {
			Intent intent = new Intent();
			intent.putExtra("chooseLongitude", data.getDoubleExtra("chooseLongitude", 0));
			intent.putExtra("chooseLatitude", data.getDoubleExtra("chooseLatitude", 0));
			intent.putExtra("fishPointInfo", chooseString);
			intent.putExtra("quality", quality);
			setResult(RESULT_OK, intent);
			Log.d("info----", data.getDoubleExtra("chooseLongitude", 0)+"=="+data.getDoubleExtra("chooseLatitude", 0));
			finish();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.fishpoint_choose_one:
			chooseString = tv_fishpointChooseOne.getText().toString().trim();
			tv_fishpointChooseOne.setTextColor(this.getResources().getColor(
					R.color.blue_35b2e1));
			tv_fishpointChooseTwo.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseThree.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseFour.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			quality = 1;
			LocationModeSourceActivity.launch(this, POSITION, longitude, latitude);
			break;
		case R.id.fishpoint_choose_two:
			chooseString = tv_fishpointChooseTwo.getText().toString().trim();
			tv_fishpointChooseTwo.setTextColor(this.getResources().getColor(
					R.color.blue_35b2e1));
			tv_fishpointChooseOne.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseThree.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseFour.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			quality = 2;
			LocationModeSourceActivity.launch(this, POSITION, longitude, latitude);
			break;
		case R.id.fishpoint_choose_three:
			chooseString = tv_fishpointChooseThree.getText().toString().trim();
			tv_fishpointChooseThree.setTextColor(this.getResources().getColor(
					R.color.blue_35b2e1));
			tv_fishpointChooseOne.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseTwo.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseFour.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			quality = 3;
			LocationModeSourceActivity.launch(this, POSITION, longitude, latitude);
			break;
		case R.id.fishpoint_choose_four:
			chooseString = tv_fishpointChooseFour.getText().toString().trim();
			tv_fishpointChooseFour.setTextColor(this.getResources().getColor(
					R.color.blue_35b2e1));
			tv_fishpointChooseOne.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseTwo.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			tv_fishpointChooseThree.setTextColor(this.getResources().getColor(
					R.color.gray_aaaaaa));
			quality = 4;
			LocationModeSourceActivity.launch(this, POSITION, longitude, latitude);
			break;
		case R.id.left_back_layout:
			finish();
			break;
		case R.id.next_btn:
			LocationModeSourceActivity.launch(this, POSITION, longitude, latitude);
			break;
		default:
			Log.d("info", longitude+"=="+latitude);
			break;
		}
	}
}
