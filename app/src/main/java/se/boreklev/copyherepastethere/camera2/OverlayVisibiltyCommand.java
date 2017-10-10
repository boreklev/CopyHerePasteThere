package se.boreklev.copyherepastethere.camera2;

/**
 * Created by matti on 2017-10-10.
 */

public class OverlayVisibiltyCommand extends CameraSettingCommand {
    private boolean mVisible;

    public OverlayVisibiltyCommand(boolean visible) {
        mVisible = visible;
    }
    @Override
    public void set(CameraFragment cf) {
        cf.showOverlay(mVisible);
    }
}
