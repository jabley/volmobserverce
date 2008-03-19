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
package com.volantis.mcs.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Implementation of UserInterface which directs IO to the console
 */
class ConsoleUserInterface implements UserInterface {

    // Javadoc inherited
    public void reportError(String message) {
        System.err.println(message);
        System.err.flush();
    }

    // Javadoc inherited
    public void reportException(Throwable throwable) {
        throwable.printStackTrace(System.err);
        System.err.flush();
    }

    // Javadoc inherited
    public void reportStatus(String message) {
        System.out.println(message);
        System.out.flush();
    }

    // Javadoc inherited
    public String getInputLine() {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

        String line = null;

        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return line;
    }



}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10413/5	pabbott	VBM:2005111811 Provide Exporter public API

 ===========================================================================
*/
