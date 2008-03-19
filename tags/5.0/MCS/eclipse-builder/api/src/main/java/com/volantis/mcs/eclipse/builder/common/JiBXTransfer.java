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
package com.volantis.mcs.eclipse.builder.common;

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.shared.content.BinaryContentInput;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

/**
 * A common superclass for transfer classes using JiBX to write data.
 */
public abstract class JiBXTransfer extends ByteArrayTransfer {
    // Javadoc inherited
    public void javaToNative(Object object, TransferData transferData) {
        if (object != null || !(object instanceof Object[]) || !isValidArrayType(object)) {
            if (isSupportedType(transferData)) {
                Object[] objects = (Object[]) object;
                try {
                    // write data to a byte array and then ask super to convert to pMedium
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    // Write out the count for the number of objects to write
                    oos.writeObject(new Integer(objects.length));
                    JiBXWriter writer = new JiBXWriter();

                    for (int i = 0; i < objects.length; i++) {
                        byte[] objectBuffer = objectToByteArray(writer, objects[i]);
                        oos.writeObject(objectBuffer);
                    }
                    oos.flush();
                    byte[] buffer = out.toByteArray();
                    oos.close();

                    super.javaToNative(buffer, transferData);
                } catch (IOException ioe) {
                    // There was an error copying the data
                    EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                            getClass(), ioe);
                } catch (RepositoryException re) {
                    // There was an error copying the data
                    EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                            getClass(), re);
                }
            }
        }
    }

    protected abstract boolean isValidArrayType(Object object);

    // Javadoc inherited
    public Object nativeToJava(TransferData transferData) {
        Object[] readArray = null;
        if (isSupportedType(transferData)) {
            byte[] buffer = (byte[]) super.nativeToJava(transferData);
            if (buffer == null) return null;

            try {
                ByteArrayInputStream in = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = new ObjectInputStream(in);
                Integer bufferCount = (Integer) ois.readObject();
                Object[] tempRead = new Object[bufferCount.intValue()];
                JiBXReader reader = getJiBXReader();
                for (int i = 0; i < bufferCount.intValue(); i++) {
                    byte[] variantBuffer = (byte[]) ois.readObject();
                    tempRead[i] = byteArrayToObject(reader, variantBuffer);
                }
                readArray = tempRead;
                ois.close();
            } catch (IOException ioe) {
                // There was an error copying the data
                EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                        getClass(), ioe);
            } catch (ClassNotFoundException cnfe) {
                // There was an error copying the data
                EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                        getClass(), cnfe);
            }
        }
        return readArray;
    }

    protected Object byteArrayToObject(JiBXReader reader, byte[] buffer)
            throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        return reader.read(new BinaryContentInput(bais), "internal");
    }

    protected byte[] objectToByteArray(JiBXWriter writer, Object object)
            throws RepositoryException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter out = new OutputStreamWriter(baos);
        writer.write(out, object);
        return baos.toByteArray();
    }

    protected JiBXReader getJiBXReader() {
        return new JiBXReader(ThemeFactory.getDefaultInstance().getRuleClass());
    }
}
