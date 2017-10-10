package se.boreklev.copyherepastethere;

/**
 * Created by mattias on 2017-08-31.
 *
 * Interface for various commands from fragments to main view.
 */

public interface FragmentInteractionCommand {
    public abstract void execute(CopyHerePasteThere chpt);
}