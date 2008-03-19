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

import com.volantis.mcs.accessors.xml.jibx.ComponentContainer;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.AudioAsset;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.DeviceAudioAsset;
import com.volantis.mcs.assets.DeviceImageAsset;
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.GenericImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.LinkAsset;
import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.assets.SubstantiveAsset;
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.components.AudioComponent;
import com.volantis.mcs.components.ButtonImageComponent;
import com.volantis.mcs.components.ChartComponent;
import com.volantis.mcs.components.DynamicVisualComponent;
import com.volantis.mcs.components.FallsBackToImage;
import com.volantis.mcs.components.FallsBackToText;
import com.volantis.mcs.components.ImageComponent;
import com.volantis.mcs.components.LinkComponent;
import com.volantis.mcs.components.RolloverImageComponent;
import com.volantis.mcs.components.ScriptComponent;
import com.volantis.mcs.components.TextComponent;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.objects.CacheableRepositoryObject;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.ConcretePolicyBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.BaseURLRelativeBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.Iterator;
import java.util.List;

/**
 * Converts new policies into old components and assets.
 */
public class Old2NewConverter {

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    Old2NewConverter.class);

    private final ModelFactory modelFactory = ModelFactory.getDefaultInstance();

    private final PolicyFactory factory;

    public Old2NewConverter(PolicyFactory factory) {
        this.factory = factory;
    }

    public Old2NewConverter() {
        this(PolicyFactory.getDefaultInstance());
    }

    /**
     * Convert an old container into a new policy builder.
     * @param container The old container.
     * @return The new policy builder.
     */
    public PolicyBuilder oldContainer2NewPolicyBuilder(ComponentContainer container) {
        PolicyBuilder policyBuilder = component2VariablePolicyBuilder(container.getComponent());
        if (policyBuilder instanceof VariablePolicyBuilder) {
            VariablePolicyBuilder variablePolicyBuilder =
                    (VariablePolicyBuilder) policyBuilder;

            List assets = container.getAssets();
            for (Iterator i = assets.iterator(); i.hasNext();) {
                Asset asset = (Asset) i.next();
                VariantBuilder variantBuilder = oldAsset2NewVariantBuilder(asset);
                variablePolicyBuilder.addVariantBuilder(variantBuilder);
            }
        }

        return policyBuilder;
    }

    public PolicyBuilder component2VariablePolicyBuilder(CacheableRepositoryObject object) {
        VariablePolicyBuilder newVariablePolicyBuilder = null;
        PolicyBuilder newPolicyBuilder = null;
        if (object instanceof AssetGroup) {
            AssetGroup old = (AssetGroup) object;
            newPolicyBuilder = convertAssetGroup(old);
        } else if (object instanceof AudioComponent) {
            AudioComponent old = (AudioComponent) object;
            newVariablePolicyBuilder = convertAudioComponent(old);
        } else if (object instanceof ButtonImageComponent) {
            ButtonImageComponent old = (ButtonImageComponent) object;
            newPolicyBuilder = convertButtonImageComponent(old);
        } else if (object instanceof ChartComponent) {
            ChartComponent old = (ChartComponent) object;
            newVariablePolicyBuilder = convertChartComponent(old);
        } else if (object instanceof DynamicVisualComponent) {
            DynamicVisualComponent old = (DynamicVisualComponent) object;
            newVariablePolicyBuilder = convertDynamicVisualComponent(old);
        } else if (object instanceof ImageComponent) {
            ImageComponent old = (ImageComponent) object;
            newVariablePolicyBuilder = convertImageComponent(old);
        } else if (object instanceof LinkComponent) {
            LinkComponent old = (LinkComponent) object;
            newVariablePolicyBuilder = convertLinkComponent(old);
        } else if (object instanceof RolloverImageComponent) {
            RolloverImageComponent old = (RolloverImageComponent) object;
            newPolicyBuilder = convertRolloverImageComponent(old);
        } else if (object instanceof ScriptComponent) {
            newVariablePolicyBuilder = convertScriptComponent();
        } else if (object instanceof TextComponent) {
            TextComponent old = (TextComponent) object;
            newVariablePolicyBuilder = convertTextComponent(old);
        } else {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "policy.conversion.unknown.old.policy", object));
        }

        if (newVariablePolicyBuilder != null) {
            newPolicyBuilder = newVariablePolicyBuilder;
        }

        newPolicyBuilder.setName(object.getName());
        newPolicyBuilder.setCacheControlBuilder(createCacheControl(object));

        return newPolicyBuilder;
    }

    public VariantBuilder asset2VariantBuilder(RepositoryObject object) {
        VariantBuilder variantBuilder;
        if (object instanceof Asset) {
            variantBuilder = oldAsset2NewVariantBuilder((Asset) object);
        } else {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "policy.conversion.unknown.type", object));
        }

        validateVariantBuilder(variantBuilder);

        return variantBuilder;
    }

    private VariantBuilder oldAsset2NewVariantBuilder(Asset asset) {

        VariantBuilder variantBuilder;
        if (asset instanceof DeviceAudioAsset) {
            DeviceAudioAsset old = (DeviceAudioAsset) asset;
            variantBuilder = createAudioVariant(old);
            variantBuilder.setSelectionBuilder(createDeviceTargeted(old.getDeviceName()));

        } else if (asset instanceof AudioAsset) {
            AudioAsset old = (AudioAsset) asset;

            variantBuilder = createAudioVariant(old);
            variantBuilder.setSelectionBuilder(factory.createEncodingSelectionBuilder());

        } else if (asset instanceof DynamicVisualAsset) {
            DynamicVisualAsset old = (DynamicVisualAsset) asset;

            int oldEncoding = old.getEncoding();
            VideoEncoding encoding = (VideoEncoding)
                    EnumerationConverter.VIDEO_ENCODING.get(oldEncoding);
            if (encoding == null) {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "policy.conversion.video.encoding.not.supported",
                        new Integer(oldEncoding)));
            }

            VideoMetaDataBuilder videoBuilder =
                    factory.createVideoMetaDataBuilder();
            videoBuilder.setVideoEncoding(encoding);
            videoBuilder.setWidth(old.getPixelsX());
            videoBuilder.setHeight(old.getPixelsY());

            variantBuilder = factory.createVariantBuilder(VariantType.VIDEO);
            variantBuilder.setSelectionBuilder(factory.createEncodingSelectionBuilder());
            variantBuilder.setMetaDataBuilder(videoBuilder);
            variantBuilder.setContentBuilder(getURLContent(old));

        } else if (asset instanceof ConvertibleImageAsset) {
            ConvertibleImageAsset old = (ConvertibleImageAsset) asset;

            variantBuilder = createImageVariant(
                    old, ImageConversionMode.ALWAYS_CONVERT, false);
            DefaultSelectionBuilder defaultSelection =
                    factory.createDefaultSelectionBuilder();
            variantBuilder.setSelectionBuilder(defaultSelection);

        } else if (asset instanceof GenericImageAsset) {
            GenericImageAsset old = (GenericImageAsset) asset;

            variantBuilder = createImageVariant(
                            old, ImageConversionMode.NEVER_CONVERT, false);
            GenericImageSelectionBuilder generic =
                    factory.createGenericImageSelectionBuilder();
            generic.setWidthHint(old.getWidthHint());
            variantBuilder.setSelectionBuilder(generic);

        } else if (asset instanceof DeviceImageAsset) {
            DeviceImageAsset old = (DeviceImageAsset) asset;

            variantBuilder = createImageVariant(
                    old, ImageConversionMode.NEVER_CONVERT, old.isLocalSrc());
            variantBuilder.setSelectionBuilder(createDeviceTargeted(old.getDeviceName()));
        } else if (asset instanceof LinkAsset) {
            LinkAsset old = (LinkAsset) asset;

            variantBuilder = factory.createVariantBuilder(VariantType.LINK);
            variantBuilder.setSelectionBuilder(createDeviceTargeted(old.getDeviceName()));
            variantBuilder.setMetaDataBuilder(null);
            variantBuilder.setContentBuilder(getURLContent(old));
        } else if (asset instanceof ScriptAsset) {
            ScriptAsset old = (ScriptAsset) asset;

            String oldProgrammingLanguage = old.getProgrammingLanguage();
            ScriptEncoding encoding = (ScriptEncoding)
                    EnumerationConverter.SCRIPT_PROGRAMMING_LANGUAGE.get(
                            oldProgrammingLanguage);
            if (encoding == null) {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "policy.conversion.script.language.not.supported",
                        oldProgrammingLanguage));
            }

            ScriptMetaDataBuilder scriptBuilder =
                    factory.createScriptMetaDataBuilder();
            scriptBuilder.setScriptEncoding(encoding);
            scriptBuilder.setCharacterSet(old.getCharacterSet());

            ContentBuilder content;
            int valueType = old.getValueType();
            if (valueType == ScriptAsset.URL) {
                content = getURLContent(old);
            } else if (valueType == ScriptAsset.LITERAL) {
                content = getTextualContent(old);
            } else {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "policy.conversion.script.value.type.not.supported",
                        new Integer(valueType)));
            }

            variantBuilder = factory.createVariantBuilder(VariantType.SCRIPT);
            variantBuilder.setSelectionBuilder(createDeviceTargeted(old.getDeviceName()));
            variantBuilder.setMetaDataBuilder(scriptBuilder);
            variantBuilder.setContentBuilder(content);
        } else if (asset instanceof TextAsset) {
            TextAsset old = (TextAsset) asset;

            String language = old.getLanguage();
            if (language != null &&
                    !language.equals(TextAsset.DEFAULT_LANGUAGE)) {

                throw new IllegalArgumentException(
                        EXCEPTION_LOCALIZER.format(
                                "policy.conversion.text.language.not.supported"));
            }

            int oldEncoding = old.getEncoding();
            TextEncoding encoding = (TextEncoding)
                    EnumerationConverter.TEXT_ENCODING.get(oldEncoding);
            if (encoding == null) {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "policy.conversion.audio.encoding.not.supported",
                        new Integer(oldEncoding)));
            }

            ContentBuilder content;
            int valueType = old.getValueType();
            if (valueType == TextAsset.URL) {
                content = getURLContent(old);
            } else if (valueType == TextAsset.LITERAL) {
                content = getTextualContent(old);
            } else {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "policy.conversion.text.value.type.not.supported",
                        new Integer(valueType)));
            }

            TextMetaDataBuilder textBuilder =
                    factory.createTextMetaDataBuilder();
            textBuilder.setTextEncoding(encoding);

            variantBuilder = factory.createVariantBuilder(VariantType.TEXT);
            variantBuilder.setSelectionBuilder(createDeviceTargeted(old.getDeviceName()));
            variantBuilder.setMetaDataBuilder(textBuilder);
            variantBuilder.setContentBuilder(content);
        } else if (asset instanceof ChartAsset) {
            ChartAsset old = (ChartAsset) asset;

            String oldChartType = old.getType();
            ChartType chartType = (ChartType) EnumerationConverter.CHART_TYPE.get(
                    oldChartType);

            ChartMetaDataBuilder chartBuilder =
                    factory.createChartMetaDataBuilder();
            chartBuilder.setChartType(chartType);
            chartBuilder.setHeightHint(old.getHeightHint());
            chartBuilder.setWidthHint(old.getWidthHint());

            AxisMetaDataBuilder xAxisBuilder = factory.createAxisBuilder();
            xAxisBuilder.setInterval(old.getXInterval());
            xAxisBuilder.setTitle(old.getXTitle());
            chartBuilder.setXAxisBuilder(xAxisBuilder);

            AxisMetaDataBuilder yAxisBuilder = factory.createAxisBuilder();
            yAxisBuilder.setInterval(old.getYInterval());
            yAxisBuilder.setTitle(old.getYTitle());
            chartBuilder.setYAxisBuilder(yAxisBuilder);

            variantBuilder = factory.createVariantBuilder(VariantType.CHART);
            variantBuilder.setSelectionBuilder(factory.createDefaultSelectionBuilder());
            variantBuilder.setMetaDataBuilder(chartBuilder);
            variantBuilder.setContentBuilder(null);
        } else {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "policy.conversion.unknown.asset.type", asset));
        }

        return variantBuilder;
    }

    private void validateVariantBuilder(VariantBuilder variant) {

        StrictValidator strictValidator = modelFactory.createStrictValidator();
        // Validation of a variants can only occur properly within a whole
        // policy, e.g. device and category variants have to be unique across
        // the whole policy. Therefore, set up anything that the policy would
        // normally set up.
        ValidationContext context = strictValidator.getValidationContext();
        context.beginDefinitionScope(PolicyModel.DEVICE_TARGETED);
        context.beginDefinitionScope(PolicyModel.CATEGORY_TARGETED);
        context.beginDefinitionScope(PolicyModel.DEFAULT_SELECTION);

        strictValidator.validate((Validatable) variant);

        context.endDefinitionScope(PolicyModel.DEFAULT_SELECTION);
        context.endDefinitionScope(PolicyModel.CATEGORY_TARGETED);
        context.endDefinitionScope(PolicyModel.DEVICE_TARGETED);
    }

    private ContentBuilder getTextualContent(SubstantiveAsset substantive) {
        EmbeddedContentBuilder embedded = factory.createEmbeddedContentBuilder();
        embedded.setData(substantive.getValue());
        return embedded;
    }

    private VariantBuilder createImageVariant(
            ImageAsset old,
            ImageConversionMode conversionMode,
            boolean localSrc) {

        int oldEncoding = old.getEncoding();
        ImageEncoding encoding = (ImageEncoding)
                EnumerationConverter.IMAGE_ENCODING.get(oldEncoding);
        if (encoding == null) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "policy.conversion.image.encoding.not.supported",
                    new Integer(oldEncoding)));
        }

        int oldRendering = old.getRendering();
        ImageRendering rendering = (ImageRendering) EnumerationConverter.IMAGE_RENDERING.get(
                oldRendering);
        if (rendering == null) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "policy.conversion.image.rendering.not.supported",
                    new Integer(oldRendering)));
        }

        ImageMetaDataBuilder imageBuilder =
                factory.createImageMetaDataBuilder();
        imageBuilder.setImageEncoding(encoding);
        imageBuilder.setWidth(old.getPixelsX());
        imageBuilder.setHeight(old.getPixelsY());
        imageBuilder.setRendering(rendering);
        imageBuilder.setPixelDepth(old.getPixelDepth());
        imageBuilder.setConversionMode(conversionMode);

        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.IMAGE);
        variantBuilder.setMetaDataBuilder(imageBuilder);

        ContentBuilder content;
        if (old.isSequence()) {
            AutoURLSequenceBuilder sequence =
                    factory.createAutoURLSequenceBuilder();
            sequence.setSequenceSize(old.getSequenceSize());
            sequence.setURLTemplate(old.getValue());
            addBaseURLReference(old, sequence);
            content = sequence;
        } else {
            content = getURLContent(old);
        }
        if (localSrc) {
            BaseURLRelativeBuilder relative = (BaseURLRelativeBuilder) content;
            relative.setBaseLocation(BaseLocation.DEVICE);
        }
        variantBuilder.setContentBuilder(content);

        return variantBuilder;
    }

    private SelectionBuilder createDeviceTargeted(String deviceName) {
        TargetedSelectionBuilder targeted =
                factory.createTargetedSelectionBuilder();
        if (deviceName != null) {
            DeviceReference reference = factory.createDeviceReference(
                    deviceName);
            targeted.getModifiableDeviceReferences().add(reference);
        }
        return targeted;
    }

    private VariantBuilder createAudioVariant(AudioAsset old) {
        int oldEncoding = old.getEncoding();
        AudioEncoding encoding = (AudioEncoding)
                EnumerationConverter.AUDIO_ENCODING.get(oldEncoding);
        if (encoding == null) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "policy.conversion.audio.encoding.not.supported",
                    new Integer(oldEncoding)));

        }

        AudioMetaDataBuilder audioBuilder =
                factory.createAudioMetaDataBuilder();
        audioBuilder.setAudioEncoding(encoding);

        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.AUDIO);
        variantBuilder.setMetaDataBuilder(audioBuilder);

        variantBuilder.setContentBuilder(getURLContent(old));
        return variantBuilder;
    }

    private URLContentBuilder getURLContent(SubstantiveAsset substantive) {

        URLContentBuilder content = factory.createURLContentBuilder();
        content.setURL(substantive.getValue());
        addBaseURLReference(substantive, content);

        return content;
    }

    private void addBaseURLReference(
            SubstantiveAsset substantive, BaseURLRelativeBuilder baseURLRelative) {
        String assetGroupName = substantive.getAssetGroupName();
        if (assetGroupName != null) {
            PolicyReference reference = factory.createPolicyReference(
                    assetGroupName, PolicyType.BASE_URL);
            baseURLRelative.setBaseURLPolicyReference(reference);
        }
    }

    private PolicyBuilder convertAssetGroup(AssetGroup old) {
        BaseURLPolicyBuilder policyBuilder =
                factory.createBaseURLPolicyBuilder();
        int location = old.getLocationType();
        if (location == AssetGroup.ON_DEVICE) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "policy.conversion.device.location.not.supported"));
        }
        // In 2.9, prefix URLs were dealt with weirdly. If they started
        // with / then they are host relative, otherwise they are webapp
        // relative. We broke this in 3.0 and only realised in 351 so we
        // had to go back to 3.0 schema and translate ON_SERVER into HOST
        // or CONTEXT. See MarlinLPDM.printComponent(AssetGroup).
        if (location == AssetGroup.ON_SERVER) {
            if (old.getPrefixURL() != null && old.getPrefixURL().startsWith("/")) {
                policyBuilder.setBaseLocation(BaseLocation.HOST);
            }
            // else, default to context.
        }
        policyBuilder.setBaseURL(old.getPrefixURL());

        return policyBuilder;
    }

    private VariablePolicyBuilder convertAudioComponent(AudioComponent old) {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.AUDIO);

        // Process the alternate audio.
        addAlternateReference(old.getFallbackAudioComponentName(),
                PolicyType.AUDIO, policyBuilder);

        // Process the alternate text.
        addAlternateText(old, policyBuilder);

        return policyBuilder;
    }

    private VariablePolicyBuilder convertChartComponent(ChartComponent old) {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.CHART);

        // Process the alternate chart.
        addAlternateReference(old.getFallbackChartComponentName(),
                PolicyType.CHART, policyBuilder);

        // Process the alternate image.
        addAlternateImage(old, policyBuilder);

        // Process the alternate text.
        addAlternateText(old, policyBuilder);

        return policyBuilder;
    }

    private VariablePolicyBuilder convertDynamicVisualComponent(DynamicVisualComponent old) {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.VIDEO);

        // Process the alternate dynvis.
        addAlternateReference(old.getFallbackDynVisComponentName(),
                PolicyType.VIDEO, policyBuilder);

        // Process the alternate audio.
        addAlternateReference(old.getFallbackAudioComponentName(),
                PolicyType.AUDIO, policyBuilder);

        // Process the alternate image.
        addAlternateImage(old, policyBuilder);

        // Process the alternate text.
        addAlternateText(old, policyBuilder);

        return policyBuilder;
    }

    private VariablePolicyBuilder convertImageComponent(ImageComponent old) {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.IMAGE);

        // Process the alternate image.
        addAlternateReference(old.getFallbackImageComponentName(),
                PolicyType.IMAGE, policyBuilder);

        // Process the alternate text.
        addAlternateText(old, policyBuilder);

        return policyBuilder;
    }

    private VariablePolicyBuilder convertLinkComponent(LinkComponent old) {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.LINK);

        // Process the alternate text.
        addAlternateText(old, policyBuilder);

        return policyBuilder;
    }

    private PolicyBuilder convertButtonImageComponent(ButtonImageComponent old) {
        ButtonImagePolicyBuilder policyBuilder =
                factory.createButtonImagePolicyBuilder();

        String reference;

        // Process the down image reference.
        reference = old.getDownImageComponentName();
        if (reference != null) {
            policyBuilder.setDownPolicy(
                    factory.createPolicyReference(reference, PolicyType.IMAGE));
        }

        // Process the over image reference.
        reference = old.getOverImageComponentName();
        if (reference != null) {
            policyBuilder.setOverPolicy(
                    factory.createPolicyReference(reference, PolicyType.IMAGE));
        }

        // Process the up image reference.
        reference = old.getUpImageComponentName();
        if (reference != null) {
            policyBuilder.setUpPolicy(
                    factory.createPolicyReference(reference, PolicyType.IMAGE));
        }

        // Process the alternate text.
        addAlternateText(old, policyBuilder);

        return policyBuilder;
    }

    private PolicyBuilder convertRolloverImageComponent(RolloverImageComponent old) {
        RolloverImagePolicyBuilder policyBuilder =
                factory.createRolloverImagePolicyBuilder();

        String reference;

        // Process the normal image reference.
        reference = old.getNormalImageComponentName();
        if (reference != null) {
            policyBuilder.setNormalPolicy(
                    factory.createPolicyReference(reference, PolicyType.IMAGE));
        }

        // Process the over image reference.
        reference = old.getOverImageComponentName();
        if (reference != null) {
            policyBuilder.setOverPolicy(
                    factory.createPolicyReference(reference, PolicyType.IMAGE));
        }

        // Process the alternate text.
        addAlternateText(old, policyBuilder);

        return policyBuilder;
    }

    private VariablePolicyBuilder convertScriptComponent() {
        VariablePolicyBuilder policyBuilder = factory.createVariablePolicyBuilder(PolicyType.SCRIPT);
        return policyBuilder;
    }

    private VariablePolicyBuilder convertTextComponent(TextComponent old) {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.TEXT);

        // Process the alternate dynvis.
        addAlternateReference(old.getFallbackTextComponentName(),
                PolicyType.TEXT, policyBuilder);

        return policyBuilder;
    }

    private void addAlternateReference(
            String alternate, PolicyType expectedPolicyType,
            ConcretePolicyBuilder policyBuilder) {
        if (alternate != null) {
            PolicyReference ref = factory.createPolicyReference(
                    alternate, expectedPolicyType);
            policyBuilder.addAlternatePolicy(ref);
        }
    }

    private void addAlternateImage(
            FallsBackToImage old, ConcretePolicyBuilder policyBuilder) {

        addAlternateReference(old.getFallbackImageComponentName(),
                PolicyType.IMAGE, policyBuilder);
    }

    private void addAlternateText(FallsBackToText old, ConcretePolicyBuilder policyBuilder) {
        addAlternateReference(old.getFallbackTextComponentName(),
                PolicyType.TEXT, policyBuilder);
    }

    private CacheControlBuilder createCacheControl(CacheableRepositoryObject old) {

        CacheControlBuilder controlBuilder;
        if (old.getCacheThisPolicy()) {
            controlBuilder = factory.createCacheControlBuilder();

            controlBuilder.setCacheThisPolicy(old.getCacheThisPolicy());
            controlBuilder.setRetainDuringRetry(old.getRetainDuringRetry());
            controlBuilder.setRetryFailedRetrieval(old.getRetryFailedRetrieval());
            controlBuilder.setRetryInterval(old.getRetryInterval());
            controlBuilder.setRetryMaxCount(old.getRetryMaxCount());
            controlBuilder.setTimeToLive(old.getTimeToLive());
        } else {
            controlBuilder = null;
        }

        return controlBuilder;
    }
}
