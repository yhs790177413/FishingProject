/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goby.fishing.common.photochoose.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.goby.fishing.R;

public class ScaleCropImageActivity extends MonitoredActivity {
	private static final boolean IN_MEMORY_CROP = Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1;
	private static final int SIZE_DEFAULT = 2048;
	private static final int SIZE_LIMIT = 4096;

	private final Handler handler = new Handler();

	private int exifRotation;
	private Uri sourceUri, saveUri;
	private int sampleSize;
	private RotateBitmap rotateBitmap;

	private ImageCroppingView imageView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crop_activity_crop);
		initViews();

		setupFromIntent();
		if (rotateBitmap == null) {
			finish();
			return;
		}
		startCrop();
	}

	private void initViews() {
		imageView = (ImageCroppingView) findViewById(R.id.crop_image);

		findViewById(R.id.btn_cancel).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});

		findViewById(R.id.btn_done).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						onSaveClicked();
					}
				});
	}

	private void setupFromIntent() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			saveUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT);
		}

		sourceUri = intent.getData();
		if (sourceUri != null) {
			exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(
					getContentResolver(), sourceUri));

			InputStream is = null;
			try {
				sampleSize = calculateBitmapSampleSize(sourceUri);
				is = getContentResolver().openInputStream(sourceUri);
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = sampleSize;
				rotateBitmap = new RotateBitmap(BitmapFactory.decodeStream(is,
						null, option), exifRotation);
			} catch (IOException e) {
				setResultException(e);
			} catch (OutOfMemoryError e) {
				setResultException(e);
			} finally {
				CropUtil.closeSilently(is);
			}
		}
	}

	private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
		InputStream is = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			is = getContentResolver().openInputStream(bitmapUri);
			BitmapFactory.decodeStream(is, null, options); // Just get image
			// size
		} finally {
			CropUtil.closeSilently(is);
		}

		int maxSize = getMaxImageSize();
		int sampleSize = 1;
		while (options.outHeight / sampleSize > maxSize
				|| options.outWidth / sampleSize > maxSize) {
			sampleSize = sampleSize << 1;
		}
		return sampleSize;
	}

	private int getMaxImageSize() {
		int textureLimit = getMaxTextureSize();
		if (textureLimit == 0) {
			return SIZE_DEFAULT;
		} else {
			return Math.min(textureLimit, SIZE_LIMIT);
		}
	}

	private int getMaxTextureSize() {
		// The OpenGL texture size is the maximum size that can be drawn in an
		// ImageView
		int[] maxSize = new int[1];
		GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
		return maxSize[0];
	}

	private void startCrop() {
		if (isFinishing()) {
			return;
		}

		imageView.setImageBitmap(rotateBitmap.getBitmap());
	}

	private void onSaveClicked() {
		saveImage(imageView.getCroppedImage());
	}

	private void saveImage(Bitmap croppedImage) {
		if (croppedImage != null) {
			final Bitmap b = croppedImage;
			CropUtil.startBackgroundJob(this, null,
					getResources().getString(R.string.crop__saving),
					new Runnable() {
						public void run() {
							saveOutput(b);
						}
					}, handler);
		} else {
			finish();
		}
	}

	private void clearImageView() {
		imageView.setImageBitmap(null);
		if (rotateBitmap != null) {
			rotateBitmap.recycle();
		}
		System.gc();
	}

	private void saveOutput(Bitmap croppedImage) {
		if (saveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = getContentResolver().openOutputStream(saveUri);
				if (outputStream != null) {
					croppedImage.compress(Bitmap.CompressFormat.JPEG, 90,
							outputStream);
				}
			} catch (IOException e) {
				setResultException(e);
			} finally {
				CropUtil.closeSilently(outputStream);
			}

			if (!IN_MEMORY_CROP) {
				// In-memory crop negates the rotation
				CropUtil.copyExifRotation(CropUtil.getFromMediaUri(
						getContentResolver(), sourceUri), CropUtil
						.getFromMediaUri(getContentResolver(), saveUri));
			}

			setResultUri(saveUri);
		}

		final Bitmap b = croppedImage;
		handler.post(new Runnable() {
			public void run() {
				b.recycle();
				clearImageView();
			}
		});

		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (rotateBitmap != null) {
			rotateBitmap.recycle();
		}
	}

	private void setResultUri(Uri uri) {
		setResult(RESULT_OK,
				new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
	}

	private void setResultException(Throwable throwable) {
		setResult(Crop.RESULT_ERROR,
				new Intent().putExtra(Crop.Extra.ERROR, throwable));
	}

}
