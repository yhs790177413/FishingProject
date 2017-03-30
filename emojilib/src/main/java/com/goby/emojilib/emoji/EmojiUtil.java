package com.goby.emojilib.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goby.emojilib.R;

public class EmojiUtil {
	private static ArrayList<Emoji> emojiList;

	public static ArrayList<Emoji> getEmojiList() {
		if (emojiList == null) {
			emojiList = generateEmojis();
		}
		return emojiList;
	}

	private static ArrayList<Emoji> generateEmojis() {
		ArrayList<Emoji> list = new ArrayList<Emoji>();
		for (int i = 0; i < EmojiResArray.length; i++) {
			Emoji emoji = new Emoji();
			emoji.setImageUri(EmojiResArray[i]);
			emoji.setContent(EmojiTextArray[i]);
			list.add(emoji);
		}
		return list;
	}

	public static final int[] EmojiResArray = { R.drawable.baoqian,
			R.drawable.bufu, R.drawable.buhuiba, R.drawable.che,
			R.drawable.daxiao, R.drawable.e, R.drawable.gaoxing,
			R.drawable.haixiu, R.drawable.han, R.drawable.han2,
			R.drawable.haose, R.drawable.huangzhang, R.drawable.jingya,
			R.drawable.ku, R.drawable.leiben, R.drawable.meng,
			R.drawable.miaoshi, R.drawable.nihao, R.drawable.nima,
			R.drawable.nu, R.drawable.pen, R.drawable.qiang, R.drawable.qiqiu,
			R.drawable.shuai, R.drawable.shuaku, R.drawable.shui,
			R.drawable.touxiao, R.drawable.tu, R.drawable.wuyu,
			R.drawable.xiao, R.drawable.yi, R.drawable.zaban,
			R.drawable.zhuanqian, };

	public static final String[] EmojiTextArray = { "[抱歉]", "[不服]", "[不会吧]",
			"[che]", "[大笑]", "[额]", "[搞笑]", "[害羞]", "[大汗]", "[流汗]", "[好色]",
			"[慌张]", "[惊讶]", "[哭]", "[泪奔]", "[萌]", "[藐视]", "[你好]", "[尼玛]",
			"[怒]", "[喷]", "[强]", "[乞求]", "[衰]", "[耍酷]", "[睡]", "[偷笑]", "[吐]",
			"[无语]", "[笑]", "[疑问]", "[咋办]", "[赚钱]", };

	static {
		emojiList = generateEmojis();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static SpannableStringBuilder handlerEmojiText(String content,
			Context context) throws IOException {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "\\[(\\S+?)\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		Iterator<Emoji> iterator;
		Emoji emoji = null;
		while (m.find()) {
			iterator = emojiList.iterator();
			String tempText = m.group();
			while (iterator.hasNext()) {
				emoji = iterator.next();
				if (tempText.equals(emoji.getContent())) {
					// 转换为Span并设置Span的大小
					sb.setSpan(
							new ImageSpan(context,
									decodeSampledBitmapFromResource(
											context.getResources(),
											emoji.getImageUri(),
											dip2px(context, 18),
											dip2px(context, 18))), m.start(), m
									.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;
				}
			}
		}
		return sb;
	}
}
