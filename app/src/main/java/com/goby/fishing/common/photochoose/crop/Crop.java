package com.goby.fishing.common.photochoose.crop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.java.util.FileTool;
import com.goby.fishing.common.utils.helper.java.util.Mail;

public class Crop {

	public static final int REQUEST_CROP = 6709;
	public static final int REQUEST_PICK = 9162;
	public static final int REQUEST_CAMERA = 8192;
	public static final int RESULT_ERROR = 404;

	static interface Extra {
		String ASPECT_X = "aspect_x";
		String ASPECT_Y = "aspect_y";
		String MAX_X = "max_x";
		String MAX_Y = "max_y";
		String ERROR = "error";
	}

	private Intent cropIntent;

	/**
	 * Create a crop Intent builder with source image
	 * 
	 * @param source
	 *            Source image URI
	 */
	public Crop(Uri source) {
		cropIntent = new Intent();
		cropIntent.setData(source);
	}

	/**
	 * Set output URI where the cropped image will be saved
	 * 
	 * @param output
	 *            Output image URI
	 */
	public Crop output(Uri output) {
		cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, output);
		return this;
	}

	/**
	 * Set fixed aspect ratio for crop area
	 * 
	 * @param x
	 *            Aspect X
	 * @param y
	 *            Aspect Y
	 */
	public Crop withAspect(int x, int y) {
		cropIntent.putExtra(Extra.ASPECT_X, x);
		cropIntent.putExtra(Extra.ASPECT_Y, y);
		return this;
	}

	/**
	 * Crop area with fixed 1:1 aspect ratio
	 */
	public Crop asSquare() {
		cropIntent.putExtra(Extra.ASPECT_X, 1);
		cropIntent.putExtra(Extra.ASPECT_Y, 1);
		return this;
	}

	/**
	 * Set maximum crop size
	 * 
	 * @param width
	 *            Max width
	 * @param height
	 *            Max height
	 */
	public Crop withMaxSize(int width, int height) {
		cropIntent.putExtra(Extra.MAX_X, width);
		cropIntent.putExtra(Extra.MAX_Y, height);
		return this;
	}

	/**
	 * Send the crop Intent!
	 * 
	 * @param activity
	 *            Activity that will receive result
	 */
	public void start(Activity activity) {
		activity.startActivityForResult(getIntent(activity), REQUEST_CROP);
	}

	public void startScale(Activity activity) {
		activity.startActivityForResult(getScaleIntent(activity), REQUEST_CROP);
	}

	@SuppressLint("NewApi") public void start(Fragment fragment) {
		fragment.startActivityForResult(getIntent(fragment.getActivity()),
				REQUEST_CROP);
	}

	@SuppressLint("NewApi") public void startScale(Fragment fragment) {
		fragment.startActivityForResult(getScaleIntent(fragment.getActivity()),
				REQUEST_CROP);
	}

	public void start(android.support.v4.app.Fragment fragment) {
		fragment.startActivityForResult(getIntent(fragment.getActivity()),
				REQUEST_CROP);
	}

	public void startScale(android.support.v4.app.Fragment fragment) {
		fragment.startActivityForResult(getScaleIntent(fragment.getActivity()),
				REQUEST_CROP);
	}

	/**
	 * Send the crop Intent!
	 * 
	 * @param context
	 *            Context
	 * @param fragment
	 *            Fragment that will receive result
	 */
	@SuppressLint("NewApi") public void start(Context context, Fragment fragment) {
		fragment.startActivityForResult(getIntent(context), REQUEST_CROP);
	}

	@SuppressLint("NewApi") public void startScale(Context context, Fragment fragment) {
		fragment.startActivityForResult(getScaleIntent(context), REQUEST_CROP);
	}

	public void start(Context context, android.support.v4.app.Fragment fragment) {
		fragment.startActivityForResult(getIntent(context), REQUEST_CROP);
	}

	public void startScale(Context context,
			android.support.v4.app.Fragment fragment) {
		fragment.startActivityForResult(getScaleIntent(context), REQUEST_CROP);
	}

	Intent getIntent(Context context) {
		cropIntent.setClass(context, CropImageActivity.class);
		return cropIntent;
	}

	Intent getScaleIntent(Context context) {
		cropIntent.setClass(context, ScaleCropImageActivity.class);
		return cropIntent;
	}

	/**
	 * Retrieve URI for cropped image, as set in the Intent builder
	 * 
	 * @param result
	 *            Output Image URI
	 */
	public static Uri getOutput(Intent result) {
		return result.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
	}

	/**
	 * Retrieve error that caused crop to fail
	 * 
	 * @param result
	 *            Result Intent
	 * @return Throwable handled in CropImageActivity
	 */
	public static Throwable getError(Intent result) {
		return (Throwable) result.getSerializableExtra(Extra.ERROR);
	}

	/**
	 * Utility method that starts an image picker since that often precedes a
	 * crop
	 * 
	 * @param activity
	 *            Activity that will receive result
	 */
	public static void pickImage(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
				.setType("image/*");
		try {
			activity.startActivityForResult(intent, REQUEST_PICK);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void camera(Activity activity, Uri uri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			activity.startActivityForResult(intent, REQUEST_CAMERA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void camera(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			activity.startActivityForResult(intent, REQUEST_CAMERA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("NewApi") public static void pickImage(Fragment fragment) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
				.setType("image/*");
		try {
			fragment.startActivityForResult(intent, REQUEST_PICK);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(fragment.getActivity(), R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("NewApi") public static void camera(Fragment fragment, Uri uri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			fragment.startActivityForResult(intent, REQUEST_CAMERA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(fragment.getActivity(), R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("NewApi") public static void camera(Fragment fragment) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			fragment.startActivityForResult(intent, REQUEST_CAMERA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(fragment.getActivity(), R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void pickImage(android.support.v4.app.Fragment fragment) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
				.setType("image/*");
		try {
			fragment.startActivityForResult(intent, REQUEST_PICK);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(fragment.getActivity(), R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void camera(android.support.v4.app.Fragment fragment, Uri uri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			fragment.startActivityForResult(intent, REQUEST_CAMERA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(fragment.getActivity(), R.string.crop__pick_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void camera(android.support.v4.app.Fragment fragment) {
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// try {
		// fragment.startActivityForResult(intent, REQUEST_CAMERA);
		// } catch (ActivityNotFoundException e) {
		// Toast.makeText(fragment.getActivity(), R.string.crop__pick_error,
		// Toast.LENGTH_SHORT).show();
		// }
		try {
			String pathName = FileTool.getImageCachePathName();
			if (pathName == null) {
				return;
			}
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri uri = Uri.fromFile(new File(pathName));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			fragment.startActivityForResult(intent, REQUEST_CAMERA);
			Mail.putMail("photo_file", uri);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
