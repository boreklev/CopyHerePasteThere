package se.boreklev.copyherepastethere.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import se.boreklev.copyherepastethere.CHPTFragment;
import se.boreklev.copyherepastethere.ImageProcessor;
import se.boreklev.copyherepastethere.NoActionCommand;
import se.boreklev.copyherepastethere.SendTaskParameters;
import se.boreklev.copyherepastethere.FragmentSelectedCommand;
import se.boreklev.copyherepastethere.R;
import se.boreklev.copyherepastethere.camera2.OverlayVisibiltyCommand;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QRFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRFragment extends CHPTFragment implements ImageProcessor {
    private OnFragmentInteractionListener mListener;
    public final static String TAG = "QR_FRAGMENT";
    private BarcodeDetector mBarcodeDetector;
    private QRActionCommand mQRActionCommand;
    //private Overlay mOverlay;

    //private QRCodeReader mQrReader;

    public QRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QRFragment.
     */
    public static QRFragment newInstance() {
        QRFragment fragment = new QRFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr, container, false);

        mBarcodeDetector =
                new BarcodeDetector.Builder(this.getActivity().getApplicationContext()) .build();


        ImageView btn_text = (ImageView)view.findViewById(R.id.qrs_text);
        btn_text.setClickable(true);
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().executePendingTransactions();
                CHPTFragment to = ((CHPTFragment)getActivity().getSupportFragmentManager().findFragmentByTag("TEXT_FRAGMENT"));
                FragmentSelectedCommand fsc = new FragmentSelectedCommand(getThis(),
                        to,
                        FragmentSelectedCommand.RIGHT);
                if (mListener != null) {
                    mListener.onFragmentInteraction(fsc);
                }
            }
        });

        ImageView btn_qr = view.findViewById(R.id.qrs_qr);
        btn_qr.setClickable(true);
        Animation fadeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.tween);
        btn_qr.startAnimation(fadeAnim);

        ImageView btn_image = view.findViewById(R.id.qrs_image);
        btn_image.setClickable(true);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().executePendingTransactions();
                CHPTFragment to = ((CHPTFragment)getActivity().getSupportFragmentManager().findFragmentByTag("IMAGE_FRAGMENT"));
                FragmentSelectedCommand fsc = new FragmentSelectedCommand(getThis(),
                        to,
                        FragmentSelectedCommand.LEFT);
                if (mListener != null) {
                    mListener.onFragmentInteraction(fsc);
                }
            }
        });

        //mOverlay = view.findViewById(R.id.qrs_overlay);
        mQRActionCommand = new QRActionCommand(this);

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
        if (mListener != null) {
            mListener.onFragmentInteraction(mQRActionCommand);
        }
    }

    @Override
    public void fragmentDeselected() {
        if (mListener != null) {
            mListener.onFragmentInteraction(new NoActionCommand());
        }
    }

    @Override
    public SendTaskParameters process(Bitmap bitmap) {
        try {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcodes = mBarcodeDetector.detect(frame);
            // End if we couldn't find a barcode
            if(barcodes.size() == 0) { return null; }
            Barcode thisCode = barcodes.valueAt(0);
            return new SendTaskParameters(SendTaskParameters.TEXT, thisCode.rawValue);
        } catch(Exception e) {
            Log.e(TAG, "Error in processing image", e);
            return null;
        }
    }
}
