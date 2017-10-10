package se.boreklev.copyherepastethere;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by matti on 2017-09-11.
 */

public class NoActionCommand extends FragmentActionCommand implements ImageProcessor {
    public NoActionCommand() {
        super(null, false);
    }

    @Override
    public ImageProcessor getProcessor() {
        return this;
    }

    @Override
    public SendTaskParameters process(Bitmap bitmap) {
        return null;
    }
}
