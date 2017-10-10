package se.boreklev.copyherepastethere.camera2;

import se.boreklev.copyherepastethere.CopyHerePasteThere;
import se.boreklev.copyherepastethere.FragmentInteractionCommand;

/**
 * Created by matti on 2017-10-10.
 */

public abstract class CameraSettingCommand implements FragmentInteractionCommand {
    @Override
    public void execute(CopyHerePasteThere chpt) {
        chpt.cameraSetting(this);
    }

    public abstract void set(CameraFragment cf);
}
