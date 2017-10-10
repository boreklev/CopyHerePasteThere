package se.boreklev.copyherepastethere;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import se.boreklev.copyherepastethere.camera2.CameraFragment;
import se.boreklev.copyherepastethere.camera2.CameraSettingCommand;
import se.boreklev.copyherepastethere.image.ImageFragment;
import se.boreklev.copyherepastethere.qr.QRFragment;
import se.boreklev.copyherepastethere.text.TextFragment;

public class CopyHerePasteThere extends AppCompatActivity
        implements CHPTFragment.OnFragmentInteractionListener {
    // member variables
    private CameraFragment mCameraFragment;
    // static variable
    private static CopyHerePasteThere sME;
    private static String sServer;
    private static int sPort;
    // public static
    public static final String PREF = "chptpref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_here_paste_there);
        if (null == savedInstanceState) {
            mCameraFragment = CameraFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, mCameraFragment)
                    .commit();
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

        QRFragment mQRfragment = QRFragment.newInstance();
        TextFragment mTextFragment = TextFragment.newInstance();
        ImageFragment mImageFragment = ImageFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, mTextFragment, TextFragment.TAG);
        transaction.add(R.id.container, mQRfragment, QRFragment.TAG);
        transaction.add(R.id.container, mImageFragment, ImageFragment.TAG);
        transaction.show(mTextFragment);
        transaction.hide(mQRfragment);
        transaction.hide(mImageFragment);
        transaction.commit();
        SharedPreferences sharedPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sServer = sharedPref.getString(getString(R.string.server), "10.0.0.10");
        sPort = sharedPref.getInt(getString(R.string.port), 8179);
        sME = this;

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_server) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Override this to make sure scanning ends if back button is pressed.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onFragmentInteraction(new NoActionCommand());
    }

    public static String getServer() {
        return sServer;
    }

    public static int getPort() {
        return sPort;
    }

    public static CopyHerePasteThere getMe() { return sME; }
    public static void yo() { Toast.makeText(getMe(), "YO", Toast.LENGTH_SHORT).show(); }
    public static FileInputStream getFile() throws FileNotFoundException {
        return sME.openFileInput("pic.jpg");
    }

    @Override
    public void onFragmentInteraction(FragmentInteractionCommand cmd) {
        cmd.execute(this);
    }

    public void takePicture(FragmentActionCommand fac) {
        mCameraFragment.takePicture(fac);
    }

    public void setFlash(int flash) { mCameraFragment.setFlash(flash); }

    public void cameraSetting(CameraSettingCommand csc) {
        csc.set(mCameraFragment);
    }

    public static void feedback(String msg) {
        Toast.makeText(sME, msg, Toast.LENGTH_SHORT).show();
        MediaPlayer mp = MediaPlayer.create(sME, R.raw.click);
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator)sME.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) sME.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(150);
        }
        mp.start();
    }
}
