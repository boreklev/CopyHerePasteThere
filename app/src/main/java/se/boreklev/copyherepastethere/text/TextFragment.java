package se.boreklev.copyherepastethere.text;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import se.boreklev.copyherepastethere.CHPTFragment;
//import se.boreklev.copyherepastethere.FragmentActionCommand;
import se.boreklev.copyherepastethere.CopyHerePasteThere;
import se.boreklev.copyherepastethere.FragmentActionCommand;
import se.boreklev.copyherepastethere.FragmentInteractionCommand;
import se.boreklev.copyherepastethere.FragmentSelectedCommand;
import se.boreklev.copyherepastethere.ImageProcessor;
import se.boreklev.copyherepastethere.NoActionCommand;
import se.boreklev.copyherepastethere.R;
import se.boreklev.copyherepastethere.SendTaskParameters;
import se.boreklev.copyherepastethere.qr.QRFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextFragment extends CHPTFragment implements ImageProcessor {

    private OnFragmentInteractionListener mListener;
    public final static String TAG = "TEXT_FRAGMENT";
    private TessBaseAPI mTess; //Tess API reference
    private String mDatapath;
    private String mLang;

    public TextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CopyTextFragment.
     */
    public static TextFragment newInstance() {
        TextFragment fragment = new TextFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        ImageView btn_image = view.findViewById(R.id.ts_image);
        btn_image.setClickable(true);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().executePendingTransactions();
                CHPTFragment to = ((CHPTFragment)getActivity().getSupportFragmentManager().findFragmentByTag("IMAGE_FRAGMENT"));
                FragmentSelectedCommand fsc = new FragmentSelectedCommand(getThis(),
                        to,
                        FragmentSelectedCommand.RIGHT);
                if (mListener != null) {
                    mListener.onFragmentInteraction(fsc);
                }
            }
        });

        ImageView btn_text = (ImageView)view.findViewById(R.id.ts_text);
        btn_text.setClickable(true);
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {Log.i(TAG, "Pressed");
                TextActionCommand tac = new TextActionCommand((ImageProcessor)getThis());
                if (mListener != null) {
                    mListener.onFragmentInteraction(tac);
                }
            }
        });

        ImageView btn_qr = view.findViewById(R.id.ts_qr);
        btn_qr.setClickable(true);
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().executePendingTransactions();
                CHPTFragment to = ((CHPTFragment)getActivity().getSupportFragmentManager().findFragmentByTag("QR_FRAGMENT"));
                FragmentSelectedCommand fsc = new FragmentSelectedCommand(getThis(),
                        to,
                        FragmentSelectedCommand.LEFT);
                if (mListener != null) {
                    mListener.onFragmentInteraction(fsc);
                }
            }
        });

        //initialize Tesseract API
        mLang = "swe";
        mDatapath = getActivity().getFilesDir()+ "/tesseract/";
        mTess = new TessBaseAPI();

        checkFile(new File(mDatapath + "tessdata/"));
        mTess.init(mDatapath, mLang);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void fragmentSelected() {
    }

    @Override
    public void fragmentDeselected() {

    }

    private void setup() {
        try {
            String filepath = mDatapath + "/tessdata/" + mLang + ".traineddata";
            File f = new File(filepath);
            if(!f.exists())
                f.mkdirs();
            Log.i(TAG, f.getAbsolutePath());
            if(!f.exists()) {
                //get access to AssetManager
                AssetManager assetManager = getContext().getAssets();

                //open byte streams for reading/writing
                InputStream instream = assetManager.open("tessdata/" + mLang + ".traineddata");
                OutputStream outstream = new FileOutputStream(filepath);

                //copy the file to the location specified by filepath
                byte[] buffer = new byte[1024];
                int read;
                while ((read = instream.read(buffer)) != -1) {
                    outstream.write(buffer, 0, read);
                }
                outstream.flush();
                outstream.close();
                instream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        if(dir.exists()) {
            String datafilepath = mDatapath+ "/tessdata/" + mLang + ".traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            String filepath = mDatapath + "/tessdata/" + mLang + ".traineddata";
            AssetManager assetManager = getActivity().getAssets();

            InputStream instream = assetManager.open("tessdata/" + mLang + ".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Override
    public SendTaskParameters process(Bitmap bitmap) {
        mTess.setImage(bitmap);
        Log.i("TEST", mTess.getUTF8Text());
        String str = mTess.getUTF8Text();
        if(str.length() == 0) return null;
        return new SendTaskParameters(SendTaskParameters.TEXT, str);
    }
}
