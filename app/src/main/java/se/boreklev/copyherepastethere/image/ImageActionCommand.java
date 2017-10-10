package se.boreklev.copyherepastethere.image;

import se.boreklev.copyherepastethere.FragmentActionCommand;
import se.boreklev.copyherepastethere.ImageProcessor;

/**
 * Created by matti on 2017-09-14.
 */

public class ImageActionCommand extends FragmentActionCommand {

    protected ImageActionCommand(ImageProcessor processor) {
        super(processor, false);
    }
}
