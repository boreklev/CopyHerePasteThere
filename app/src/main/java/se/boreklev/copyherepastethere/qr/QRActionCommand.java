package se.boreklev.copyherepastethere.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.nio.ByteBuffer;

import se.boreklev.copyherepastethere.CHPTFragment;
import se.boreklev.copyherepastethere.CopyHerePasteThere;
import se.boreklev.copyherepastethere.FragmentActionCommand;
import se.boreklev.copyherepastethere.ImageProcessor;
import se.boreklev.copyherepastethere.R;
import se.boreklev.copyherepastethere.SendTask;
import se.boreklev.copyherepastethere.SendTaskParameters;
import se.boreklev.copyherepastethere.camera2.CameraFragment;

/**
 * Created by matti on 2017-08-31.
 */

public class QRActionCommand extends FragmentActionCommand {

    protected QRActionCommand(ImageProcessor processor) {
        super(processor, true);
    }
}
