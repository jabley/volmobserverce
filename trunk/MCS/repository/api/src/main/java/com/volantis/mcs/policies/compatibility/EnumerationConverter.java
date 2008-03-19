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

package com.volantis.mcs.policies.compatibility;

import com.volantis.mcs.assets.AudioAsset;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.video.VideoEncoding;

import java.util.HashMap;
import java.util.Map;

public class EnumerationConverter {

    public static final IntObjectMap AUDIO_ENCODING;
    public static final Map CHART_TYPE;
    public static final IntObjectMap IMAGE_ENCODING;
    public static final IntObjectMap IMAGE_RENDERING;
    public static final Map SCRIPT_PROGRAMMING_LANGUAGE;
    public static final Map SCRIPT_ENCODING_TO_LANGUAGE;
    public static final IntObjectMap TEXT_ENCODING;
    public static final IntObjectMap VIDEO_ENCODING;

    static {
        IntObjectMap intMap;
        Map map;

        intMap = new IntObjectMap();
        intMap.put(AudioAsset.ADCPM32, AudioEncoding.ADPCM32);
        intMap.put(AudioAsset.AMR_AUDIO, AudioEncoding.AMR);
        intMap.put(AudioAsset.BASIC_AUDIO, AudioEncoding.BASIC);
        intMap.put(AudioAsset.GSM_AUDIO, AudioEncoding.GSM);
        intMap.put(AudioAsset.IMELODY_AUDIO, AudioEncoding.IMELODY);
        intMap.put(AudioAsset.MIDI_AUDIO, AudioEncoding.MIDI);
        intMap.put(AudioAsset.MP3_AUDIO, AudioEncoding.MP3);
        intMap.put(AudioAsset.NOKRING_AUDIO, AudioEncoding.NOKIA_RING_TONE);
        intMap.put(AudioAsset.REAL_AUDIO, AudioEncoding.REAL);
        intMap.put(AudioAsset.RMF_AUDIO, AudioEncoding.RMF);
        intMap.put(AudioAsset.SMAF_AUDIO, AudioEncoding.SMAF);
        intMap.put(AudioAsset.SP_MIDI_AUDIO, AudioEncoding.SP_MIDI);
        intMap.put(AudioAsset.WAV_AUDIO, AudioEncoding.WAV);
        intMap.put(AudioAsset.WINDOWS_MEDIA_AUDIO,
                AudioEncoding.WINDOWS_MEDIA);
        AUDIO_ENCODING = intMap;

        map = new HashMap();
        map.put(ChartAsset.BAR_CHART, ChartType.BAR);
        map.put(ChartAsset.COLUMN_CHART, ChartType.COLUMN);
        map.put(ChartAsset.LEGEND_CHART, ChartType.LEGEND);
        map.put(ChartAsset.LINE_CHART, ChartType.LINE);
        map.put(ChartAsset.PIE_CHART, ChartType.PIE);
        CHART_TYPE = map;

        intMap = new IntObjectMap();
        intMap.put(ImageAsset.BMP, ImageEncoding.BMP);
        intMap.put(ImageAsset.GIF, ImageEncoding.GIF);
        intMap.put(ImageAsset.JPEG, ImageEncoding.JPEG);
        intMap.put(ImageAsset.PJPEG, ImageEncoding.PJPEG);
        intMap.put(ImageAsset.PNG, ImageEncoding.PNG);
        intMap.put(ImageAsset.TIFF, ImageEncoding.TIFF);
        intMap.put(ImageAsset.VIDEOTEX, ImageEncoding.VIDEOTEX);
        intMap.put(ImageAsset.WBMP, ImageEncoding.WBMP);
        IMAGE_ENCODING = intMap;

        intMap = new IntObjectMap();
        intMap.put(ImageAsset.COLOR, ImageRendering.COLOR);
        intMap.put(ImageAsset.MONOCHROME, ImageRendering.GRAYSCALE);
        IMAGE_RENDERING = intMap;

        map = new HashMap();
        map.put(ScriptAsset.JAVA_SCRIPT, ScriptEncoding.JAVASCRIPT);
        map.put(ScriptAsset.JAVA_SCRIPT_1_0, ScriptEncoding.JAVASCRIPT_1_0);
        map.put(ScriptAsset.JAVA_SCRIPT_1_1, ScriptEncoding.JAVASCRIPT_1_1);
        map.put(ScriptAsset.JAVA_SCRIPT_1_2, ScriptEncoding.JAVASCRIPT_1_2);
        map.put(ScriptAsset.JAVA_SCRIPT_1_3, ScriptEncoding.JAVASCRIPT_1_3);
        map.put(ScriptAsset.JAVA_SCRIPT_1_4, ScriptEncoding.JAVASCRIPT_1_4);
        map.put(ScriptAsset.WML_TASK, ScriptEncoding.WML_TASK);
        SCRIPT_PROGRAMMING_LANGUAGE = map;

        map = new HashMap();
        map.put(ScriptEncoding.JAVASCRIPT, ScriptAsset.JAVA_SCRIPT);
        map.put(ScriptEncoding.JAVASCRIPT_1_0, ScriptAsset.JAVA_SCRIPT_1_0);
        map.put(ScriptEncoding.JAVASCRIPT_1_1, ScriptAsset.JAVA_SCRIPT_1_1);
        map.put(ScriptEncoding.JAVASCRIPT_1_2, ScriptAsset.JAVA_SCRIPT_1_2);
        map.put(ScriptEncoding.JAVASCRIPT_1_3, ScriptAsset.JAVA_SCRIPT_1_3);
        map.put(ScriptEncoding.JAVASCRIPT_1_4, ScriptAsset.JAVA_SCRIPT_1_4);
        map.put(ScriptEncoding.WML_TASK, ScriptAsset.WML_TASK);
        SCRIPT_ENCODING_TO_LANGUAGE = map;

        intMap = new IntObjectMap();
        intMap.put(TextAsset.PLAIN, TextEncoding.PLAIN);
        intMap.put(TextAsset.FORM_VALIDATOR, TextEncoding.FORM_VALIDATOR);
        intMap.put(TextAsset.VOICEXML_ERROR, TextEncoding.VOICE_XML_ERROR);
        intMap.put(TextAsset.VOICEXML_HELP, TextEncoding.VOICE_XML_HELP);
        intMap.put(TextAsset.VOICEXML_PROMPT, TextEncoding.VOICE_XML_PROMPT);
        intMap.put(TextAsset.VOICEXML_NUANCE_GRAMMAR,
                TextEncoding.VOICE_XML_NUANCE_GRAMMAR);
        TEXT_ENCODING = intMap;

        intMap = new IntObjectMap();
        intMap.put(DynamicVisualAsset.ANIMATED_GIF,
                VideoEncoding.ANIMATED_GIF);
        intMap.put(DynamicVisualAsset.MACROMEDIA_FLASH,
                VideoEncoding.MACROMEDIA_FLASH);
        intMap.put(DynamicVisualAsset.MACROMEDIA_SHOCKWAVE,
                VideoEncoding.MACROMEDIA_SHOCKWAVE);
        intMap.put(DynamicVisualAsset.MPEG1, VideoEncoding.MPEG1);
        intMap.put(DynamicVisualAsset.MPEG4, VideoEncoding.MPEG4);
        intMap.put(DynamicVisualAsset.QUICKTIME_VIDEO,
                VideoEncoding.QUICKTIME);
        intMap.put(DynamicVisualAsset.REAL_VIDEO, VideoEncoding.REAL);
        intMap.put(DynamicVisualAsset.THREE_GPP, VideoEncoding.THREE_GPP);
        intMap.put(DynamicVisualAsset.WINDOWS_MEDIA_VIDEO,
                VideoEncoding.WINDOWS_MEDIA);
        VIDEO_ENCODING = intMap;
    }
}
