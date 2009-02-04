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
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.mcs.integration.TestURLRewriter;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.mcs.protocols.DefaultStyleSheetHandler;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.configuration.WMLOutputPreference;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.selection.VariantSelectionPolicy;
import junitx.util.PrivateAccessor;

import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Test Volantis object.
 */
public class TestVolantis extends Volantis {

    private VariantSelectionPolicy variantSelectionPolicy;
    private ProtocolsConfiguration protocolsConfiguration;
    private PolicyReferenceFactory policyReferenceFactory;
    private RuntimeProject defaultProject;

    // Javadoc inherited
    public TestVolantis() {
        super();
        try {
            PrivateAccessor.setField(this, "pageGenerationCache",
                new PageGenerationCache());

                        // Initialise the DefaultStyleSheet
            InputStream cssInputStream =
                Volantis.class.getResourceAsStream("default.css");

            defaultStyleSheet =
                    DefaultStyleSheetHandler.compileStyleSheet(cssInputStream);
        } catch (NoSuchFieldException e) {
            throw new UndeclaredThrowableException(e);
        }
    }


    // Javadoc inherited
    public URLRewriter getURLRewriter() {
        return new TestURLRewriter();
    }

    /**
     * Test method to set the header message directly.
     *
     * @param message
     */
    public void setPageHeadingMsg(String message) {
        try {
            PrivateAccessor.setField(this, "vtPageHeadingMsg", message);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    // Javadoc inherited
    public VariantSelectionPolicy getVariantSelectionPolicy() {
        return variantSelectionPolicy;
    }

    // Javadoc inherited
    public void setVariantSelectionPolicy(VariantSelectionPolicy policy) {
        variantSelectionPolicy = policy;
    }

    // Javadoc inherited
    public PageTrackerFactory getPageTrackerFactory() {
        return null;
    }

    // Javadoc inherited
    public ProtocolsConfiguration getProtocolsConfiguration() {
        if (protocolsConfiguration == null) {
            protocolsConfiguration = new ProtocolsConfiguration();
            protocolsConfiguration.setPreferredOutputFormat(
                WMLOutputPreference.WMLC);
        }
        return protocolsConfiguration;
    }

    public void setPolicyReferenceFactory(
            PolicyReferenceFactory policyReferenceFactory) {
        this.policyReferenceFactory = policyReferenceFactory;
    }

    public PolicyReferenceFactory getPolicyReferenceFactory() {
        return policyReferenceFactory;
    }

    public RuntimeProject getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(RuntimeProject defaultProject) {
        this.defaultProject = defaultProject;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10343/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 16-Nov-05	10343/1	ianw	VBM:2005111405 Phase 1 performance improvements

 10-Aug-05	9211/6	pabbott	VBM:2005080902 End to End CSS emulation test

 10-Aug-05	9211/3	pabbott	VBM:2005080902 End to End CSS emulation test

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/
