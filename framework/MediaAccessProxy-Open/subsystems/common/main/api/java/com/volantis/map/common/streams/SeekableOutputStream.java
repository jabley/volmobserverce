/* ----------------------------------------------------------------------------
 * Copyright Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.common.streams;

import javax.imageio.stream.ImageOutputStream;

/**
 * Simple wrapper for ImageOutputStreams as we are not always dealing with
 * images when we use them.
 *
 */
public interface SeekableOutputStream extends ImageOutputStream {
}
