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
 * (c) Copyright Volantis Systems Ltd. 2007.
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.common.param;

public class ParameterNames {

    private ParameterNames() {
        // hide the constructor
    }

    /**
     * The default dither mode parameter
     */
    public static final String DEFAULT_DITHER_MODE = "DefaultDitherMode";

    public static final String DITHER_MODE_16_BIT = "DitherMode16Bit";

    public static final String DITHER_MODE_1_BIT = "DitherMode1Bit";

    public static final String DITHER_MODE_2_BIT = "DitherMode2Bit";

    public static final String DITHER_MODE_4_BIT = "DitherMode4Bit";

    public static final String DITHER_MODE_8_BIT = "DitherMode8Bit";

    public static final String GIF_ENABLED = "IsGifEnabled";

    public static final String PROXY_NAME = "Proxy";

    public static final String PROXY_PORT = "ProxyPort";

    public static final String MAXIMUM_REDIRECTS = "MaxRedirects";

    public static final String JPEG_MODE = "JpegMode";

    public static final String MINIMUM_BIT_DEPTH = "MinimumBitDepth";

    public static final String SCALE_LARGER = "CanScaleLarger";

    public static final String SCALE_MODE = "ScaleMode";

    public static final String IMAGE_WIDTH = "ImageWidth";

    public static final String IMAGE_HEIGHT = "ImageHeight";

    public static final String MAX_IMAGE_SIZE = "MaxImageSize";

    public static final String SOURCE_IMAGE_SIZE = "InputImageSize";

    public static final String IN_MEMORY_IO_CACHE = "UseInMemoryIOCache";

    public static final String PRESERVE_X_LEFT = "PreserveXLeft";

    public static final String PRESERVE_X_RIGHT = "PreserveXRight";

    public static final String CLIP_AREA = "ClipArea";

    public static final String DEVICE_REPOSITORY_URL = "DeviceRepositoryURL";

    public static final String MINIMUM_JPEG_QUALITY = "MinimumJPEGQuality";

    public static final String DESTINATION_FORMAT_RULE = "DestinationFormatRule";

    public static final String SUPPORTED_IMAGES = "SupportedImages";    

    public static final String BOTTOM_Y = "BottomY";

    public static final String TOP_Y = "TopY";

    public static final String LEFT_X = "LeftX";

    public static final String RIGHT_X = "RightX";

    /**
     * Key used to store a ImageInputStream containing the watermark image.
     */
    public static final String WATERMARK_INPUT_STREAM = "WatermarkInputStream";

    /**
     * Key used to store the RenderedOp containing the scale watermark
     */
    public static final String WATERMARK_IMAGE = "WatermarkImage";

    /**
     * Key used to store the String url of the wartermark.
     */
    public static final String WATERMARK_URL = "WatermarkURL";

    public static final String SOURCE_IMAGE_MIME_TYPE = "InputImageMIMEType";

    public static final String OUTPUT_IMAGE_MIME_TYPE = "OutputImageMIMEType";

    public static final String VIDEO_VISUAL_CODEC = "VideoVisualCodec";

    public static final String VIDEO_AUDIO_CODEC = "VideoAudioCodec";

    public static final String VIDEO_FRAME_RATE = "VideoFrameRate";

    public static final String VIDEO_WIDTH = "VideoWidth";

    public static final String VIDEO_HEIGHT = "VideoHeight";

    public static final String MAX_VIDEO_SIZE = "MaxVideoSize";

    public static final String MAX_VIDEO_VISUAL_BIT_RATE = "MaxVideoVisualBitRate";

    public static final String MAX_VIDEO_AUDIO_BIT_RATE = "MaxVideoVisualBitRate";

    public static final String MAX_AUDIO_SIZE = "MaxAudioSize";

    public static final String AUDIO_CODEC = "AudioCodec";

    public static final String MAX_AUDIO_BIT_RATE = "MaxAudioBitRate";


    /**
     * The source of asset. If not set, will be recreated
     * from the below params
     */
    public static final String SOURCE_URL = "SourceURL";

    /**
     * The protocol used to communicate to obtain the source asset.
     * Will be used to recreate source URL if SOURCE_URL is not set
     */
    public static final String SOURCE_PROTOCOL = "SourceProtocol";

    /**
     * The source userInfo parameter. Will be used to recreate
     * source URL if SOURCE_URL is not set
     */
    public static String SOURCE_USER_INFO = "SourceUserInfo";

    /**
     * The source host parameter. Will be used to recreate
     * source URL if SOURCE_URL is not set
     */
    public static final String SOURCE_HOST = "SourceHost";

    /**
     * The source port parameter. Will be used to recreate
     * source URL if SOURCE_URL is not set
     */
    public static final String SOURCE_PORT = "SourcePort";

    /**
     * The source path parameter. Will be used to recreate
     * source URL if SOURCE_URL is not set 
     */
    public static final String SOURCE_PATH = "SourcePath";

    /**
     * The source fragment parameter. Will be used to recreate
     * source URL if SOURCE_URL is not set
     */
    public static final String SOURCE_FRAGMENT = "SourceFragment";

    /**
     * The source query parameter. Will be used to recreate
     * source URL if SOURCE_URL is not set.
     *
     * Note: all paramenter names and values stored under this param
     * are assumed to be properly URL encoded. 
     */
    public static final String SOURCE_QUERY = "SourceQuery";

    /**
     * Local path accessible to MAP via filesystem,
     * against which resources should be resolved in case
     * of no source host specified.
     *
     * Runtime parameter set internally by MAP
     */
    public static final String ENV_LOCAL_ROOT = "LocalRoot";
}
