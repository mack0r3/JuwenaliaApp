package com.mpier.juvenaliaapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Konpon96 on 08-May-16.
 */
public final class FragmentReplacer {

    /**
     * Switch fragment of main container to passed fragment
     *
     * @param fragmentManager Fragment manager
     * @param fragment Fragment which is meant to be displayed in main container
     * @param addToBackstack Set to true if fragment should be added to backstack
     */
    public static void switchFragment(FragmentManager fragmentManager, Fragment fragment, boolean addToBackstack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .add(R.id.main_container, fragment);

        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    /**
     * Switch fragment of main container to tiles (main menu)
     *
     * @param fragmentManager Fragment manager
     */
    public static void switchToTiles(FragmentManager fragmentManager) {
        switchFragment(fragmentManager, new TilesFragment(), false);
    }

}
