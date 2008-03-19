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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.theme.ThemeContentBuilder;
import com.volantis.mcs.policies.variants.layout.LayoutContentBuilder;
import com.volantis.mcs.policies.variants.metadata.InternalMetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaDataBuilder;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.selection.InternalSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilderVisitor;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.EncodingSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.utilities.StringUtils;

import java.util.List;
import java.util.Iterator;
import java.text.MessageFormat;

/**
 * Renderer class to return the string representation of a variant builder.
 */
public class VariantLabelRenderer {

    /**
     * Prefix for property resources.
     */
    private static final String RESOURCE_PREFIX = "VariantProxyLabelProvider.";

    /**
     * Renders a VariantBuilder as text.
     *
     * @param variant The object to render
     * @return The string value of that variant builder
     */
    public String render(final VariantBuilder variant) {
        String rendered = "";
        if (variant != null) {
            VariantBuilderRenderer renderer = new VariantBuilderRenderer();
            InternalSelectionBuilder selection =
                (InternalSelectionBuilder) variant.getSelectionBuilder();
            if (selection != null) {
                selection.accept(renderer);
            }
            InternalContentBuilder content =
                (InternalContentBuilder) variant.getContentBuilder();
            if (content != null) {
                content.accept(renderer);
            }
            InternalMetaDataBuilder metadata =
                (InternalMetaDataBuilder) variant.getMetaDataBuilder();
            if (metadata != null) {
                metadata.accept(renderer);
            }

            rendered = renderer.getRenderedVariant();
        }
        return rendered;
    }

    /**
     * Visitor for rendering the three components of a variant (content,
     * metadata and selection policy) in a text format.
     */
    private class VariantBuilderRenderer implements ContentBuilderVisitor,
            MetaDataBuilderVisitor, SelectionBuilderVisitor {
        /**
         * The string buffer into which the data is being rendered
         */
        private StringBuffer renderBuffer = new StringBuffer();

        /**
         * Retrieve the rendered data.
         *
         * @return The rendered data
         */
        public String getRenderedVariant() {
            return renderBuffer.toString();
        }

        // Javadoc inherited
        public void visit(AutoURLSequenceBuilder content) {
        }

        // Javadoc inherited
        public void visit(EmbeddedContentBuilder content) {
            appendText(EditorMessages.getString(
                RESOURCE_PREFIX + "embedded.label"));
        }

        // Javadoc inherited
        public void visit(LayoutContentBuilder content) {
            // No rendering of content for layouts
        }

        // Javadoc inherited
        public void visit(ThemeContentBuilder content) {
            // No rendering of content for themes
        }

        // Javadoc inherited
        public void visit(URLContentBuilder content) {
            appendText(content.getURL());
        }

        // Javadoc inherited
        public void visit(AudioMetaDataBuilder metaData) {
            renderEncoding(metaData.getAudioEncoding());
        }

        // Javadoc inherited
        public void visit(ChartMetaDataBuilder metaData) {
            ChartType chartType = metaData.getChartType();
            if (chartType != null) {
                String chartTypeName =
                    chartType.toString().replaceAll(" ", "");
                appendText(EditorMessages.getString(
                        "ChartType." +
                        StringUtils.toLowerIgnoreLocale(chartTypeName) +
                        ".label"));
            }
        }

        // Javadoc inherited
        public void visit(ImageMetaDataBuilder metaData) {
            renderEncoding(metaData.getImageEncoding());
            renderSize(metaData);
        }

        // Javadoc inherited
        public void visit(ScriptMetaDataBuilder metaData) {
            renderEncoding(metaData.getScriptEncoding());
            appendText(metaData.getCharacterSet());
        }

        // Javadoc inherited
        public void visit(TextMetaDataBuilder metaData) {
            renderEncoding(metaData.getTextEncoding());
        }

        // Javadoc inherited
        public void visit(VideoMetaDataBuilder metaData) {
            renderEncoding(metaData.getVideoEncoding());
            renderSize(metaData);
        }

        // Javadoc inherited
        public void visit(DefaultSelectionBuilder selection) {
            appendText(EditorMessages.getString(
                RESOURCE_PREFIX + "default.label"));
        }

        // Javadoc inherited
        public void visit(EncodingSelectionBuilder selection) {
            appendText(EditorMessages.getString(
                RESOURCE_PREFIX + "encoding.label"));
        }

        // Javadoc inherited
        public void visit(GenericImageSelectionBuilder selection) {
            appendText(EditorMessages.getString(
                RESOURCE_PREFIX + "generic.label"));
            appendText(selection.getWidthHint() + "%");
        }

        // Javadoc inherited
        public void visit(TargetedSelectionBuilder selection) {
            appendText(EditorMessages.getString(
                RESOURCE_PREFIX + "targeted.label"));

            // Append the categories
            List refs = selection.getModifiableCategoryReferences();
            if (refs != null && !refs.isEmpty()) {
                Iterator it = refs.iterator();
                while (it.hasNext()) {
                    CategoryReference categoryReference =
                            (CategoryReference) it.next();
                    appendText(categoryReference.getCategoryName());
                }
            }

            // Append the devices
            refs = selection.getModifiableDeviceReferences();
            if (refs != null && !refs.isEmpty()) {
                Iterator it = refs.iterator();
                while (it.hasNext()) {
                    DeviceReference deviceReference =
                            (DeviceReference) it.next();
                    appendText(deviceReference.getDeviceName());
                }
            }
        }

        /**
         * Render the encoding.
         *
         * @param encoding The encoding to be rendered
         */
        private void renderEncoding(Encoding encoding) {
            if (encoding != null) {
                String encodingName = encoding.getName().replaceAll(" ", "");
                String encodingDisplay = EditorMessages.getString("Encoding." +
                        encodingName + ".label");
                appendText(encodingDisplay);
            }
        }

        /**
         * Render the size for a give piece of metadata that has one.
         *
         * @param metaData The metadata for which the size should be rendered
         */
        private void renderSize(PixelDimensionsMetaDataBuilder metaData) {
            String sizeFormat = EditorMessages.getString(
                RESOURCE_PREFIX + "size.format");
            String size = MessageFormat.format(sizeFormat, new Object[] {
                new Integer(metaData.getWidth()),
                new Integer(metaData.getHeight()) });
            appendText(size);
        }

        /**
         * Append text to the render buffer, adding a separator if text already
         * exists.
         *
         * @param text The text to add
         */
        private void appendText(String text) {
            if (renderBuffer.length() > 0) {
                renderBuffer.append(", ");
            }
            renderBuffer.append(text);
        }
    }
}
