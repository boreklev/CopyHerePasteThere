package se.boreklev.copyherepastethere;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

/**
 * Created by mattias on 2017-08-22.
 *
 * Sets one of the interaction fragments to visible and the others to invisible.
 * Called from the current visible fragment, so you have to provide the fragment to be set visible
 * in the constructor.
 */

public class FragmentSelectedCommand implements FragmentInteractionCommand {
    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    private int mDirection;
    private CHPTFragment mFrom;
    private CHPTFragment mTo;

    public FragmentSelectedCommand(CHPTFragment from, CHPTFragment to, int direction) {
        mFrom = from;
        mTo = to;
        mDirection = direction;
    }

    @Override
    public void execute(CopyHerePasteThere chpt) {
        FragmentTransaction transaction = chpt.getSupportFragmentManager().beginTransaction();
        if(mDirection == 0)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        else
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        if(mTo.isHidden())
            transaction.show(mTo);
        transaction.replace(mFrom.getId(), mTo);
        transaction.addToBackStack(null);
        transaction.commit();
        mFrom.fragmentDeselected();
        mTo.fragmentSelected();
    }
}
