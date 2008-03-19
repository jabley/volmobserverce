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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.assets;

/**
 * An asset reference that can wrap a causative exception.  This is achieved by
 * specialising <code>ExtendedException</code>.
 */
public class AssetReferenceException extends Exception {

    /**
     * Initialize the new instance.
     */
    public AssetReferenceException() {
        super();
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message the exception message string
     */
    public AssetReferenceException(String message) {
        super(message);
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message   the exception message string
     * @param throwable the underlying exception being wrapped
     */
    public AssetReferenceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param throwable the underlying exception being wrapped
     */
    public AssetReferenceException(Throwable throwable) {
        super(throwable);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 ===========================================================================
*/
