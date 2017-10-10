package se.boreklev.copyherepastethere.camera2;

import android.hardware.camera2.CaptureRequest;

import se.boreklev.copyherepastethere.CopyHerePasteThere;
import se.boreklev.copyherepastethere.FragmentInteractionCommand;

/**
 * Created by matti on 2017-09-19.
 */

public class CameraFlashCommand implements FragmentInteractionCommand {

    public static final int FLASH_ON = CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH;
    public static final int FLASH_OFF = CaptureRequest.CONTROL_AE_MODE_OFF;
    public static final int FLASH_AUTO = CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH;
    private int mFlash;

    public CameraFlashCommand(int flash) {
        mFlash = flash;
    }
    @Override
    public void execute(CopyHerePasteThere chpt) {
        chpt.setFlash(mFlash);
    }
}
