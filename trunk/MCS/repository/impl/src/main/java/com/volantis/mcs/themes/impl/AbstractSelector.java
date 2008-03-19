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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.ThemeVisitor;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.jibx.JiBXSourceLocation;
import com.volantis.mcs.model.jibx.JiBXBase;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.xdime.XDIMESchemata;

public abstract class AbstractSelector
        extends JiBXBase
        implements Selector {

    /**
     * Placeholder for validation of XML tokens.
     */
    private static final String TOKEN_INITIAL =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

    /**
     * Placeholder for validation of XML tokens.
     */
    private static final String TOKEN_SUBSEQUENT =
            TOKEN_INITIAL + "0123456789.-";

    // Javadoc inherited.
    public void accept(ThemeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Validates a token to ensure that it is legal.
     *
     * @param token The token name to validate
     * @return True if the token name is valid, false otherwise
     */
    protected boolean isValidToken(String token) {
        boolean valid = true;
        if (token.length() > 0) {
            if (TOKEN_INITIAL.indexOf(token.charAt(0)) == -1) {
                valid = false;
            } else {
                for (int i = 1; valid && i < token.length(); i++) {
                    if (TOKEN_SUBSEQUENT.indexOf(token.charAt(i)) == -1) {
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

    /**
     * Validate that the namespace prefix is valid.
     *
     * @param context         The validation context.
     * @param identifier      The identifier of the property with which
     *                        diagnostics should be associated.
     * @param namespacePrefix The prefix value to validate.
     */
    protected void validateNamespacePrefix(
            ValidationContext context, final PropertyIdentifier identifier,
            final String namespacePrefix) {

        // Namespace prefixes are always optional.
        if (namespacePrefix == null) {
            return;
        }

        Step step = context.pushPropertyStep(identifier);
        if (!isValidNamespacePrefix(namespacePrefix)) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                     context.createMessage("theme-invalid-namespace",
                             namespacePrefix));
        }
        context.popStep(step);
    }

    /**
     * Check to see whether the prefix is a valid namespace or not.
     *
     * @param namespacePrefix The prefix to check.
     * @return True if it is valid, false otherwise.
     */
    private boolean isValidNamespacePrefix(String namespacePrefix) {
        return namespacePrefix.equals("*")
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_CDM_PREFIX)
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_XFORMS_PREFIX)
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_XHTML2_PREFIX)
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_WIDGETS_PREFIX)
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_RESPONSE_PREFIX)
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_TICKER_PREFIX)
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_TICKER_RESPONSE_PREFIX)
        || namespacePrefix.equals(XDIMESchemata.DEFAULT_GALLERY_PREFIX);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 ===========================================================================
*/
