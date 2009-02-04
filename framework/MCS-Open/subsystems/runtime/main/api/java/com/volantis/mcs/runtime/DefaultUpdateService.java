/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.synergetics.cornerstone.utilities.extensions.ExtensionFactoryLoader;
import com.volantis.mcs.utilities.VolantisVersion;

/**
 * The default version of the UpdateService.
 */
public class DefaultUpdateService implements UpdateService {

    /**
     * The base url of the update service. The build name will be appended to
     * the end of this
     */
    private static final String VOLANTIS_UPDATE_SERVICE =
        "http://www.volantis.com/updateping/";

    // javadoc inherited
    public void checkForUpdates() {

        StringBuffer currentVersion = new StringBuffer();
        currentVersion.append(VolantisVersion.getVolantisProductName());
        currentVersion.append("-");
        currentVersion.append(VolantisVersion.getVolantisVersion());
        
        UpdateChecker.checkForUpdates(VOLANTIS_UPDATE_SERVICE + currentVersion);
    }
}
