package com.goby.fishing.common.photochoose.crop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * Created by hu on 15/1/31.
 */
public class UriUtils {
	private static final String SCHEME_FILE = "file";
	private static final String SCHEME_CONTENT = "content";

	public static Uri parseUri(String uri) {
		try {
			return Uri.parse(uri);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressLint("NewApi")
	public static String getFromMediaUri(Context context, Uri uri) {
		if (uri == null)
			return null;

		String path = null;
		if (SCHEME_FILE.equals(uri.getScheme())) {
			path = uri.getPath();
		} else if (SCHEME_CONTENT.equals(uri.getScheme())) {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
					&& DocumentsContract.isDocumentUri(context, uri)) {
				String wholeID = DocumentsContract.getDocumentId(uri);
				String id = wholeID.split(":")[1];
				String[] column = { MediaStore.Images.Media.DATA };
				String sel = MediaStore.Images.Media._ID + "=?";
				Cursor cursor = context.getContentResolver().query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
						sel, new String[] { id }, null);
				int columnIndex = cursor.getColumnIndex(column[0]);
				if (cursor.moveToFirst()) {
					path = cursor.getString(columnIndex);
				}
				cursor.close();
			} else {
				String[] projection = { MediaStore.Images.Media.DATA };
				Cursor cursor = context.getContentResolver().query(uri,
						projection, null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.moveToFirst()) {
					path = cursor.getString(column_index);
				}
			}

			if (!TextUtils.isEmpty(path)) {
				return path;
			}

			final String[] filePathColumn = { MediaStore.MediaColumns.DATA,
					MediaStore.MediaColumns.DISPLAY_NAME };
			Cursor cursor = null;
			try {
				cursor = context.getContentResolver().query(uri,
						filePathColumn, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					final int columnIndex = (uri.toString()
							.startsWith("content://com.google.android.gallery3d")) ? cursor
							.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
							: cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
					// Picasa image on newer devices with Honeycomb and up
					if (columnIndex != -1) {
						path = cursor.getString(columnIndex);
						if (!TextUtils.isEmpty(path)) {
							return path;
						}
					}
				}
			} catch (SecurityException ignored) {
				// Nothing we can do
			} finally {
				if (cursor != null)
					cursor.close();
			}
		}
		return null;
	}
}
