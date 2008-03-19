/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/** (c) Volantis Systems Ltd 2004.  */
package com.volantis.mcs.project.impl.xml;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.impl.xml.XMLPolicyBuilderAccessor;
import com.volantis.mcs.policies.impl.xml.XMLPolicyBuilderReader;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.mcs.project.impl.LocalPolicySourceImpl;
import com.volantis.mcs.project.xml.XMLPolicySource;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.File;


/**
 * A PolicySource for an XML policy which defines the directory for
 * the policy.
 */
public class XMLPolicySourceImpl
        extends LocalPolicySourceImpl
        implements XMLPolicySource {

    /**
     * The accessor to use for accessing policies in any XML source.
     */
    private static final XMLPolicyBuilderAccessor ACCESSOR =
            new XMLPolicyBuilderAccessor();

    /**
     * The reader to use for accessing policies in any XML source.
     */
    private static final XMLPolicyBuilderReader READER =
            new XMLPolicyBuilderReader(ACCESSOR);


    /**
     * The directory for this XML policy source.
     */
    private String directory;

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XMLPolicySourceImpl.class);

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param directory The directory name for the policy source
     */
    public XMLPolicySourceImpl(
            LocalRepository repository,
            String directory) {
        super(repository, ACCESSOR, READER);

        File f = new File(directory);
        if (!f.isAbsolute()) {
            // warn that this has no guarantee that it will work
            logger.warn("dangerous-path");
        }
        // For backwards compatibility, set even if f is not a valid file
        this.directory = directory;
    }

    // Javadoc inherited
    public boolean equals(Object obj) {
        if (!(obj instanceof XMLPolicySourceImpl)) {
            return false;
        }
        return getDirectory().equals(
                ((XMLPolicySource) obj).getDirectory());
    }

    // Javadoc inherited
    public int hashCode() {
        return getClass().hashCode() + getDirectory().hashCode();
    }

    /**
     * Get the directory name
     *
     * @return The directory name.
     */
    public String getDirectory() {
        return directory;
    }

    // Javadoc inherited.
    public PolicyBuilderManager createPolicyBuilderManager(
            InternalProject project) {
        InternalPolicyFactory factory =
                InternalPolicyFactory.getInternalInstance();
        return factory.createXMLPolicyBuilderManager(project);
    }

    //javdoc inherited
    public String toString() {
        return "[directory=" + directory + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 15-Feb-05	6974/3	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	3090/1	ianw	VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

 27-Jan-04	2769/3	mat	VBM:2004012702 Added testcases

 27-Jan-04	2769/1	mat	VBM:2004012702 Add PolicySource

 ===========================================================================
*/
