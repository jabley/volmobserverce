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
 * $Header: /src/voyager/com/volantis/mcs/papi/ChartElement.java,v 1.18 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Jan-02    Adrian          VBM:2001121003 - Created the Chart PAPI
 *                              Element.
 * 05-Mar-02    Adrian          VBM:2002021907 - If an error occurs writing the
 *                              chart image then attempt to write the altText
 *                              or altImage.  Also fixed problem whereby chart
 *                              was not written if an asset was not found.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 18-Mar-02    Payal           VBM:2001120405 - Modified elementEndImpl() to
 *                              display the alt text if the device does not
 *                              support GIF image format.
 * 20-Mar-02    Allan           VBM:2002031908 - Set the pane in pattributes
 *                              in elementEndImpl().
 * 20-Mar-02    Payal           VBM:2001120405 - Modified elementEndImpl() to
 *                              try the altImage and altText if encoding
 *                              equals GIF image  as GIF charts are not
 *                              supported regardless of the device.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 03-Apr-02    Adrian          VBM:2001102414 - use ComponentIdentities in
 *                              calls to retrieveAsset methods in
 *                              MarinerPageContext to retrieve fallbacks.
 * 28-Apr-02    Adrian          VBM:2002040808 - Temporatily removed theme
 *                              support in method updateDisplayDef
 * 16-May-02    Adrian          VBM:2002040808 - Reimplemented theme support in
 *                              in method updateDisplayDef.  Also added two new
 *                              utility methods getFontSize & convertToDegrees
 *                              to support the implementation.
 * 20-May-02    Paul            VBM:2001122105 - Initialised general events.
 * 25-Jul-02    Phil W-S        VBM:2002052705 - Allow WBMP images to be
 *                              generated for charts and incorporated the
 *                              pixel depth vs image type selection policy
 *                              being applied to images themselves.
 * 29-Oct-02    Chris W         VBM:2002111101 - Gets pane name from
 *                              FormatReferencerather than directly from
 *                              MarinerPageContext
 * 18-Nov-02    Geoff           VBM:2002111504 - Removed unused/unneeded code
 *                              and cleaned up some javadoc and imports.
 * 20-Feb-03    Byron           VBM:2003021809 - Modified elementEndImpl to
 *                              call the assetURLRewriter.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.davisor.core.NotFoundException;
import com.davisor.data.FloatType;
import com.davisor.data.InvalidDataException;
import com.davisor.data.TextType;
import com.davisor.data.stream.DataBuffer;
import com.davisor.data.stream.JoinedDataBuffer;
import com.davisor.data.stream.ListDataBuffer;
import com.davisor.graphics.RenderAttributes;
import com.davisor.graphics.chart.AxisRenderAttributes;
import com.davisor.graphics.chart.ChannelAttributes;
import com.davisor.graphics.chart.ChartAttributesFactory;
import com.davisor.graphics.chart.ChartAxes;
import com.davisor.graphics.chart.ChartAxis;
import com.davisor.graphics.chart.ChartData;
import com.davisor.graphics.chart.ChartException;
import com.davisor.graphics.chart.ChartFactory;
import com.davisor.graphics.chart.ChartObjectAttributes;
import com.davisor.graphics.chart.ImageChart;
import com.davisor.graphics.chart.PlotRenderAttributes;
import com.davisor.graphics.data.PaintType;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.ChartAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.properties.MCSChartAdornmentsKeywords;
import com.volantis.mcs.themes.properties.MCSChartLabelValuesKeywords;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.utilities.ChartValues;
import com.volantis.mcs.utilities.Convertors;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * The Chart element.
 */
public class ChartElementImpl
        extends BlockElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ChartElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(ChartElementImpl.class);

    /**
     * A legend chart has no data - only labels. However, data is still
     * required to construct the chart (a Woolox thing). DUMMY_DATA_SET
     * acts as data for legends.
     */
    private static final String DUMMY_DATA_SET = "0";

    /**
     * Valid font sizes.
     */
    private static final int[] FONT_SIZES = {6, 8, 10, 12, 14, 16, 18};

    /**
     * Percentage representation of valid font sizes.
     */
    private static final int[] PERCENTS = {50, 50, 75, 100, 125, 150, 200};

    /**
     * em representation of valid font sizes.
     */
    private static final double[] EM =
            {0.25, 0.50, 0.75, 1.00, 1.25, 1.50, 1.75};

    /**
     * The gradian factor constant value.
     */
    private static final double GRADIAN_FACTOR = 360.0 / 400.0;
    private static final Map FONT_SIZE_KEYWORD_2_INTEGER;

    static {
        Map map = new HashMap();
        map.put(FontSizeKeywords.XX_SMALL, new Integer(6));
        map.put(FontSizeKeywords.X_SMALL, new Integer(8));
        map.put(FontSizeKeywords.SMALL, new Integer(10));
        map.put(FontSizeKeywords.MEDIUM, new Integer(12));
        map.put(FontSizeKeywords.LARGE, new Integer(14));
        map.put(FontSizeKeywords.X_LARGE, new Integer(16));
        map.put(FontSizeKeywords.XX_LARGE, new Integer(18));

        FONT_SIZE_KEYWORD_2_INTEGER = map;
    }

    /**
     * Create a new <code>ChartElement</code>.
     */
    public ChartElementImpl() {
    }

    // Javadoc inherited from super class.
    protected int elementStartImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttribute)
            throws PAPIException {
        return SKIP_ELEMENT_BODY;
    }

    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes) throws PAPIException {

        try {

            boolean success = false;

            final MarinerPageContext pageContext
                    = ContextInternals.getMarinerPageContext(context);
            final ChartAttributes attributes =
                    (ChartAttributes) blockAttributes;

            // Create a chart policy reference from the chart name.
            PolicyReferenceResolver resolver =
                    pageContext.getPolicyReferenceResolver();

            // Resolve the expression to a reference.
            RuntimePolicyReference reference =
                    resolver.resolveUnquotedPolicyExpression(
                            attributes.getName(), PolicyType.CHART);

            AssetResolver assetResolver = pageContext.getAssetResolver();

            SelectedVariant selected = assetResolver.selectBestVariant(
                    reference, null);
            if (selected != null) {

                // Try and retrieve the chart asset.
                ChartAsset chartAsset = (ChartAsset) selected.getOldObject();

                // get styles for the current element.
                Styles styles = pageContext.getStylingEngine().getStyles();

                ActivatedVariablePolicy policy = selected.getPolicy();

                // If a chart asset was found
                String name = policy.getName();
                if (chartAsset != null) {

                    // Set up the chart display definition.
                    ChartDefinition chartDefinition = new ChartDefinition();
                    updateChartDefinition(chartDefinition, chartAsset);

                    // Create the attributes for the protocol image.
                    ImageAttributes pattributes = new ImageAttributes();

                    // Set the styles for the current element on MCSAttributes
                    pattributes.setStyles(styles);

                    updateChartDefinition(chartDefinition, styles);

                    // Create the chart file and return the URL to it.
                    // TODO: is this supposed to cache generated charts too?
                    String imageSrc =
                            createChartFile(pageContext, chartDefinition,
                                    name, attributes.getData(),
                                    attributes.getLabels());
                    // If we managed to write the chart and create a URL to it
                    if (imageSrc != null) {
                        // Then call the protocol to write an image tag to
                        // the URL we created.

                        // Find the pane and add it to the attributes.
                        if (attributes.getPane() != null) {
                            final FormatReference formatReference =
                                    FormatReferenceParser.parsePane(
                                            attributes.getPane(), pageContext);
                            pattributes.setPane(pageContext.getPane(
                                    formatReference.getStem()));
                        }

                        // Rewrite the URL if necessary.
                        final AssetURLRewriter assetURLRewriter =
                                ContextInternals.getApplicationContext(context).
                                        getAssetURLRewriter();
                        MarinerURL url =
                                assetURLRewriter.rewriteAssetURL(context,
                                        chartAsset, null,
                                        new MarinerURL(imageSrc));

                        // Update the rest of the attributes.
                        pattributes.setSrc(url.getExternalForm());
                        pattributes.setId(attributes.getId());
                        pattributes.setTitle(attributes.getTitle());
                        pattributes.setAltText(attributes.getAltText());

                        // Initialise the general event attributes.
                        PAPIInternals
                                .initialiseGeneralEventAttributes(pageContext,
                                        attributes, pattributes);

                        // Write the image to the protocol's buffer.
                        final VolantisProtocol protocol =
                                pageContext.getProtocol();
                        protocol.writeImage(pattributes);

                        success = true;
                    } else {
                        // Else, the chart creation or referencing process failed.
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                    "Unable to create/reference chart for " +
                                            name + ", trying fallback");
                        }
                    }
                } else {
                    // Else no chart asset was found.
                    if (logger.isDebugEnabled()) {
                        logger.debug("No chart asset found for " +
                                name + ", trying fallback");
                    }
                }

                // If we were unable to write the chart for any reason...
                if (!success) {
                    // Then try and write the fallback.

                    if (MediaUtilities.tryAltImg(attributes, pageContext,
                            policy, attributes.getAltText())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("AltImage output for chart asset " +
                                    name + " on device " +
                                    pageContext.getDeviceName());
                        }
                    } else
                    if (MediaUtilities.tryAltText(attributes, pageContext,
                            policy, attributes.getAltText(), styles)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("AltText output for chart asset " +
                                    name + " on device " +
                                    pageContext.getDeviceName());
                        }
                    } else {
                        // MCSPA0010W="Unsuccessful Chart Element call for chart
                        logger.warn("chart-element-failure", new Object[]{
                                name, pageContext.getDeviceName()});
                    }
                }
            }
        } catch (Exception e) {
            logger.error("chart-element-error", e);
            throw new PAPIException(
                    exceptionLocalizer.format("chart-element-error"), e);
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Volantis ChartElement EndImpl ends");
            }
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }

    /**
     * Create a chart file for the chart definition supplied, returning the URL
     * required to reference the chart file created.
     *
     * @param pageContext     the page context.
     * @param chartDefinition the definition of the chart.
     * @param chartName       the name of the chart.
     * @return the URL to the chart file created.
     * @throws PAPIException
     */
    private String createChartFile(
            MarinerPageContext pageContext,
            ChartDefinition chartDefinition, String chartName,
            String data, String labels) throws PAPIException {

        String chartUrl = null;

        // We must have data for a non-legend type chart.
        final String chartType = chartDefinition.getType();
        if ((data != null) || "legend".equals(chartType)) {
            // get device details
            InternalDevice device = pageContext.getDevice();
            int encoding = getPreferredEncoding(device);

            // We only support NON-GIF charts with valid encodings.
            if ((encoding != 0) && (encoding != ImageAsset.GIF)) {

                // Create the chart attributes using the encoding, chart
                // definition and device.
                ChartObjectAttributes chartAttrs = createChartObjectAttributes(
                        chartType, encoding, chartDefinition, device);

                // Create the chart object from the input data.
                ImageChart chart = createImageChart(data, labels,
                        chartDefinition, chartAttrs, chartType);

                // Create an appropriate simple filename for the image.
                //
                // This needs to be unique enough that different requests
                // rendering of the same chart asset into a file on disk do
                // not interfere with each other.
                //
                // The chart image may vary depending on the device because
                // the device may control the size of the chart (naturally).
                // Thus the filename must contain something that represents
                // the device. Currently we use the device name.
                //
                // This file is of the form <device><chart name>.<extension>
                // E.g. [PC-UNIX][/chart.mcht].[jpeg]
                //
                // TODO: CHART IMAGE NAMING IS NOT REQUEST SAFE.
                // Given that the chart data is supplied (potentially
                // dynamically) from the XDIME, we must also include something
                // that represents that dataset in the file name in order for
                // our file uniqueness scheme to work.
                //
                // TODO: CHART IMAGE NAMING IS NOT THREAD SAFE.
                // Also, there is no syncronisation for multiple simultaneous
                // requests of identical charts.
                //
                String imageFilename = device.getName() +
                        chartName + "." +
                        ImageAsset.fileExtension(encoding);

                // Write the chart object we created to a file, using the
                // simple filename we just created, and return a URL that
                // may be used to reference the file created.
                // todo this should not really be the current project but it will do for now.
                chartUrl = writeImageChart(pageContext, chart,
                        pageContext.getCurrentProject(), imageFilename);

            } else {
                // No encoding can be found OR the encoding is the GIF
                // image format so we try the altImage and altText
                // (the GIF test is required because GIF charts are not
                // supported regardless of the device).
                // todo: should this have an associated warning?
            }
        } else {
            logger.warn("chart-data-required");
        }

        return chartUrl;
    }

    /**
     * Return the preferred image encoding applicable to the given device,
     * taking into account pixel depth vs image type policies.
     *
     * @param device the device for which the encoding is to be found
     * @return the encoding identifier, or zero if no encoding is available
     */
    private int getPreferredEncoding(InternalDevice device) {
        int result = 0;
        int[] encodings = null;

        // Select the correct search order depending on the device pixel
        // depth. The ordering is chosen to correspond to the ordering
        // defined for Images (see GenericImageAssetSelectionPolicy.
        // selectBestGenericImageAsset)
        if (device.getPixelDepth() > 8) {
            int[] deep = {ImageAsset.JPEG,
                    ImageAsset.PNG,
                    ImageAsset.GIF,
                    ImageAsset.BMP,
                    ImageAsset.WBMP};
            encodings = deep;
        } else {
            int[] shallow = {ImageAsset.PNG,
                    ImageAsset.GIF,
                    ImageAsset.JPEG,
                    ImageAsset.BMP,
                    ImageAsset.WBMP};
            encodings = shallow;
        }

        for (int i = 0; (result == 0) && (i < encodings.length); i++) {
            if (device.supportsImageEncoding(encodings[i])) {
                result = encodings[i];
            }
        }

        return result;
    }

    /**
     * Create the chart object from the input data and chart definition
     * supplied.
     *
     * @param data            the string containing the data .
     * @param chartDefinition the chart definition
     * @param chartAttrs
     * @param chartType       the chart type, e.g. 'bar', 'column', etc.
     * @return the image chart object created
     */
    private ImageChart createImageChart(
            String data, String labels,
            ChartDefinition chartDefinition, ChartObjectAttributes chartAttrs,
            String chartType) throws PAPIException {

        try {
            ChartValues legendVals = null;

            if ("legend".equals(chartType)) {
                // The labels are for the legend as opposed to an axis on
                // a chart.
                legendVals = new ChartValues(labels);
            }
            ChartValues chartVals = createChartValues(data, legendVals);

            // Create the buffers that need to be populate with data.
            DataBuffer[] buffers = createDataBuffer(
                    chartDefinition.hasAdornments(), chartType,
                    chartVals.size());

            // Populate the buffers array with appropriate values.
            initializeBuffers(buffers, chartType, chartVals, chartDefinition,
                    labels);

            // In old tag an unitialised String was passed into this
            // constructor, so the same is done here.  Is this a bug?
            // AJB 2002-01-18
            JoinedDataBuffer buffer = new JoinedDataBuffer(null, buffers);
            ChartData chartData = new ChartData(buffer);

            // MCS currently only provides single ChartAxes charts so
            // there should never be more than one ChartAxes in the
            // returned List. Therefore set to the first item in the
            // List returned from ChartAttribute.getAxes().
            ChartAxes chartAxes = (ChartAxes) chartAttrs.getAxes().get(0);

            // Initialize the attributes.
            initializeAttributes(chartDefinition, chartAxes, chartAttrs,
                    buffers, legendVals, chartData);

            if (logger.isDebugEnabled()) {
                logger.debug("Chart definition: " + chartDefinition.toString());
            }

            ImageChart chart = (ImageChart) ChartFactory.getFactory(chartType).
                    createChart(chartData, chartAttrs);

            return chart;
        } catch (InvalidDataException e) {
            throw new PAPIException(
                    exceptionLocalizer.format("chart-creation-error"), e);
        } catch (NotFoundException e) {
            throw new PAPIException(
                    exceptionLocalizer.format("chart-creation-error"), e);
        } catch (ChartException e) {
            throw new PAPIException(
                    exceptionLocalizer.format("chart-creation-error"), e);
        }
    }

    /**
     * Initialize the renderers of the axes (fonts, colours, etc.)
     *
     * @param chartDefinition the chart definition
     * @param chartAxes       the chart axes instance.
     * @param chartAttributes the chart attributes.
     * @param buffers         the array of data buffers
     * @param legendVals      the chart values for the legend.
     * @param chartData       the chart data instance.
     */
    private void initializeAttributes(
            ChartDefinition chartDefinition,
            ChartAxes chartAxes,
            com.davisor.graphics.chart.ChartAttributes chartAttributes,
            DataBuffer[] buffers,
            ChartValues legendVals,
            ChartData chartData) {

        PlotRenderAttributes plotRenderAttributes = chartAttributes.getPlot();
        plotRenderAttributes.addChannelAttributes(chartData);
        plotRenderAttributes.setValueFormat("{.value,float,#}");
        plotRenderAttributes.setFont(new Font(chartDefinition.getFontName(),
                Font.PLAIN, chartDefinition.getFontSize()));

        // We need to set the names for the legend values.
        for (int index = 0; index < buffers.length; index++) {
            if ((legendVals != null) && (index < legendVals.size())) {
                // set the legend.
                ChannelAttributes chanAttrs = (ChannelAttributes)
                        plotRenderAttributes.getChannelAttributes()
                                .get("series" + index);
                if (chanAttrs != null) {
                    chanAttrs.setName(
                            (String) legendVals.getValues().elementAt(index));
                }
            }
        }

        // Set title, angle, color, font and text for the 1st axis.
        ChartAxis axis = chartAxes.getAxis(0);
        axis.setTitleText(chartDefinition.getXTitle());
        axis.setAngle(chartDefinition.getXAngle());
        if (chartDefinition.getXInterval() != 0) {
            axis.setGap(chartDefinition.getXInterval());
        }

        RenderAttributes renderAttributes = axis.getRender();
        renderAttributes.setColor(chartDefinition.getGridColor());
        renderAttributes.setFont(new Font(chartDefinition.getFontName(),
                Font.PLAIN, chartDefinition.getFontSize()));

        // Set title, angle, color, font and text for the 2nd axis.
        axis = chartAxes.getAxis(1);
        axis.setTitleText(chartDefinition.getYTitle());
        axis.setAngle(chartDefinition.getYAngle());
        if (chartDefinition.getYInterval() != 0) {
            axis.setGap(chartDefinition.getYInterval());
        }
        renderAttributes = axis.getRender();
        renderAttributes.setColor(chartDefinition.getGridColor());
        renderAttributes.setFont(new Font(chartDefinition.getFontName(),
                Font.PLAIN, chartDefinition.getFontSize()));

        RenderAttributes chartRender = chartAttributes.getBack();
        chartRender.setColor(Color.black);
        chartRender.setAlignment(RenderAttributes.AUTO);

        AxisRenderAttributes axisRenderAttributes = chartAxes.getRender();
        axisRenderAttributes.setPaint(chartDefinition.getBgColor());
        axisRenderAttributes.setColor(chartDefinition.getPenColor());

        // This needs to be set so that the value labels appear.
        PlotRenderAttributes axesPlotRenderAttributes = chartAxes.getPlot();
        axesPlotRenderAttributes.setDefaults(plotRenderAttributes);
    }

    /**
     * Initialize the buffers with data (series1, series2, etc., labels and
     * colours).
     *
     * @param buffers         the array of buffers DataBuffer objects.
     * @param chartType       the chart type ('pie', 'column', 'bar', etc.).
     * @param chartVals       the chart values.
     * @param chartDefinition the chart definition.
     * @param labels          the chart labels
     * @throws InvalidDataException if the data is invalid.
     */
    private void initializeBuffers(
            DataBuffer[] buffers,
            String chartType,
            ChartValues chartVals,
            ChartDefinition chartDefinition,
            String labels)
            throws InvalidDataException {

        // Get the data sets from chartVals and put them into buffers. Store
        // the series with an id of the form 'seriesX' where X is an integer
        // starting at 0.
        int bufNo = 0;
        for (; bufNo < chartVals.size(); bufNo++) {
            String vals = (String) chartVals.getValues().elementAt(bufNo);
            buffers[bufNo] = new ListDataBuffer(new FloatType("value",
                    "series" + bufNo, null), vals, ' ');
        }

        // Update the chart's label data array.
        if (chartDefinition.hasAdornments()) {
            TextType textVals = new TextType("label");
            if (labels != null) {
                buffers[bufNo] = new ListDataBuffer(textVals, labels,
                        ' ');
                bufNo++;
            }
        }

        if (requiresBufferForColours(chartType, chartVals.size())) {
            // According to Woolox it is possible to set the
            // colors using a Vector of Colors. However, I have
            // not been able to get this to work due to
            // ClassCastExceptions against Color later on
            // when the Chart is initializing. When Woolox publish
            // some better API docs maybe the way will be clear
            // but until them I am using a string of #rrggbb values.
            // AB 2001-02-04.
            // todo later not sure if the comment above is still valid.
            StringBuffer colors = new StringBuffer();
            for (int i = 0; i < chartDefinition.getFgColors().length; i++) {
                Color color = chartDefinition.getFgColors()[i];
                colors.append(Convertors.colorToCSSColor(color)).
                        append(' ');
            }
            buffers[bufNo] = new ListDataBuffer(new PaintType("paint"),
                    colors.toString(), ' ');
        }
    }

    /**
     * If there is more than one data set, then foreground colors will be used
     * for each set. However, if there is only one data set, or the chart is a
     * pie chart then the foreground colors apply to each data value. When this
     * is the case we need an additional data channel for the colors.
     *
     * @param chartType the chart type.
     * @param size      the size of the buffers
     * @return true if a buffer is required for colours (Pie charts slice
     *         differentiation).
     */
    private boolean requiresBufferForColours(String chartType, int size) {
        return (size == 1) && "pie".equals(chartType);
    }

    /**
     * Create the data buffer with the correct dimensions.
     *
     * @param hasAdornments true if we have adornments (labels), false otherwise.
     * @param chartType     the chart type.
     * @param size          the minimum size of the buffer.
     * @return the newly created array of {@link DataBuffer} objects.
     */
    private DataBuffer[] createDataBuffer(
            boolean hasAdornments,
            String chartType,
            int size) {
        // set up the buffer's size.
        int channelSize = hasAdornments ? size + 1 : size;

        if (requiresBufferForColours(chartType, size)) {
            ++channelSize;
        }
        return new DataBuffer[channelSize];
    }

    /**
     * Create an instance of the {@link ChartValues} object with appropriate
     * data.
     *
     * @param data       the data list as a string.
     * @param legendVals the current legend chart values instance.
     * @return the newly created ChartValues instance.
     */
    private ChartValues createChartValues(String data, ChartValues legendVals) {
        ChartValues chartVals = null;
        if (data == null) {
            // This chart is a legend chart and has no data. However, we need
            // data in order to produce the legends - so we create some dummy
            // data.
            Vector dataVals = new Vector(legendVals.size());
            for (int i = 0; i < legendVals.size(); i++) {
                dataVals.add(DUMMY_DATA_SET);
            }
            chartVals = new ChartValues(dataVals);
        } else {
            chartVals = new ChartValues(data);
        }
        return chartVals;
    }

    /**
     * Create the chart attributes with and set the content type, width and
     * height.
     *
     * @param chartType       the chart type, e.g. 'bar', 'column', etc.
     * @param encoding        the encoding used to determine the content type.
     * @param chartDefinition the chart definition.
     * @param device          the device.
     * @return the newly created and partially populated <code>ChartAttributes</code>.
     */
    private ChartObjectAttributes createChartObjectAttributes(
            String chartType,
            int encoding,
            ChartDefinition chartDefinition,
            InternalDevice device) {

        final ChartAttributesFactory instance =
                ChartAttributesFactory.getInstance();
        final ChartObjectAttributes chartAttributes =
                instance.createChartObjectAttributes();

        chartAttributes.setContentType(ImageAsset.mimeType(encoding));
        chartAttributes.setChartType(chartType);

        if (logger.isDebugEnabled()) {
            logger.debug("ChartElement: encoding is " + encoding);
        }
        // set the width.
        chartAttributes.setWidth(getDimension(device.getPolicyValue("pixelsx"),
                chartDefinition.getWidthHint()), false);

        // set the height.
        chartAttributes.setHeight(getDimension(device.getPolicyValue("pixelsy"),
                chartDefinition.getHeightHint()), false);

        return chartAttributes;
    }

    /**
     * Get the pixels dimension of a chart given a string size and its hint.
     *
     * @param size     the size represented as a string.
     * @param sizeHint size hint.
     * @return a Number representation of the size (zero if the size element
     *         couldn't be parsed).
     */
    private Number getDimension(String size, int sizeHint) {
        int result = 0;
        if (size != null && sizeHint > 0) {
            try {
                result = (int) (Float.parseFloat(size) *
                        ((float) sizeHint / (float) 100.0));
            } catch (NumberFormatException e) {
                logger.error("unexpected-ioexception", e);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("ChartElement: chart size is " + result);
        }
        return new Integer(result);
    }

    /**
     * Override display definition attributes with theme attributes. This is a
     * convenience method to split up the code. If there is a theme for this
     * chart then update the display definition with the theme applicable
     * attributes.
     *
     * @param chartDefinition the chart definition.
     * @param styles          the styles.
     */
    private void updateChartDefinition(
            ChartDefinition chartDefinition,
            Styles styles) {

//        ValueHandler colorValueHandler = new ColorHandler(
//                        ColorNameKeywordMapper.getSingleton());
//        PropertyHandler colorHandler = new ValueHandlerToPropertyAdapter(
//                        StylePropertyDetails.COLOR, colorValueHandler);

//        ChoicePropertyHandler choicePropertyHandler = new ChoicePropertyHandler();
//        choicePropertyHandler.addHandlers(
//                PropertyGroups.BORDER_COLOR_PROPERTIES, colorValueHandler);
//        PropertyHandler borderColorHandler = choicePropertyHandler;

        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue;

        styleValue =
                propertyValues.getComputedValue(StylePropertyDetails.COLOR);
        if (styleValue instanceof StyleColor) {
            Color color = cSSColorToColor((StyleColor) styleValue);
            chartDefinition.setBgColor(color);
        }

        styleValue = propertyValues
                .getComputedValue(StylePropertyDetails.MCS_CHART_GRID_COLOR);
        if (styleValue instanceof StyleColor) {
            Color color = cSSColorToColor((StyleColor) styleValue);
            chartDefinition.setGridColor(color);
        }
        styleValue = propertyValues
                .getComputedValue(StylePropertyDetails.BORDER_TOP_COLOR);
        if (styleValue instanceof StyleColor) {
            Color color = cSSColorToColor((StyleColor) styleValue);
            chartDefinition.setBorderColor(color);
        }

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CHART_FOREGROUND_COLORS);
        if (styleValue instanceof StyleList) {
            StyleList styleList = (StyleList) styleValue;
            List list = styleList.getList();
            Vector fgCols = new Vector();
            for (int i = 0; i < list.size(); i++) {
                StyleValue itemValue = (StyleValue) list.get(i);
                Color color = cSSColorToColor((StyleColor) itemValue);
                if (color != null) {
                    fgCols.add(color);
                }
            }
            Color[] colArray = new Color[fgCols.size()];
            fgCols.copyInto(colArray);
            chartDefinition.setFgColors(colArray);
        }

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CHART_ADORNMENTS);
        if (styleValue == MCSChartAdornmentsKeywords.ALL) {
            chartDefinition.setAdornments(true);
        } else {
            chartDefinition.setAdornments(false);
        }

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CHART_LABEL_VALUES);
        if (styleValue == MCSChartLabelValuesKeywords.YES) {
            chartDefinition.setLabelValues(false);
        } else {
            chartDefinition.setLabelValues(true);
        }

        String fontName = getFontFamily(propertyValues);
        if (fontName != null) {
            chartDefinition.setFontName(fontName);
        }

        int size = getFontSize(propertyValues
                .getComputedValue(StylePropertyDetails.FONT_SIZE));
        if (size != -1) {
            chartDefinition.setFontSize(size);
        }

        // HeightHint and WidthHint - neither of these belong
        // in the Theme but they are device dependent and
        // at the moment there is no other place for them to
        // go. This will change in a future release. AB 2001-01-24.
        StylePercentage stylePercentage;

        stylePercentage = (StylePercentage) propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CHART_HEIGHT);
        chartDefinition.setHeightHint((int) stylePercentage.getPercentage());

        stylePercentage = (StylePercentage) propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CHART_WIDTH);
        chartDefinition.setWidthHint((int) stylePercentage.getPercentage());

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CHART_X_AXIS_ANGLE);
        chartDefinition.setXAngle(convertToDegrees(styleValue));

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CHART_Y_AXIS_ANGLE);
        chartDefinition.setYAngle(convertToDegrees(styleValue));
    }

    /**
     * Convert a theme angle to a value in degrees.
     *
     * @param value the angle in rad, grad or degrees.
     * @return the angle in degrees as a float.
     */
    private float convertToDegrees(StyleValue value) {
        float degrees = 0;
        if (value instanceof StyleAngle) {
            StyleAngle angle = (StyleAngle) value;
            AngleUnit unit = angle.getUnit();
            if (unit == AngleUnit.DEG) {
                degrees = (float) angle.getNumber();
            } else if (unit == AngleUnit.GRAD) {
                degrees = (float) (GRADIAN_FACTOR * angle.getNumber());
            } else if (unit == AngleUnit.RAD) {
                degrees = (float) Math.toDegrees(angle.getNumber());
            }
        }
        return degrees;
    }

    /**
     * Process a font size retreived from a theme.
     *
     * @param value
     * @return int representation of the FontSize style value, -1 if the font
     *         size cannot be computed.
     */
    private int getFontSize(StyleValue value) {
        int result = -1;
        if (value instanceof StyleLength) {
            StyleLength length = (StyleLength) value;
            LengthUnit unit = length.getUnit();
            if (unit == LengthUnit.PX) {
                result = (int) length.getNumber();
            } else if (unit == LengthUnit.EM) {
                double fem = length.getNumber();
                for (int i = EM.length - 1; (result == -1) && (i >= 0); i--) {
                    if (fem >= EM[i]) {
                        result = FONT_SIZES[i];
                    }
                }
            }
        } else if (value instanceof StylePercentage) {
            StylePercentage percentage = (StylePercentage) value;
            double fpc = percentage.getPercentage();
            for (int i = PERCENTS.length - 1; (result == -1) && (i >= 0); i--) {
                if (fpc >= PERCENTS[i]) {
                    result = FONT_SIZES[i];
                }
            }
        } else {
            Integer size = (Integer) FONT_SIZE_KEYWORD_2_INTEGER.get(value);
            if (size != null) {
                result = size.intValue();
            }
        }

        if (result == -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unknown font type: " + value);
            }
        }
        return result;
    }

    private String getFontFamily(PropertyValues propertyValues) {
        StyleValue value = propertyValues
                .getComputedValue(StylePropertyDetails.FONT_FAMILY);
        if (value != null) {
            StyleList list = (StyleList) value;
            return ((StyleValue) list.getList().get(0)).getStandardCSS();
        } else {
            return null;
        }
    }

    /**
     * Override display definition attributes with asset attributes. This is a
     * convenience method to split up the code. If there a ChartAsset for this
     * chart then update the display definition with the asset applicable
     * values.
     *
     * @param chartDefinition the chart display definition.
     * @param chartAsset      the ChartAsset associated with this chart
     */
    private void updateChartDefinition(
            ChartDefinition chartDefinition,
            ChartAsset chartAsset) {
        if (chartAsset != null) {
            if (chartAsset.getXInterval() != 0) {
                chartDefinition.setXInterval(chartAsset.getXInterval());
            }
            if (chartAsset.getYInterval() != 0) {
                chartDefinition.setYInterval(chartAsset.getYInterval());
            }
            if (chartAsset.getXTitle() != null) {
                chartDefinition.setXTitle(chartAsset.getXTitle());
            }
            if (chartAsset.getYTitle() != null) {
                chartDefinition.setYTitle(chartAsset.getYTitle());
            }
            if (chartAsset.getType() != null) {
                chartDefinition.setType(chartAsset.getType());
            }
            if (chartAsset.getHeightHint() != 0) {
                chartDefinition.setHeightHint(chartAsset.getHeightHint());
            }
            if (chartAsset.getWidthHint() != 0) {
                chartDefinition.setWidthHint(chartAsset.getWidthHint());
            }
        }
    }

    /**
     * Write the chart to a file and return a URL to the chart file created.
     *
     * @param pageContext   The <code>MarinerPageContext</code> in which this
     *                      element is being processed.
     * @param chart         the image chart
     * @param project       the project to which the chart belongs.
     * @param imageFileName the name of the image file.
     * @return the URL for the image src, or null if we could not create or
     *         reference the chart for any reason.
     */
    private String writeImageChart(
            MarinerPageContext pageContext,
            ImageChart chart, Project project,
            String imageFileName) {

        String imageSrc = null;

        // Calculate the directory corresponding to our webapp context.
        EnvironmentContext environment = pageContext.getEnvironmentContext();
        String contextPath = environment.getRealPath("/");

        // If we have a non-null context path...
        if (contextPath != null) {
            // Then we are running inside a normal (expanded) webapp context.

            // Extract the generated resource base from the id's runtimeProject.
            RuntimeProject runtimeProject = (RuntimeProject) project;
            String generatedResourceBase =
                    runtimeProject.getGeneratedResourcesBaseDir();
            // Extract the chart images base.
            final String chartImagesBase = pageContext.getChartImagesBase();
            // If a chart images base was supplied...
            if (chartImagesBase != null && chartImagesBase.length() > 0) {
                // Then we should have a stab at writing the image.

                // Calculate the full file name from the main base dir, the
                // intermediate base directories and the file name.
                String imagePath = createImageFilePath(contextPath,
                        generatedResourceBase, chartImagesBase, imageFileName);
                if (logger.isDebugEnabled()) {
                    logger.debug("Chart image path is " + imagePath);
                }

                // We need to determine whether the director(ies) exist where we
                // we intend to write the image file. If not, we create them. This
                // accommodates any directory separators that may occur in the chart
                // name.
                File file = new File(imagePath);
                final File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                    // NOTE: no error detected here, but it will be reported
                    // just below anyway.
                }

                // Once we have a valid file path, we can write the image to
                // that path.
                try {
                    chart.putImage(imagePath);

                    // Then try and calculate the related URL which we will
                    // reference that file.
                    imageSrc = createImageURL(
                            pageContext.getAbsolutePageBaseURL(),
                            generatedResourceBase, chartImagesBase,
                            imageFileName);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Chart image src is " + imageSrc);
                    }

                } catch (Exception e) {
                    logger.warn("chart-file-error", new Object[]{imagePath}, e);
                }
            } else {
                // Else, no chart images base was supplied.

                // We cannot proceed here. We cannot really supply a default
                // as it may trash users content, and we cannot work without
                // one as it may trash other users of the resource base.

                // MCSPA0012E=chartimages base directory not set in config file.
                logger.error("missing-chartimages-base-dir");
            }
        } else {
            // Then we are operating inside an unexpanded WAR file.

            // Charts used to support this but the implementation was dodgy
            // and has been removed. Architecture have stated that Charts no
            // longer need to support running in WAR files.

            // MCSPA0013E=Webapp running in unexpanded WAR file. Cannot write
            logger.error("chart-disabled-in-unexpanded-war");
        }

        return imageSrc;
    }

    /**
     * Create an absolute URL to the chart image file using the URL components
     * supplied.
     *
     * @param baseURL               the absolute base URL of the webapp.
     * @param generatedResourceBase the optional relative URL of the generated
     *                              resource files within the base URL. Note that this may be null if
     *                              this was not specified by the user.
     * @param chartImagesBase       the relative URL of the chart images within the
     *                              generated resource URL, or the base URL if the generated resource
     *                              URL was null.
     * @param imageFileName         the simple name of the image file.
     * @return an absolute URL to the chart image.
     */
    private String createImageURL(
            String baseURL,
            String generatedResourceBase, final String chartImagesBase,
            String imageFileName) {

        // NOTE: this is designed for correctness rather than minimising GC.
        // Given the number of bugs we have had in this code I believe this
        // is the correct approach.
        // NOTE: This must match createImageFilePath below!

        MarinerURL url = new MarinerURL(baseURL);
        ensureDirectory(url);
        if (generatedResourceBase != null) {
            url = new MarinerURL(url, generatedResourceBase);
            ensureDirectory(url);
        }
        url = new MarinerURL(url, chartImagesBase);
        ensureDirectory(url);
        url = new MarinerURL(url, imageFileName);
        return url.getExternalForm();
    }

    /**
     * Simple helper method to ensure that a MarinerURL is in the form of a
     * directory. This is done by adding a trailing / if one is not already
     * present.
     *
     * @param url A url to force into the form of a directory.
     */
    private void ensureDirectory(MarinerURL url) {
        if (!url.getPath().endsWith("/")) {
            url.setPath(url.getPath() + "/");
        }
    }

    /**
     * Create an absolute file path to the chart image file using the path
     * components specified.
     * <p>
     * The path is constructed according to the following formula:
     * <pre>
     * chart-image-file = base-dir + generated-resource-base +
     *                    chart-images-base + image-file-name.
     * </pre>
     * Note that the path components may contain directory separators either in
     * default Java format (/) or platform specific format (eg \ on Windows).
     *
     * @param baseDir               the absolute path of the base directory of the webapp.
     * @param generatedResourceBase the optional relative path of the generated
     *                              resource base directory within the base directory. This may be null
     *                              if the user has not specified a value for this.
     * @param chartImagesBase       the relative path of the chart images within the
     *                              generated resource directory, or the base directory if the
     *                              resource base directory is null.
     * @param imageFileName         the simple name of the image file.
     * @return an absolute file path to the chart image.
     */
    private String createImageFilePath(
            String baseDir,
            String generatedResourceBase, final String chartImagesBase,
            String imageFileName) {

        // NOTE: This is designed for correctness rather than minimising GC.
        // Given the number of bugs we have had in this code I believe this
        // is the correct approach.
        // NOTE: This must match createImageURL above!

        File file = new File(baseDir);
        if (generatedResourceBase != null) {
            file = new File(file, generatedResourceBase);
        }
        file = new File(file, chartImagesBase);
        file = new File(file, imageFileName);

        return file.getPath();
    }

    /**
     * Takes a string color representation of the format #rrggbb
     * or one of the CSS named colors and returns the equivalent java Color
     * object.
     *
     * @param color The style color.
     * @return Color represented by String param
     */
    public static Color cSSColorToColor(StyleColor color)
            throws NumberFormatException {

        if (color == StyleColorNames.BLACK) {
            return Color.black;
        }
        if (color == StyleColorNames.WHITE) {
            return Color.white;
        }
        if (color == StyleColorNames.RED) {
            return Color.red;
        }
        if (color == StyleColorNames.GREEN) {
            return Color.green;
        }
        if (color == StyleColorNames.BLUE) {
            return Color.blue;
        }
        if (color == StyleColorNames.GRAY) {
            return Color.gray;
        }
        if (color == StyleColorNames.SILVER) {
            return Color.lightGray;
        }
//        if (color == StyleColorNames.ORANGE) {
//            return Color.orange;
//        }
        if (color == StyleColorNames.YELLOW) {
            return Color.yellow;
        }
//        if (cSSColor.equalsIgnoreCase("indigo")) {
//            return new Color(51, 0, 255);
//        }
//        if (cSSColor.equalsIgnoreCase("violet")) {
//            return new Color(204, 0, 255);
//        }
        if (color == StyleColorNames.AQUA) {
            return new Color(0, 255, 255);
        }
        if (color == StyleColorNames.FUCHSIA) {
            return new Color(255, 0, 255);
        }
        if (color == StyleColorNames.LIME) {
            return Color.green;
        }
        if (color == StyleColorNames.MAROON) {
            return new Color(128, 0, 0);
        }
        if (color == StyleColorNames.NAVY) {
            return new Color(0, 0, 128);
        }
        if (color == StyleColorNames.OLIVE) {
            return new Color(128, 128, 0);
        }
        if (color == StyleColorNames.PURPLE) {
            return new Color(128, 0, 128);
        }
        if (color == StyleColorNames.TEAL) {
            return new Color(0, 128, 128);
        }
//        if (cSSColor.equalsIgnoreCase("dark gray")) {
//            return new Color(51, 51, 51);
//        }
//        if (cSSColor.equalsIgnoreCase("tan")) {
//            return new Color(255, 204, 153);
//        }

        if (color instanceof StyleColorRGB) {
            return new Color(((StyleColorRGB) color).getRGB());
        }

        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 07-Nov-05	10116/2	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/2	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/8	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6076/3	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 09-Nov-04	6027/6	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 08-Nov-04	6027/4	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/5	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 21-Oct-04	5895/1	geoff	VBM:2004101503 chartimage path rendered icnorrectly when deployed as ROOT webapp

 21-Oct-04	5884/1	geoff	VBM:2004101503 chartimage path rendered icnorrectly when deployed as ROOT webapp

 28-Sep-04	5683/1	pcameron	VBM:2004092802 Fixed incorrect chart path

 28-Sep-04	5681/1	pcameron	VBM:2004092802 Fixed incorrect chart path

 23-Sep-04	5631/1	pcameron	VBM:2004092202 Fixed chart filename generation

 23-Sep-04	5608/3	pcameron	VBM:2004092202 Fixed chart filename generation

 11-Aug-04	5139/1	geoff	VBM:2004080311 Implement Null Assets: ObjectSelectionPolicys

 21-Jul-04	4874/5	byron	VBM:2004070601 Upgrade Davisor chart package to v4.2 - fix test case

 20-Jul-04	4874/3	byron	VBM:2004070601 Upgrade Davisor chart package to v4.2

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 10-May-04	4257/1	geoff	VBM:2004051002 Enhance Menu Support: Integration Bugs: NPE in getPageConnection

 19-Feb-04	2789/6	tony	VBM:2004012601 refactored localised logging to synergetics

 16-Feb-04	2789/4	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
