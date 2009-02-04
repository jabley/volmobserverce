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

package com.volantis.mcs.interaction.impl.validation;

import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.Validator;

import java.util.List;

/**
 */
public class ProxyValidator {

    private static final ModelFactory MODEL_FACTORY =
            ModelFactory.getDefaultInstance();

    private static final Prepare4Validation PREPARE_4_VALIDATION =
            new Prepare4Validation();

    public void validate(Proxy proxy) {

        // Prepare each proxy for validation.
        PREPARE_4_VALIDATION.prepare(proxy);

        // Validate the model.
        Validator validator = MODEL_FACTORY.createValidator();
        Validatable validatable = (Validatable) proxy.getModelObject();
        validator.validate(validatable);

        // Get the diagnostics and iterate over them associating them with
        // the relevant proxy.
        List diagnostics = validator.getDiagnostics();
        for (int i = 0; i < diagnostics.size(); i++) {
            Diagnostic diagnostic = (Diagnostic) diagnostics.get(i);
            Path path = diagnostic.getPath();
            InternalProxy internalProxy = (InternalProxy)
                    proxy.getEnclosingProxy(path);
            if (internalProxy == null) {
                // System.out.println("Unknown path: " + path.getAsString());
            } else {
                ProxyDiagnostic proxyDiagnostic = new ProxyDiagnosticImpl(
                        diagnostic.getLevel(), internalProxy,
                        diagnostic.getMessage());
                internalProxy.addDiagnostic(proxyDiagnostic);
            }
        }

        // Walk back over the proxies generating events if the diagnostics
        // have changed.
        FinishValidation finish = new FinishValidation();
        finish.finish(proxy);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
