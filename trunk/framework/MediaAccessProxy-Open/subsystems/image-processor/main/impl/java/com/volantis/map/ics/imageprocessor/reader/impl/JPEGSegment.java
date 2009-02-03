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
 * (c) Copyright Volantis Systems Ltd. 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.reader.impl;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

 /**
 * Representation of a JPEGSegment
 */
public class JPEGSegment {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(JPEGSegment.class);

     public static final String ID_JFIF = "JFIF";
     public static final String ID_JFXX = "JFXX";


    private final static int SIZE_OF_MARKER = 2; // 2 bytes
    private final static int SIZE_OF_LENGTH = 2; // 2 bytes

    private JPEGMarker marker = JPEGMarker.UNKNOWN;

    private byte[] data;

    private JPEGSegment() { }

     /**
      * Identifies a segment stored in the provided stream.
      *
      * Modifies stream position.
      */
    public static JPEGMarker identify(ImageInputStream input) throws IOException {

        byte[] markerBytes = new byte[2];
        input.read(markerBytes);

        return identify(markerBytes);
    }

     /**
      * Identifies a segment stored in the provided byte array
      */
     public static JPEGMarker identify(byte[] bytes) {

          // Each segments starts with 0xff followed by the marker
          JPEGMarker marker = null;
          if ((bytes[0] & 0xff) != 0xff) {
              marker = JPEGMarker.UNKNOWN;
          } else {
             marker = JPEGMarker.identify(bytes[1]);
             if (logger.isDebugEnabled()) {
                 if (!marker.isKnown()) {
                     logger.debug("Found unknown marker " + byteToHexString(bytes[1]));
                 }
             }
         }
         return marker;
     }

     /**
      * Reads a segment of specified type from the stream
      *
      * @throws IOException
      */
     public static JPEGSegment read(JPEGMarker marker, ImageInputStream input) throws IOException {

        if (!isSupported(marker)) {
            // This is not a supported segment, so we don't to read it
            return null;
        }

        JPEGSegment segment = new JPEGSegment();
        segment.marker = marker;

        if (isLengthBased(segment.marker)) {
            // Read length
            int length = input.readUnsignedShort();
            // Lenght of JPEG segment includes the two bytes specifying
            // the length itself, but we store just pure data, so
            // we need SIZE_OF_LENGTH bytes less
            segment.data = new byte[length - SIZE_OF_LENGTH];

            input.read(segment.data);
        }
        return segment;
    }

     /**
      * Identifies and read the segment
      */
    public static JPEGSegment read(ImageInputStream input) throws IOException {
        return read(identify(input), input);
    }

     /**
      * Creates a segment of specified type from the provided bytes
      */
    public static JPEGSegment create(JPEGMarker marker, byte[] bytes) {

        if (!isSupported(marker)) {
            // This is not a supported segment, so we don't to read it
            return null;
        }

        JPEGSegment segment = new JPEGSegment();
        segment.marker = marker;

        // Lenght of JPEG segment includes the two bytes specifying
        // the length itself, but we store just pure data, so
        // we need SIZE_OF_LENGTH bytes less
        segment.data = new byte[bytes.length - SIZE_OF_LENGTH];

        System.arraycopy(bytes, 2, segment.data, 0, segment.data.length);

        return segment;
    }


    /**
     * At the moment we support only SOI and variable length
     * segments that specify length in the first two bytes of field
     *
     * @return true if this is a supported segment
     */
    public boolean isSupported() {
        return isSupported(marker);
    }

    /**
     * At the moment we support only SOI and variable length
     * segments that specify length in the first two bytes of field
     *
     * @return true if this is a supported segment
     */     
    public static boolean isSupported(JPEGMarker marker) {
        return isLengthBased(marker)
            || (marker == JPEGMarker.SOI);
    }

     /**
      * Length-based segment is a segment which starts with 2-byte marker,
      * followed by 2-byte length and then by the (length - 2) bytes
      * of the actual data (so in other words the vvalue of lenght includes
      * itself)
      *
      * @return if a segment of the specified type is length-based
      */
    public boolean isLengthBased() {
        return isLengthBased(marker);        
    }

     /**
      * @return if a segment of the specified type is length-based
      */
    public static boolean isLengthBased(JPEGMarker marker) {
        return (marker.isApplicationMarker()
             ||  (marker == JPEGMarker.SOF0)
             ||  (marker == JPEGMarker.DHT)
             ||  (marker == JPEGMarker.DQT)
             ||  (marker == JPEGMarker.COM));
    }

     /**
      * Returns the size of complete segment including marker
      *
      * For length-based segment, size is equal to
      * the length plus 2 bytes for the marker. For marker-only   
      */
    public int getSize() {
        int size = -1;
        if (isLengthBased()) {
            size = (null != data) ? data.length + SIZE_OF_LENGTH + SIZE_OF_MARKER : -1;
        } else {
            size = SIZE_OF_MARKER; 
        }
        return size;
    }

     /**
      * Writes the segment to the specified array assuming BIG_ENDIAN byte order
      *
      * @param out
      * @param offset
      * @return number of bytes written
      */
    public int write(byte[] out, int offset) {

        int bytesWrote = 0;
        // Write marker
        out[offset + bytesWrote++] = (byte)0xff;
        out[offset + bytesWrote++] = marker.byteValue();

        if (isLengthBased()) {
            // Lenght of JPEG segment includes the two bytes specifying
            // the length itself, so we need to add SIZE_OF_LENGTH bytes
            int length = SIZE_OF_LENGTH + data.length;
            // Write length in big endian
            byte high = (byte)((length >> 8) & 0xff);
            byte low = (byte)(length & 0xff);
            out[offset + bytesWrote++] = high;
            out[offset + bytesWrote++] = low;

            // Write data
            System.arraycopy(data, 0, out, offset + bytesWrote, data.length);
            bytesWrote += data.length;
        }
        
        return bytesWrote;
    }

    /**
     * Return id of segment
     *
     * @return String representation of segment id or null if we don't know
     *         how to retrieve id of specific segment
     */
    public String getId() {
        // null will be returned if this type of segment is unsupported
        String result = null;
        if (JPEGMarker.APP0.equals(marker)) {
            result = new String(data, 0, 4);
        }
        return result;
    }

     /**
      * @return marker defining the type of this segment
      */
    public JPEGMarker identify() {
        return marker;
    }

     /**
      * @return true if this segment is of specified type
      */
     public boolean is(JPEGMarker marker) {
         return this.marker == marker;
     }

    private static String byteToHexString(byte b) {
        int i = (b & 0xff);
        String hex = Integer.toHexString(b & 0xff);
        return (i < 0x10) ? "0x0" + hex : "0x" + hex;
    }
}
