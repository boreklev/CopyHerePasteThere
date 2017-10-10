package se.boreklev.copyherepastethere;

import android.media.Image;
import android.support.v4.app.Fragment;
/**
 * Created by matti on 2017-08-23.
 */

public abstract class CHPTFragment extends Fragment {

    public CHPTFragment getThis() {
        return this;
    }

    public abstract void fragmentSelected();

    public abstract void fragmentDeselected();

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(FragmentInteractionCommand cmd);
    }
}