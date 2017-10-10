package se.boreklev.copyherepastethere;

import android.media.Image;

import se.boreklev.copyherepastethere.camera2.CameraFragment;

/**
 * Created by matti on 2017-08-31.
 */

public abstract class FragmentActionCommand implements FragmentInteractionCommand {
    private boolean mReady;
    private boolean mScanner;
    private ImageProcessor mProcessor;

    public FragmentActionCommand(ImageProcessor processor, boolean s) {
        mReady = true;
        mScanner = s;
        mProcessor = processor;
    }

    @Override
    public void execute(CopyHerePasteThere chpt) {
        chpt.takePicture(this);
    }

    public ImageProcessor getProcessor() {
        return mProcessor;
    }

    public void setReady(boolean r) {
        mReady = r;
    }

    public boolean isReady() {
        return mReady;
    }

    public boolean isScanner() {
        return mScanner;
    }

    public void setScanner(boolean s) {
        mScanner = s;
    }
}
