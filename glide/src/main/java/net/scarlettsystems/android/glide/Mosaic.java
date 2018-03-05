package net.scarlettsystems.android.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;

public class Mosaic extends BitmapTransformation
{
	private static final String ID = "net.scarlettsystems.android.transformations.glide.Mosaic";
	private static final byte[] ID_BYTES = ID.getBytes();
	private Context mContext;
	private Integer xPixels, yPixels;
	private float factor = 1f;


	public Mosaic(Context context)
	{
		mContext = context;
	}

	public Mosaic setByWidth(int widthPixels)
	{
		xPixels = Math.max(1, widthPixels);
		yPixels = -1;
		return this;
	}

	public Mosaic setByHeight(int heightPixels)
	{
		xPixels = -1;
		yPixels = Math.max(1, heightPixels);
		return this;
	}

	public Mosaic setByFactor(int downsizeFactor)
	{
		xPixels = -1;
		yPixels = -1;
		factor = Math.max(1, downsizeFactor);
		return this;
	}

	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight)
	{
		//Size Image
		resolveDimensions(source.getWidth(), source.getHeight());
		Bitmap scaled = Bitmap.createScaledBitmap(source, xPixels, yPixels, false);
		Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
		Rect bitmapBounds = new Rect(0, 0, source.getWidth(), source.getHeight());
		//Create Image Paint
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setFilterBitmap(false);
		//Draw to Canvas
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(scaled, null, bitmapBounds, paint);
		scaled.recycle();
		return bitmap;

	}

	private void resolveDimensions(int width, int height)
	{
		if(xPixels != -1)
		{
			xPixels = Math.min(width, xPixels);
			yPixels = Math.round((float)height / Math.max(1f, (float)width / (float)xPixels));
		}
		else if(yPixels != -1)
		{
			yPixels = Math.min(width, yPixels);
			xPixels = Math.round((float)height / Math.max(1f, (float)height / (float)yPixels));
		}
		else
		{
			xPixels = Math.max(1, Math.round((float)width / factor));
			yPixels = Math.max(1, Math.round((float)height / factor));
		}
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof Mosaic)
		{
			Mosaic other = (Mosaic) object;
			return xPixels == other.xPixels
					&& yPixels == other.yPixels
					&& factor == other.factor;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Util.hashCode(ID.hashCode(),
				Util.hashCode(xPixels,
						Util.hashCode(yPixels,
						Util.hashCode(factor))));
	}

	@Override
	public void updateDiskCacheKey(MessageDigest messageDigest)
	{
		ArrayList<byte[]> messages = new ArrayList<>();

		messages.add(ID_BYTES);
		messages.add(ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(xPixels).array());
		messages.add(ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(yPixels).array());
		messages.add(ByteBuffer.allocate(Float.SIZE/Byte.SIZE).putFloat(factor).array());

		for(int c = 0; c < messages.size(); c++)
		{
			messageDigest.update(messages.get(c));
		}
	}
}