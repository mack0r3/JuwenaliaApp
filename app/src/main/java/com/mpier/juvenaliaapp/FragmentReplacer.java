package com.mpier.juvenaliaapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Konpon96 on 08-May-16.
 */
public final class FragmentReplacer {

    /**
     * Switch fragment of main container to passed fragment
     *
     * @param fragmentManager Fragment manager
     * @param fragment Fragment which is meant to be displayed in main container
     */
    public static void switchFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.main_container, fragment)
                .commit();
    }

    /**
     * Switch fragment of main container to tiles (main menu)
     *
     * @param fragmentManager Fragment manager
     */
    public static void switchToTiles(FragmentManager fragmentManager) {
        switchFragment(fragmentManager, new TilesFragment());
    }

}
