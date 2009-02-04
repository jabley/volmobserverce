package com.volantis.mcs.runtime;

import com.volantis.synergetics.cornerstone.utilities.extensions.Extension;

/**
 * Update service to check if there is a newer version of the product.
 */
public interface UpdateService extends Extension {

    /**
     * Checks for updates.
     * <p>If there is an updated version of the product available then it's up
     * to the implementation how it handles the update.</p>
     */
    public void checkForUpdates();
}
