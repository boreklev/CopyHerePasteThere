package se.boreklev.copyherepastethere.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import se.boreklev.copyherepastethere.CHPTFragment;
import se.boreklev.copyherepastethere.FragmentSelectedCommand;
import se.boreklev.copyherepastethere.ImageProcessor;
import se.boreklev.copyherepastethere.R;
import se.boreklev.copyherepastethere.SendTaskParameters;
import se.boreklev.copyherepastethere.camera2.OverlayVisibiltyCommand;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends CHPTFragment implements ImageProcessor {

    private OnFragmentInteractionListener mListener;
    public final static String TAG = "IMAGE_FRAGMENT";

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageFragment newInstance() {
        ImageFragment fragment = new ImageFragment();
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
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView btn_qr = (ImageView)view.findViewById(R.id.is_qr);
        btn_qr.setClickable(true);
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().executePendingTransactions();
                CHPTFragment to = ((CHPTFragment)getActivity().getSupportFragmentManager().findFragmentByTag("QR_FRAGMENT"));
                FragmentSelectedCommand fsc = new FragmentSelectedCommand(getThis(),
                        to,
                        FragmentSelectedCommand.RIGHT);
                if (mListener != null) {
                    mListener.onFragmentInteraction(fsc);
                }
            }
        });

        ImageView btn_image = view.findViewById(R.id.is_image);
        btn_image.setClickable(true);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ImageActionCommand iac = new ImageActionCommand((ImageProcessor) getThis());
                if (mListener != null) {
                    mListener.onFragmentInteraction(iac);
                }
            }
        });

        ImageView btn_text = view.findViewById(R.id.is_text );
        btn_text.setClickable(true);
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().executePendingTransactions();
                CHPTFragment to = ((CHPTFragment)getActivity().getSupportFragmentManager().findFragmentByTag("TEXT_FRAGMENT"));
                FragmentSelectedCommand fsc = new FragmentSelectedCommand(getThis(),
                        to,
                        FragmentSelectedCommand.LEFT);
                if (mListener != null) {
                    mListener.onFragmentInteraction(fsc);
                }
            }
        });
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
            mListener.onFragmentInteraction(new OverlayVisibiltyCommand(false));
        }
    }

    @Override
    public void fragmentDeselected() {
        if (mListener != null) {
            mListener.onFragmentInteraction(new OverlayVisibiltyCommand(true));
        }
    }

    //@Override
    public SendTaskParameters process(Bitmap bitmap) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] array = outputStream.toByteArray();
            return new SendTaskParameters(SendTaskParameters.IMAGE, array, getString(R.string.image_sent));
        } catch(Exception e) {
            Log.e(TAG, "Error in processing image", e);
            return null;
        }
    }
}
