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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * A Transfer for transfering arrays of Strings.
 */
public class StringArrayTransfer extends ByteArrayTransfer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(StringArrayTransfer.class);

    /**
     * Constant containing the name of the type for StringArrayTransfer.
     */
    private final static String TYPE_NAME = "String[]";

    /**
     * The type ids for StringArrayTransfer.
     */
    private final int[] typeIDs;

    /**
     * The singleon instance.
     */
    private static StringArrayTransfer SINGLETON = new StringArrayTransfer();

    /**
     * The singleton getInstance() method to be consistent with Eclipse
     * implementations of ByteArrayTransfer.
     */
    public static StringArrayTransfer getInstance() {
        return SINGLETON;
    }

    /**
     * The private constructor.
     */
    private StringArrayTransfer() {
        typeIDs = new int[]{registerType(TYPE_NAME)};
    }


    // javadoc inherited
    protected String[] getTypeNames() {
        return new String[]{TYPE_NAME};
    }

    // javadoc inherited
    protected int[] getTypeIds() {
        return typeIDs;
    }

    // javadoc inherited
    public void javaToNative(Object object, TransferData transferData) {
        if (logger.isDebugEnabled()) {
            logger.debug("javaToNative: object=" + object +
                    ", transferData=" + transferData);
            logger.debug("javaToNative: transferData supported=" +
                    isSupportedType(transferData));
            logger.debug("javaToNative: object class=" + object.getClass());
        }

        if (object != null && object instanceof String[]) {
            if (isSupportedType(transferData)) {
                String[] strings = (String[]) object;
                try {
                    // Write data to a byte array and then ask super to convert
                    // to pMedium (copied this java doc - don't know what
                    // pmedium is!)
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(strings);
                    oos.close();
                    out.close();

                    super.javaToNative(out.toByteArray(), transferData);

                } catch (IOException e) {
                    throw new UndeclaredThrowableException(e);
                }
            }
        }
    }

    // javadoc inherited
    public Object nativeToJava(TransferData transferData) {
        if (logger.isDebugEnabled()) {
            logger.debug("nativeToJava: transferData=" + transferData);
            logger.debug("nativeToJava: transferData supported=" +
                    isSupportedType(transferData));
        }
        String[] strings = null;
        if (isSupportedType(transferData)) {

            byte[] buffer = (byte[]) super.nativeToJava(transferData);

            if (buffer != null) {
                ByteArrayInputStream baim = new ByteArrayInputStream(buffer);
                try {
                    ObjectInputStream ois = new ObjectInputStream(baim);
                    strings = (String[]) ois.readObject();
                } catch (IOException e) {
                    logger.error("unexpected-ioexception", e);
                    throw new UndeclaredThrowableException(e);
                } catch (ClassNotFoundException e) {
                    logger.error("unexpected-exception", e);
                    throw new UndeclaredThrowableException(e);
                }
            }
        }
        return strings;
    }

    // javadoc inherited
    public boolean isSupportedType() {
        return true;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 04-Feb-05	6749/5	allan	VBM:2005012102 Rework issues

 03-Feb-05	6749/3	allan	VBM:2005012102 Rework issues

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
