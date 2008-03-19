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

package com.volantis.mcs.policies.impl.io;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.mcs.themes.CSSParserMode;

public class DeSerializer {

    /**
     * A mapping between {@link VariablePolicyType} and string.
     */
    private static final Object2StringMap VARIABLE_POLICY_TYPE;

    static {
        VARIABLE_POLICY_TYPE = new Object2StringMap();
        VARIABLE_POLICY_TYPE.map(PolicyType.AUDIO, "audio");
        VARIABLE_POLICY_TYPE.map(PolicyType.CHART, "chart");
        VARIABLE_POLICY_TYPE.map(PolicyType.IMAGE, "image");
        VARIABLE_POLICY_TYPE.map(PolicyType.LAYOUT, "layout");
        VARIABLE_POLICY_TYPE.map(PolicyType.LINK, "link");
        VARIABLE_POLICY_TYPE.map(PolicyType.RESOURCE, "resource");
        VARIABLE_POLICY_TYPE.map(PolicyType.SCRIPT, "script");
        VARIABLE_POLICY_TYPE.map(PolicyType.TEXT, "text");
        VARIABLE_POLICY_TYPE.map(PolicyType.THEME, "theme");
        VARIABLE_POLICY_TYPE.map(PolicyType.VIDEO, "video");
    }

    public static String variablePolicyTypeToString(VariablePolicyType type) {
        return VARIABLE_POLICY_TYPE.object2String(type);
    }

    public static VariablePolicyType stringToVariablePolicyType(String string) {
        return (VariablePolicyType) VARIABLE_POLICY_TYPE.string2Object(string);
    }

    /**
     * A mapping between {@link VariablePolicyType} and string.
     */
    private static final Object2StringMap POLICY_TYPE;

    static {
        POLICY_TYPE = new Object2StringMap();
        POLICY_TYPE.map(PolicyType.AUDIO, "audio");
        POLICY_TYPE.map(PolicyType.BASE_URL, "base-url");
        POLICY_TYPE.map(PolicyType.BUTTON_IMAGE, "button");
        POLICY_TYPE.map(PolicyType.CHART, "chart");
        POLICY_TYPE.map(PolicyType.IMAGE, "image");
        POLICY_TYPE.map(PolicyType.LAYOUT, "layout");
        POLICY_TYPE.map(PolicyType.LINK, "link");
        POLICY_TYPE.map(PolicyType.RESOURCE, "resource");
        POLICY_TYPE.map(PolicyType.ROLLOVER_IMAGE, "rollover");
        POLICY_TYPE.map(PolicyType.SCRIPT, "script");
        POLICY_TYPE.map(PolicyType.TEXT, "text");
        POLICY_TYPE.map(PolicyType.THEME, "theme");
        POLICY_TYPE.map(PolicyType.VIDEO, "video");
    }

    public static String policyTypeToString(PolicyType type) {
        return POLICY_TYPE.object2String(type);
    }

    public static PolicyType stringToPolicyType(String string) {
        return (PolicyType) POLICY_TYPE.string2Object(string);
    }

    /**
     * A mapping between {@link VariantType} and string.
     */
    private static final Object2StringMap VARIANT_TYPE;

    static {
        VARIANT_TYPE = new Object2StringMap();
        VARIANT_TYPE.map(VariantType.AUDIO, "audio");
        VARIANT_TYPE.map(VariantType.CHART, "chart");
        VARIANT_TYPE.map(VariantType.IMAGE, "image");
        VARIANT_TYPE.map(VariantType.LAYOUT, "layout");
        VARIANT_TYPE.map(VariantType.LINK, "link");
        VARIANT_TYPE.map(VariantType.NULL, "null");
        VARIANT_TYPE.map(VariantType.SCRIPT, "script");
        VARIANT_TYPE.map(VariantType.TEXT, "text");
        VARIANT_TYPE.map(VariantType.THEME, "theme");
        VARIANT_TYPE.map(VariantType.VIDEO, "video");
    }

    public static String variantTypeToString(VariantType type) {
        return VARIANT_TYPE.object2String(type);
    }

    public static VariantType stringToVariantType(String string) {
        return (VariantType) VARIANT_TYPE.string2Object(string);
    }

    /**
     * A mapping between {@link AudioEncoding} and string.
     */
    private static final Object2StringMap AUDIO_ENCODINGS;

    static {
        AUDIO_ENCODINGS = new Object2StringMap();
        AUDIO_ENCODINGS.map(AudioEncoding.ADPCM32, "adpcm32");
        AUDIO_ENCODINGS.map(AudioEncoding.AMR, "amr");
        AUDIO_ENCODINGS.map(AudioEncoding.BASIC, "basic");
        AUDIO_ENCODINGS.map(AudioEncoding.GSM, "gsm");
        AUDIO_ENCODINGS.map(AudioEncoding.IMELODY, "imelody");
        AUDIO_ENCODINGS.map(AudioEncoding.MIDI, "midi");
        AUDIO_ENCODINGS.map(AudioEncoding.MP3, "mp3");
        AUDIO_ENCODINGS.map(AudioEncoding.NOKIA_RING_TONE, "nokia-ring-tone");
        AUDIO_ENCODINGS.map(AudioEncoding.REAL, "real");
        AUDIO_ENCODINGS.map(AudioEncoding.RMF, "rmf");
        AUDIO_ENCODINGS.map(AudioEncoding.SMAF, "smaf");
        AUDIO_ENCODINGS.map(AudioEncoding.SP_MIDI, "sp-midi");
        AUDIO_ENCODINGS.map(AudioEncoding.WAV, "wav");
        AUDIO_ENCODINGS.map(AudioEncoding.WINDOWS_MEDIA, "windows-media");
    }

    public static String audioEncodingToString(AudioEncoding encoding) {
        return AUDIO_ENCODINGS.object2String(encoding);
    }

    public static AudioEncoding stringToAudioEncoding(String string) {
        return (AudioEncoding) AUDIO_ENCODINGS.string2Object(string);
    }

    /**
     * A mapping between {@link ChartType} and string.
     */
    private static final Object2StringMap CHART_TYPE;

    static {
        CHART_TYPE = new Object2StringMap();
        CHART_TYPE.map(ChartType.BAR, "bar");
        CHART_TYPE.map(ChartType.COLUMN, "column");
        CHART_TYPE.map(ChartType.LEGEND, "legend");
        CHART_TYPE.map(ChartType.LINE, "line");
        CHART_TYPE.map(ChartType.PIE, "pie");
    }

    public static String chartTypeToString(ChartType mode) {
        return CHART_TYPE.object2String(mode);
    }

    public static ChartType stringToChartType(String string) {
        return (ChartType) CHART_TYPE.string2Object(string);
    }

    /**
     * A mapping between {@link ImageConversionMode} and string.
     */
    private static final Object2StringMap IMAGE_CONVERSION_MODE;

    static {
        IMAGE_CONVERSION_MODE = new Object2StringMap();
        IMAGE_CONVERSION_MODE.map(ImageConversionMode.ALWAYS_CONVERT, "always");
        IMAGE_CONVERSION_MODE.map(ImageConversionMode.NEVER_CONVERT, "never");
    }

    public static String imageConversionModeToString(ImageConversionMode mode) {
        return IMAGE_CONVERSION_MODE.object2String(mode);
    }

    public static ImageConversionMode stringToImageConversionMode(
            String string) {
        return (ImageConversionMode) IMAGE_CONVERSION_MODE.string2Object(
                string);
    }

    /**
     * A mapping between {@link ImageEncoding} and string.
     */
    private static final Object2StringMap IMAGE_ENCODINGS;

    static {
        IMAGE_ENCODINGS = new Object2StringMap();
        IMAGE_ENCODINGS.map(ImageEncoding.BMP, "bmp");
        IMAGE_ENCODINGS.map(ImageEncoding.GIF, "gif");
        IMAGE_ENCODINGS.map(ImageEncoding.JPEG, "jpeg");
        IMAGE_ENCODINGS.map(ImageEncoding.PJPEG, "pjpeg");
        IMAGE_ENCODINGS.map(ImageEncoding.PNG, "png");
        IMAGE_ENCODINGS.map(ImageEncoding.TIFF, "tiff");
        IMAGE_ENCODINGS.map(ImageEncoding.VIDEOTEX, "videotex");
        IMAGE_ENCODINGS.map(ImageEncoding.WBMP, "wbmp");
    }

    public static String imageEncodingToString(ImageEncoding encoding) {
        return IMAGE_ENCODINGS.object2String(encoding);
    }

    public static ImageEncoding stringToImageEncoding(String string) {
        return (ImageEncoding) IMAGE_ENCODINGS.string2Object(string);
    }

    /**
     * A mapping between {@link ImageRendering} and string.
     */
    private static final Object2StringMap IMAGE_RENDERING;

    static {
        IMAGE_RENDERING = new Object2StringMap();
        IMAGE_RENDERING.map(ImageRendering.COLOR, "color");
        IMAGE_RENDERING.map(ImageRendering.GRAYSCALE, "grayscale");
    }

    public static String imageRenderingToString(ImageRendering mode) {
        return IMAGE_RENDERING.object2String(mode);
    }

    public static ImageRendering stringToImageRendering(String string) {
        return (ImageRendering) IMAGE_RENDERING.string2Object(string);
    }

    /**
     * A mapping between {@link ScriptEncoding} and string.
     */
    private static final Object2StringMap SCRIPT_ENCODINGS;

    static {
        SCRIPT_ENCODINGS = new Object2StringMap();
        SCRIPT_ENCODINGS.map(ScriptEncoding.JAVASCRIPT, "javascript");
        SCRIPT_ENCODINGS.map(ScriptEncoding.JAVASCRIPT_1_0, "javascript1.0");
        SCRIPT_ENCODINGS.map(ScriptEncoding.JAVASCRIPT_1_1, "javascript1.1");
        SCRIPT_ENCODINGS.map(ScriptEncoding.JAVASCRIPT_1_2, "javascript1.2");
        SCRIPT_ENCODINGS.map(ScriptEncoding.JAVASCRIPT_1_3, "javascript1.3");
        SCRIPT_ENCODINGS.map(ScriptEncoding.JAVASCRIPT_1_4, "javascript1.4");
        SCRIPT_ENCODINGS.map(ScriptEncoding.WML_TASK, "WML-task");
    }

    public static String scriptEncodingToString(ScriptEncoding encoding) {
        return SCRIPT_ENCODINGS.object2String(encoding);
    }

    public static ScriptEncoding stringToScriptEncoding(String string) {
        return (ScriptEncoding) SCRIPT_ENCODINGS.string2Object(string);
    }

    /**
     * A mapping between {@link TextEncoding} and string.
     */
    private static final Object2StringMap TEXT_ENCODINGS;

    static {
        TEXT_ENCODINGS = new Object2StringMap();
        TEXT_ENCODINGS.map(TextEncoding.FORM_VALIDATOR, "form-validator");
        TEXT_ENCODINGS.map(TextEncoding.PLAIN, "plain");
        TEXT_ENCODINGS.map(TextEncoding.VOICE_XML_ERROR, "voice-xml-error");
        TEXT_ENCODINGS.map(TextEncoding.VOICE_XML_HELP, "voice-xml-help");
        TEXT_ENCODINGS.map(TextEncoding.VOICE_XML_NUANCE_GRAMMAR,
                "voice-xml-nuance-grammar");
        TEXT_ENCODINGS.map(TextEncoding.VOICE_XML_PROMPT, "voice-xml-prompt");
    }

    public static String textEncodingToString(TextEncoding encoding) {
        return TEXT_ENCODINGS.object2String(encoding);
    }

    public static TextEncoding stringToTextEncoding(String string) {
        return (TextEncoding) TEXT_ENCODINGS.string2Object(string);
    }

    /**
     * A mapping between {@link VideoEncoding} and string.
     */
    private static final Object2StringMap VIDEO_ENCODINGS;

    static {
        VIDEO_ENCODINGS = new Object2StringMap();
        VIDEO_ENCODINGS.map(VideoEncoding.ANIMATED_GIF, "animated-gif");
        VIDEO_ENCODINGS.map(VideoEncoding.MACROMEDIA_FLASH,
                "macromedia-flash");
        VIDEO_ENCODINGS.map(VideoEncoding.MACROMEDIA_SHOCKWAVE,
                "macromedia-shockwave");
        VIDEO_ENCODINGS.map(VideoEncoding.MPEG1, "mpeg1");
        VIDEO_ENCODINGS.map(VideoEncoding.MPEG4, "mpeg4");
        VIDEO_ENCODINGS.map(VideoEncoding.QUICKTIME, "quicktime");
        VIDEO_ENCODINGS.map(VideoEncoding.REAL, "real");
        VIDEO_ENCODINGS.map(VideoEncoding.TV, "tv");
        VIDEO_ENCODINGS.map(VideoEncoding.THREE_GPP, "3gpp");
        VIDEO_ENCODINGS.map(VideoEncoding.WINDOWS_MEDIA, "windows-media");
    }

    public static String videoEncodingToString(VideoEncoding encoding) {
        return VIDEO_ENCODINGS.object2String(encoding);
    }

    public static VideoEncoding stringToVideoEncoding(String string) {
        return (VideoEncoding) VIDEO_ENCODINGS.string2Object(string);
    }

    /**
     * A mapping between {@link BaseLocation} and string.
     */
    private static final Object2StringMap BASE_LOCATION;

    static {
        BASE_LOCATION = new Object2StringMap();
        BASE_LOCATION.map(BaseLocation.DEVICE, "device");
        BASE_LOCATION.map(BaseLocation.DEFAULT, "default");
        BASE_LOCATION.map(BaseLocation.HOST, "host");
        BASE_LOCATION.map(BaseLocation.CONTEXT, "context");
    }

    public static String baseLocationToString(BaseLocation location) {
        return BASE_LOCATION.object2String(location);
    }

    public static BaseLocation stringToBaseLocation(String string) {
        return (BaseLocation) BASE_LOCATION.string2Object(string);
    }

    /**
     * A mapping between {@link CSSParserMode} and string.
     */
    private static final Object2StringMap CSS_PARSER_MODE;

    static {
        CSS_PARSER_MODE = new Object2StringMap();
        CSS_PARSER_MODE.map(CSSParserMode.LAX, "lax");
        CSS_PARSER_MODE.map(CSSParserMode.STRICT, "strict");
    }

    public static String parserModeToString(CSSParserMode location) {
        return CSS_PARSER_MODE.object2String(location);
    }

    public static CSSParserMode stringToParserMode(String string) {
        return (CSSParserMode) CSS_PARSER_MODE.string2Object(string);
    }

    public static void main(String[] args) {

    }
}
