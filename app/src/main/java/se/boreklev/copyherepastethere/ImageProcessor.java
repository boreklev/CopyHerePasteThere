package se.boreklev.copyherepastethere;

import android.graphics.Bitmap;

/**
 * Created by matti on 2017-09-11.
 */

public interface ImageProcessor {
    public SendTaskParameters process(Bitmap bitmap);
}
