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

import com.volantis.map.common.streams.SeekableInputStream;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageInputStreamImpl;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Proxy stream which modifies JPEG file such that is
 * acceptable by JAI / ImageIO libs
 */
public final class JPEGConditioningInputStream extends ImageInputStreamImpl implements SeekableInputStream {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(JPEGConditioningInputStream.class);

    /**
      * Used to localize the messages in exceptions
      */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ImageReader.class);

    /**
     * Underlying stream
     */
    private ImageInputStream input;

    /**
     * Bytes added to the total file length by the conditioning process
     */
    private int patchOverhead = 0;

    /**
     * Patched data replacing data from the original stream
     */
    private byte[] patch;


    /** Fixed, simplest APP0 JFIF segment, injected into JPEGs that miss such a segment */
    private static final JPEGSegment APP0_SEGMENT = JPEGSegment.create(
        JPEGMarker.APP0, new byte[] {
        0, 0x10,                /* Length of APP0 Field  */
        'J', 'F', 'I', 'F', 0,  /* "JFIF" zero terminated Id String */
        1, 2,                   /* JFIF Format Revision (we use 1.02) */
        0,                      /* Units. 0 means we'll define pixels aspect ratio */
        0, 1, 0, 1,             /* Pixel ratio set to 1:1 (square pixels */
        0, 0                    /* Thumbnail dimensions. 0, 0 means no thumbnail */
     });


    private JPEGConditioningInputStream() {
        // not allowed
    }

    public JPEGConditioningInputStream(ImageInputStream originalInput) {
        input = originalInput;
    }

    /**
     * @return true if this stream has been initialized
     */
    private boolean isInitialized() {
        return (null != patch);
    }

    /**
     * Initializes only if not yet initialized
     *
     * @throws IOException
     */
    private void ensureInit() throws IOException {
        if (!isInitialized()) {
            initialize();
        }
    }

    /**
     * Analyzes the input stream and prepares the patch.
     *
     * @throws IOException
     */
    private void initialize() throws IOException {

        if (isInitialized()) {
            throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("already-initialized"));
        }

        int bytesRead = 0;
        boolean hasAPP2 = false;
        boolean hasJFIF = false;

        // Read SOI first
        final JPEGSegment soi = JPEGSegment.read(input);
        if (null == soi || !soi.is(JPEGMarker.SOI)) {
            // It means someone has moved the pointer position
            // of the underlying stream. We're completely lost.
            throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("conditioning-failure"));

        }
        bytesRead += soi.getSize();

        // Read all supported segments
        LinkedList<JPEGSegment> app0Segments = new LinkedList();
        LinkedList<JPEGSegment> otherSegments = new LinkedList();
        JPEGSegment segment = null;
        while ((segment = JPEGSegment.read(input)) != null) {

            if (logger.isDebugEnabled()) {
                logger.debug("Read " + segment.identify() + "  segment at position " + bytesRead);
            }

            if (JPEGMarker.APP0.equals(segment.identify())) {
                // JFIF should be added first
                if (JPEGSegment.ID_JFIF.equals(segment.getId())) {
                    app0Segments.addFirst(segment);
                    hasJFIF = true;
                } else {
                    app0Segments.addLast(segment);
                }
            } else {
                if (JPEGMarker.APP2.equals(segment.identify())) {
                    hasAPP2 = true;
                }
                otherSegments.addLast(segment);
            }
            bytesRead += segment.getSize();
        }
        int bytesToWrite = bytesRead;

        if (!hasJFIF && (hasAPP2 || !app0Segments.isEmpty())) {
            // APP0 and APP2 require that there is APP0 JFIF before,
            // so we need to inject one
            app0Segments.addFirst(APP0_SEGMENT);
            bytesToWrite += APP0_SEGMENT.getSize();
        }

        // Dump out the new segments
        if (logger.isDebugEnabled()) {
            StringBuffer out = new StringBuffer("Segments after conditioning: ");
            out.append(soi.identify()).append(' ');
            for (JPEGSegment s : app0Segments) {
                out.append(s.identify()).append(' ');
            }
            for (JPEGSegment s : otherSegments) {
                out.append(s.identify()).append(' ');
            }
            logger.debug(out);
        }

        // At this point we have read everything we needed from
        // the input stream and are ready to prepare the patch
        patch = new byte[bytesToWrite];

        // First write SOI market
        int bytesWrote = 0;
        bytesWrote += soi.write(patch, bytesWrote);

        // then all APP0 segments (they are already properly ordered)
        for (JPEGSegment s : app0Segments) {
            bytesWrote += s.write(patch, bytesWrote);
        }

        // and finally all APP0 other segments
        for (JPEGSegment s : otherSegments) {
            bytesWrote += s.write(patch, bytesWrote);
        }

        // How much bytes was added by the patch.
        // In current implementation patchOverhead is equal
        // to the length of injected JFIF or zero if
        // no injection happened
        patchOverhead = bytesWrote - bytesRead;

        // Bunch of sanity checks. May need to be modified
        // if the above implementation changes
        if (bytesRead > bytesWrote) {
            // This means we removed some data, which should not happen
            // in the current implementation
            throw new RuntimeException("conditioning-failure");
        }
        if (bytesToWrite != bytesWrote) {
            // It means we lost something
            throw new RuntimeException("conditioning-failure");
        }
    }

    // javadoc inherited
    public int read(byte[] b, int offset, int len) throws IOException {
        checkClosed();
        ensureInit();

        int bytesRead = 0;

        if (streamPos < patch.length) {
            if (streamPos + len <=  patch.length) {
                System.arraycopy(patch, (int)streamPos, b, offset, len);
                bytesRead = len;
                streamPos += bytesRead;
            } else {
                // First read from patch, then from the underlying stream
                int bytesFromPatch = patch.length - (int)streamPos;
                read(b, offset, bytesFromPatch);
                bytesRead = read(b, offset + bytesFromPatch, len - bytesFromPatch);
                if (bytesRead < 0) {
                    bytesRead = bytesFromPatch;
                } else {
                    bytesRead += bytesFromPatch;
                }
            }
        } else {
            // Adjust position to underlying stream
            long adjustedPos = streamPos - patchOverhead;
            if (adjustedPos != input.getStreamPosition()) {
                // need to set the stream pointer at the right position
                input.seek(adjustedPos);
            }
            bytesRead = input.read(b, offset, len);
            if (bytesRead > 0) {
                streamPos += bytesRead;
            }
        }

        return bytesRead;
    }

    // javadoc inherited
    public int read() throws IOException {
        checkClosed();
        ensureInit();

        int result = 0;

        if (streamPos < patch.length) {
            result = ((int)(patch[(int)streamPos]) & 0xff);
        } else {
            // Adjust position to underlying stream
            long adjustedPos = streamPos - patchOverhead;
            if (adjustedPos != input.getStreamPosition()) {
                // need to set the stream pointer at the right position
                input.seek(adjustedPos);
            }
            result = input.read(); 
        }
        streamPos++;

        return result;
    }

    // javadoc inherited
    public long length() {

        long inputLenght = -1L;
        try {
            inputLenght = input.length();

            if (-1L != inputLenght) {
                ensureInit();
                inputLenght = inputLenght + patchOverhead;
            }
        } catch (IOException e) {
            // The right thing to do would be to throw IOException
            // if the underlying stream throws one, but signature of this
            // method in parent class (ImageInputStreamImpl) does not
            // allow it. So we'll return -1
            inputLenght = -1L;
        }
        return inputLenght;
    }

    // javadoc inherited
    public boolean isCachedMemory() {
        // Our patch is cached in memory, so if the underlying stream
        // is cached too, all the data are cached
        return input.isCachedMemory();
    }

    // javadoc inherited
    public boolean isCached() {
        // Our patch is cached in memory, so if the underlying stream
        // is cached too, all the data are cached
        return input.isCached();
    }

    // javadoc inherited
    public boolean isCachedFile() {
        // No, we are - at least partially - cached in memory,
        // not in a file.
        return false;
    }

    // javadoc inherited
    public void close() throws IOException {
        super.close();
        input.close();
    }
}
