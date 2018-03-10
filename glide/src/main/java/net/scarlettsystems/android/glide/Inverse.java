package net.scarlettsystems.android.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

/**
 * Inverts the colours of the image.
 *
 * @author Shane Scarlett
 * @version 1.0.0
 */
public class Inverse extends BitmapTransformation
{
	private static final String ID = "net.scarlettsystems.android.transformations.glide.Inverse";
	private static final byte[] ID_BYTES = ID.getBytes();

	/**
	 * Default constructor. No other configuration required.
	 *
	 */
	public Inverse(){}

	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight)
	{
		Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
		//Create Image Paint
		Paint paint = new Paint();
		ColorMatrix matrix = new ColorMatrix(new float[]{
				-1.0f, 0.0f, 0.0f, 0.0f, 255f,
				0.0f, -1.0f, 0.0f, 0.0f, 255f,
				0.0f, 0.0f, -1.0f, 0.0f, 255f,
				0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
		paint.setColorFilter(new ColorMatrixColorFilter(matrix));
		//Draw to Canvas
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(source, 0, 0, paint);
		return bitmap;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof Greyscale)
		{
			return true;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Util.hashCode(ID.hashCode());
	}

	@Override
	public void updateDiskCacheKey(MessageDigest messageDigest)
	{
		messageDigest.update(ID_BYTES);
	}
}
