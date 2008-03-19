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
package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.EncodingSelectionBuilder;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.policies.variants.theme.ThemeContentBuilder;
import com.volantis.mcs.policies.variants.layout.LayoutContentBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * A typesafe enumeration describing the set of models building up a given
 * policy type (the content, metadata, fallbacks and allowable selection types).
 */
public class PolicyModelSet {
    /**
     * Map to allow quick retrieval of a model set from the corresponding policy
     * type.
     */
    private static Map policyTypesToModelSets = new HashMap();
    
    /**
     * Policy model set for audio assets.
     */
    public static final PolicyModelSet AUDIO = new PolicyModelSet(
            VariablePolicyType.AUDIO, AudioMetaDataBuilder.class,
            new Class[] { URLContentBuilder.class, null },
            new PolicyType[] { PolicyType.AUDIO, PolicyType.TEXT },
            new Class[] { TargetedSelectionBuilder.class, 
                          EncodingSelectionBuilder.class,
                          DefaultSelectionBuilder.class });

    /**
     * Policy model set for base URLs (asset groups).
     */
    public static final PolicyModelSet BASE_URL = new PolicyModelSet(
            PolicyType.BASE_URL, null, null, new PolicyType[] {}, null);

    /**
     * Policy model set for chart assets.
     */
    public static final PolicyModelSet CHART = new PolicyModelSet(
            VariablePolicyType.CHART, ChartMetaDataBuilder.class,
            null,
            new PolicyType[] { PolicyType.CHART, PolicyType.IMAGE, PolicyType.TEXT },
            new Class[] { DefaultSelectionBuilder.class });
    
    /**
     * Policy model set for image assets.
     */
    public static final PolicyModelSet IMAGE = new PolicyModelSet(
            VariablePolicyType.IMAGE, ImageMetaDataBuilder.class,
            new Class[] { URLContentBuilder.class, AutoURLSequenceBuilder.class, null },
            new PolicyType[] { PolicyType.IMAGE, PolicyType.TEXT },
            new Class[] { TargetedSelectionBuilder.class,
                          DefaultSelectionBuilder.class,
                          GenericImageSelectionBuilder.class });

    /**
     * Policy model set for layout assets.
     */
    public static final PolicyModelSet LAYOUT = new PolicyModelSet(
            VariablePolicyType.LAYOUT, null,
            new Class[] { LayoutContentBuilder.class },
            new PolicyType[] {},
            new Class[] { TargetedSelectionBuilder.class,
                          DefaultSelectionBuilder.class });

    /**
     * Policy model set for link assets.
     */
    public static final PolicyModelSet LINK = new PolicyModelSet(
            VariablePolicyType.LINK, null,
            new Class[] { URLContentBuilder.class, null },
            new PolicyType[] { PolicyType.LINK, PolicyType.TEXT },
            new Class[] { TargetedSelectionBuilder.class,
                          DefaultSelectionBuilder.class });

    /**
     * Policy model set for rollover image assets.
     */
    public static final PolicyModelSet ROLLOVER = new PolicyModelSet(
            PolicyType.ROLLOVER_IMAGE, null, null,
            new PolicyType[] { VariablePolicyType.TEXT }, null);

    /**
     * Policy model set for script assets.
     */
    public static final PolicyModelSet SCRIPT = new PolicyModelSet(
            VariablePolicyType.SCRIPT, ScriptMetaDataBuilder.class,
            new Class[] { URLContentBuilder.class, EmbeddedContentBuilder.class, null },
            new PolicyType[] { PolicyType.SCRIPT },
            new Class[] { TargetedSelectionBuilder.class,
                          DefaultSelectionBuilder.class });

    /**
     * Policy model set for text assets.
     */
    public static final PolicyModelSet TEXT = new PolicyModelSet(
            VariablePolicyType.TEXT, TextMetaDataBuilder.class,
            new Class[] { URLContentBuilder.class, EmbeddedContentBuilder.class, null },
            new PolicyType[] { PolicyType.TEXT },
            new Class[] { TargetedSelectionBuilder.class,
                          DefaultSelectionBuilder.class });

    /**
     * Policy model set for theme assets.
     */
    public static final PolicyModelSet THEME = new PolicyModelSet(
            VariablePolicyType.THEME, null,
            new Class[] { ThemeContentBuilder.class, null },
            new PolicyType[] {},
            new Class[] { TargetedSelectionBuilder.class,
                          DefaultSelectionBuilder.class });

    /**
     * Policy model set for dynamic visual assets.
     */
    public static final PolicyModelSet VIDEO = new PolicyModelSet(
            VariablePolicyType.VIDEO, VideoMetaDataBuilder.class,
            new Class[] { URLContentBuilder.class, null },
            new PolicyType[] { PolicyType.VIDEO, PolicyType.IMAGE, PolicyType.TEXT },
            new Class[] { TargetedSelectionBuilder.class,
                          EncodingSelectionBuilder.class,
                          DefaultSelectionBuilder.class });

    private PolicyType type;
    private Class metaDataClass;
    private Class[] contentClasses;
    private PolicyType[] fallBackTypes;
    private Class[] selectionClasses;

    private PolicyModelSet(PolicyType type, Class metaDataClass,
                           Class[] contentClasses, PolicyType[] fallBackTypes,
                           Class[] selectionClasses) {
        this.type = type;
        this.metaDataClass = metaDataClass;
        this.contentClasses = contentClasses;
        this.fallBackTypes = fallBackTypes;
        this.selectionClasses = selectionClasses;
        policyTypesToModelSets.put(type, this);
    }

    public static PolicyModelSet getModelSet(PolicyType type) {
        return (PolicyModelSet) policyTypesToModelSets.get(type);
    }

    public PolicyType getType() {
        return type;
    }

    public Class getMetaDataClass() {
        return metaDataClass;
    }

    public Class[] getContentClasses() {
        return contentClasses;
    }

    public PolicyType[] getFallBackTypes() {
        return fallBackTypes;
    }

    /**
     * Retrieves a list of classes corresponding to the types of selection that
     * are allowed.
     *
     * @return array of builder classes, may be null.
     */
    public Class[] getSelectionClasses() {
        return selectionClasses;
    }
}
