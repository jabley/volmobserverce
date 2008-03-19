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

package com.volantis.mcs.policies;

import com.volantis.mcs.model.descriptor.BaseClassDescriptor;
import com.volantis.mcs.model.descriptor.BeanDescriptorBuilder;
import com.volantis.mcs.model.descriptor.DescriptorFactory;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ModelObjectFactory;
import com.volantis.mcs.model.descriptor.PropertyAccessor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeBuilder;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeHandler;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.layout.LayoutContentBuilder;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaDataBuilder;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.EncodingSelectionBuilder;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.themes.model.ThemeModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PolicyModel {
    public static final DefinitionType DEVICE_TARGETED;
    public static final DefinitionType CATEGORY_TARGETED;
    public static final DefinitionType DEFAULT_SELECTION;

    public static final ModelDescriptor MODEL_DESCRIPTOR;

    /**
     * The identifier for the variantType property.
     */
    public static final PropertyIdentifier VARIANT_TYPE =
            new PropertyIdentifier(VariantBuilder.class, "variantType");
    
    public static final PropertyIdentifier CATEGORISATION_SCHEME =
            new PropertyIdentifier(VariablePolicyBuilder.class, "categorisationScheme");
    
    /**
     * The identifier for the selection property.
     */
    public static final PropertyIdentifier SELECTION =
            new PropertyIdentifier(VariantBuilder.class, "selection");
    /**
     * The identifier for the metaData property.
     */
    public static final PropertyIdentifier META_DATA =
            new PropertyIdentifier(VariantBuilder.class, "metaData");
    /**
     * The identifier for the content property.
     */
    public static final PropertyIdentifier CONTENT =
            new PropertyIdentifier(VariantBuilder.class, "content");

    /**
     * The identifier for the width hint property.
     */
    public static final PropertyIdentifier WIDTH_HINT =
            new PropertyIdentifier(ChartMetaDataBuilder.class, "widthHint");

    /**
     * The identifier for the height hint property.
     */
    public static final PropertyIdentifier HEIGHT_HINT =
            new PropertyIdentifier(ChartMetaDataBuilder.class, "heightHint");

    public static final PropertyIdentifier X_INTERVAL =
            new PropertyIdentifier(ChartMetaDataBuilder.class, "xInterval");

    public static final PropertyIdentifier Y_INTERVAL =
            new PropertyIdentifier(ChartMetaDataBuilder.class, "yInterval");

    public static final PropertyIdentifier X_TITLE =
            new PropertyIdentifier(ChartMetaDataBuilder.class, "xTitle");

    public static final PropertyIdentifier Y_TITLE =
            new PropertyIdentifier(ChartMetaDataBuilder.class, "yTitle");

    public static final PropertyIdentifier PIXEL_DEPTH =
            new PropertyIdentifier(ImageMetaDataBuilder.class, "pixelDepth");

    public static final PropertyIdentifier RENDERING =
            new PropertyIdentifier(ImageMetaDataBuilder.class, "rendering");

    public static final PropertyIdentifier CONVERSION_MODE =
            new PropertyIdentifier(ImageMetaDataBuilder.class, "conversionMode");

    public static final PropertyIdentifier PRESERVE_LEFT =
            new PropertyIdentifier(ImageMetaDataBuilder.class, "preserveLeft");

    public static final PropertyIdentifier PRESERVE_RIGHT =
            new PropertyIdentifier(ImageMetaDataBuilder.class, "preserveRight");

    public static final PropertyIdentifier WIDTH =
            new PropertyIdentifier(PixelDimensionsMetaDataBuilder.class,
                    "assetWidth", "width");

    public static final PropertyIdentifier HEIGHT =
            new PropertyIdentifier(PixelDimensionsMetaDataBuilder.class,
                    "assetHeight", "height");
    
    public static final PropertyIdentifier CHARACTER_SET =
            new PropertyIdentifier(ScriptMetaDataBuilder.class, "characterSet");

    public static final PropertyIdentifier CHART_TYPE =
            new PropertyIdentifier(ChartMetaDataBuilder.class, "chartType");
    
    public static final PropertyIdentifier ALTERNATE_POLICIES =
            new PropertyIdentifier(ConcretePolicyBuilder.class, "alternatePolicies");
    
    public static final PropertyIdentifier NORMAL_POLICY =
            new PropertyIdentifier(RolloverImagePolicyBuilder.class, "normalPolicy");

    public static final PropertyIdentifier OVER_POLICY =
            new PropertyIdentifier(RolloverImagePolicyBuilder.class, "overPolicy");

    public static final PropertyIdentifier BASE_URL =
            new PropertyIdentifier(BaseURLPolicyBuilder.class, "baseURL");

    public static final PropertyIdentifier BASE_LOCATION =
            new PropertyIdentifier(BaseURLPolicyBuilder.class, "baseLocation");

    /**
     * The identifier for the variants property.
     */
    public static final PropertyIdentifier VARIANTS =
            new PropertyIdentifier(VariablePolicyBuilder.class, "variants");

    /**
     * The identifier for the data property.
     */
    public static final PropertyIdentifier DATA =
            new PropertyIdentifier(EmbeddedContentBuilder.class, "data");

    /**
     * The identifier for the URL property.
     */
    public static final PropertyIdentifier URL =
            new PropertyIdentifier(URLContentBuilder.class, "url");

    /**
     * The identifier for the device references property.
     */
    public static final PropertyIdentifier DEVICE_REFERENCES =
            new PropertyIdentifier(TargetedSelectionBuilder.class, "deviceReferences");

    /**
     * The identifier for the device references property.
     */
    public static final PropertyIdentifier CATEGORY_REFERENCES =
            new PropertyIdentifier(TargetedSelectionBuilder.class, "categoryReferences");
    
    /**
     * The identifier for the encoding property.
     */
    public static final PropertyIdentifier ENCODING =
            new PropertyIdentifier(MetaDataBuilder.class, "encoding");

    private static final PolicyFactory POLICY_FACTORY = PolicyFactory.getDefaultInstance();

    static {
        DefinitionTypeBuilder builder = new DefinitionTypeBuilder();

        builder.setDefinitionTypeHandler(
                new DeviceTargetedDefinitionTypeHandler());
        DEVICE_TARGETED = builder.getDefinitionType();

        builder.setDefinitionTypeHandler(
                new CategoryTargetedDefinitionTypeHandler());
        CATEGORY_TARGETED = builder.getDefinitionType();

        builder.setDefinitionTypeHandler(
                new DefaultSelectionDefinitionTypeHandler());
        DEFAULT_SELECTION = builder.getDefinitionType();
    }

    static {
        DescriptorFactory factory =
                DescriptorFactory.getDefaultInstance();

        ModelDescriptorBuilder builder = factory.createModelDescriptorBuilder();

        // Shared opaque classes
        builder.addOpaqueClassDescriptor(Encoding.class);
        builder.addOpaqueClassDescriptor(ImageRendering.class);
        builder.addOpaqueClassDescriptor(ImageConversionMode.class);
        builder.addOpaqueClassDescriptor(ChartType.class);
        builder.addOpaqueClassDescriptor(PolicyReference.class);
        builder.addOpaqueClassDescriptor(VariantType.class);

        // Build non-variable policies
        buildBaseURLPolicy(builder);
        buildRolloverImagePolicy(builder);

        BeanDescriptorBuilder variantBuilder = builder.getBeanBuilder(
                VariantBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return POLICY_FACTORY.createVariantBuilder(null);
                    }
                });

        BaseClassDescriptor metaDataDescriptor = builder.addBaseClassDescriptor(MetaDataBuilder.class);

        variantBuilder.addPropertyDescriptor(VARIANT_TYPE, builder.getTypeDescriptor(VariantType.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((VariantBuilder) object).setVariantType((VariantType) value);
            }

            public Object get(Object object) {
                return ((VariantBuilder) object).getVariantType();
            }
        }, false);

        variantBuilder.addPropertyDescriptor(META_DATA, metaDataDescriptor, new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((VariantBuilder) object).setMetaDataBuilder((MetaDataBuilder) value);
            }

            public Object get(Object object) {
                return ((VariantBuilder) object).getMetaDataBuilder();
            }
        }, false);

        // Create the bean descriptors for VariablePolicyBuilder.
        BeanDescriptorBuilder variablePolicyBuilder = builder.getBeanBuilder(
                VariablePolicyBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return POLICY_FACTORY.createVariablePolicyBuilder(null);
                    }
                });

        variablePolicyBuilder.addPropertyDescriptor(VARIANTS,
                builder.getStandardListDescriptor(ArrayList.class,
                        builder.getClassDescriptor(VariantBuilder.class)),
                new PropertyAccessor() {
                    public void set(Object object, Object value) {
                        throw new UnsupportedOperationException();
                    }

                    public Object get(Object object) {
                        return ((VariablePolicyBuilder) object).getVariantBuilders();
                    }
                }, true);

        variablePolicyBuilder.addPropertyDescriptor(ALTERNATE_POLICIES,
                builder.getStandardListDescriptor(ArrayList.class,
                        builder.getClassDescriptor(PolicyReference.class)),
                new PropertyAccessor() {
                    public void set(Object object, Object value) {
                        throw new UnsupportedOperationException();
                    }

                    public Object get(Object object) {
                        return ((VariablePolicyBuilder) object).getAlternatePolicies();
                    }
                }, true);

        variablePolicyBuilder.addPropertyDescriptor(CATEGORISATION_SCHEME,
                builder.getTypeDescriptor(String.class), new PropertyAccessor() {
                    public void set(Object object, Object value) {
                        ((VariablePolicyBuilder) object).setCategorizationScheme((String) value);
                    }

                    public Object get(Object object) {
                        return ((VariablePolicyBuilder) object).getCategorizationScheme();
                    }
                }, false);

        // Construct the bean descriptors for the various metadata types
        buildAudioMetaData(builder);
        buildChartMetaData(builder);
        buildImageMetaData(builder);
        buildScriptMetaData(builder);
        buildTextMetaData(builder);
        buildVideoMetaData(builder);

        // Construct the bean descriptor for theme model content
        ThemeModel.buildThemeModelDescriptor(builder);

        BaseClassDescriptor selectionDescriptor = builder.addBaseClassDescriptor(SelectionBuilder.class);

        variantBuilder.addPropertyDescriptor(SELECTION, selectionDescriptor, new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((VariantBuilder) object).setSelectionBuilder((SelectionBuilder) value);
            }

            public Object get(Object object) {
                return ((VariantBuilder) object).getSelectionBuilder();
            }
        }, false);

        BeanDescriptorBuilder targetedSelectionBuilder = builder.getBeanBuilder(
                TargetedSelectionBuilder.class, new ModelObjectFactory() {
            public Object createObject() {
                return POLICY_FACTORY.createTargetedSelectionBuilder();
            }
        });

        builder.addOpaqueClassDescriptor(CategoryReference.class);

        targetedSelectionBuilder.addPropertyDescriptor(CATEGORY_REFERENCES,
                builder.getStandardListDescriptor(ArrayList.class,
                        builder.getClassDescriptor(CategoryReference.class)),
                        new PropertyAccessor() {
                            public void set(Object object, Object value) {
                                Collection newValues = (Collection) value;
                                TargetedSelectionBuilder selection = (TargetedSelectionBuilder) object;
                                List referenceList = selection.getModifiableCategoryReferences();
                                referenceList.clear();
                                referenceList.addAll(newValues);
                            }

                            public Object get(Object object) {
                                return ((TargetedSelectionBuilder) object).getModifiableCategoryReferences();
                            }
                        }, false);

        builder.addOpaqueClassDescriptor(DeviceReference.class);

        targetedSelectionBuilder.addPropertyDescriptor(DEVICE_REFERENCES,
                builder.getStandardListDescriptor(ArrayList.class,
                        builder.getClassDescriptor(DeviceReference.class)),
                        new PropertyAccessor() {
                            public void set(Object object, Object value) {
                                Collection newValues = (Collection) value;
                                TargetedSelectionBuilder selection = (TargetedSelectionBuilder) object;
                                List referenceList = selection.getModifiableDeviceReferences();
                                referenceList.clear();
                                referenceList.addAll(newValues);
                            }

                            public Object get(Object object) {
                                return ((TargetedSelectionBuilder) object).getModifiableDeviceReferences();
                            }
                        }, false);

        builder.addOpaqueClassDescriptor(EncodingSelectionBuilder.class);
        builder.addOpaqueClassDescriptor(DefaultSelectionBuilder.class);

        BeanDescriptorBuilder genericImageSelectionBuilder = builder.getBeanBuilder(
                GenericImageSelectionBuilder.class, new ModelObjectFactory() {
            public Object createObject() {
                return POLICY_FACTORY.createGenericImageSelectionBuilder();
            }
        });

        genericImageSelectionBuilder.addPropertyDescriptor(WIDTH_HINT,
                builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {

            public void set(Object object, Object value) {
                GenericImageSelectionBuilder builder = (GenericImageSelectionBuilder) object;
                if (value instanceof Integer) {
                    builder.setWidthHint(((Integer) value).intValue());
                } else {
                    builder.setWidthHint(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((GenericImageSelectionBuilder) object).getWidthHint());
            }
        }, false);

        BaseClassDescriptor contentDescriptor = builder.addBaseClassDescriptor(ContentBuilder.class);
        builder.addOpaqueClassDescriptor(URLContentBuilder.class);
        builder.addOpaqueClassDescriptor(EmbeddedContentBuilder.class);
        builder.addOpaqueClassDescriptor(LayoutContentBuilder.class);
        builder.addOpaqueClassDescriptor(AutoURLSequenceBuilder.class);

        variantBuilder.addPropertyDescriptor(CONTENT, contentDescriptor, new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((VariantBuilder) object).setContentBuilder((ContentBuilder) value);
            }

            public Object get(Object object) {
                return ((VariantBuilder) object).getContentBuilder();
            }
        }, false);

        /*
        BeanDescriptorBuilder urlContentBuilder = builder.getBeanBuilder(
                URLContentBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return POLICY_FACTORY.createURLContentBuilder();
                    }
                });

        urlContentBuilder.addPropertyDescriptor(URL, builder.getClassDescriptor(String.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((URLContentBuilder) object).setURL((String) value);
            }

            public Object get(Object object) {
                return ((URLContentBuilder) object).getURL();
            }
        }, true);

        BeanDescriptorBuilder embeddedContentBuilder = builder.getBeanBuilder(
                EmbeddedContentBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return POLICY_FACTORY.createEmbeddedContentBuilder();
                    }
                });

        embeddedContentBuilder.addPropertyDescriptor(DATA, builder.getClassDescriptor(String.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((EmbeddedContentBuilder) object).setData((String) value);
            }

            public Object get(Object object) {
                return ((EmbeddedContentBuilder) object).getData();
            }
        }, true);
        */

        MODEL_DESCRIPTOR = builder.getModelDescriptor();
    }

    /**
     * Constructs the bean descriptor for the image meta data.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildImageMetaData(ModelDescriptorBuilder builder) {
        BeanDescriptorBuilder imageMetaDataBuilder = builder.getBeanBuilder(
                ImageMetaDataBuilder.class, new ModelObjectFactory() {
            public Object createObject() {
                return POLICY_FACTORY.createImageMetaDataBuilder();
            }
        });

        imageMetaDataBuilder.addPropertyDescriptor(ENCODING, builder.getTypeDescriptor(Encoding.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ImageMetaDataBuilder metaData = (ImageMetaDataBuilder) object;
                metaData.setImageEncoding((ImageEncoding) value);
            }

            public Object get(Object object) {
                return ((ImageMetaDataBuilder) object).getImageEncoding();
            }
        }, false);

        imageMetaDataBuilder.addPropertyDescriptor(PIXEL_DEPTH, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                Integer intVal = (Integer) value;
                if (intVal != null) {
                    ((ImageMetaDataBuilder) object).setPixelDepth(intVal.intValue());
                } else {
                    ((ImageMetaDataBuilder) object).setPixelDepth(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((ImageMetaDataBuilder) object).getPixelDepth());
            }
        }, false);

        imageMetaDataBuilder.addPropertyDescriptor(RENDERING, builder.getTypeDescriptor(ImageRendering.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((ImageMetaDataBuilder) object).setRendering((ImageRendering) value);
            }

            public Object get(Object object) {
                return ((ImageMetaDataBuilder) object).getRendering();
            }
        }, false);

        imageMetaDataBuilder.addPropertyDescriptor(WIDTH, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                Integer intVal = (Integer) value;
                if (intVal != null) {
                    ((PixelDimensionsMetaDataBuilder) object).setWidth(intVal.intValue());
                } else {
                    ((PixelDimensionsMetaDataBuilder) object).setWidth(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((PixelDimensionsMetaDataBuilder) object).getWidth());
            }
        }, false);

        imageMetaDataBuilder.addPropertyDescriptor(HEIGHT, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                Integer intVal = (Integer) value;
                if (intVal != null) {
                    ((PixelDimensionsMetaDataBuilder) object).setHeight(intVal.intValue());
                } else {
                    ((PixelDimensionsMetaDataBuilder) object).setHeight(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((PixelDimensionsMetaDataBuilder) object).getHeight());
            }
        }, false);

        imageMetaDataBuilder.addPropertyDescriptor(CONVERSION_MODE, builder.getTypeDescriptor(ImageConversionMode.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((ImageMetaDataBuilder) object).setConversionMode((ImageConversionMode) value);
            }

            public Object get(Object object) {
                return ((ImageMetaDataBuilder) object).getConversionMode();
            }
        }, false);

        imageMetaDataBuilder.addPropertyDescriptor(PRESERVE_LEFT, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                Integer intVal = (Integer) value;
                if (intVal != null) {
                    ((ImageMetaDataBuilder) object).setPreserveLeft(intVal.intValue());
                } else {
                    ((ImageMetaDataBuilder) object).setPreserveLeft(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((ImageMetaDataBuilder) object).getPreserveLeft());
            }
        }, false);

        imageMetaDataBuilder.addPropertyDescriptor(PRESERVE_RIGHT, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                Integer intVal = (Integer) value;
                if (intVal != null) {
                    ((ImageMetaDataBuilder) object).setPreserveRight(intVal.intValue());
                } else {
                    ((ImageMetaDataBuilder) object).setPreserveRight(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((ImageMetaDataBuilder) object).getPreserveRight());
            }
        }, false);
    }

    /**
     * Constructs the bean descriptor for the audio meta data.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildAudioMetaData(ModelDescriptorBuilder builder) {
        BeanDescriptorBuilder audioMetaDataBuilder = builder.getBeanBuilder(
                AudioMetaDataBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return POLICY_FACTORY.createAudioMetaDataBuilder();
                    }
                }
        );

        audioMetaDataBuilder.addPropertyDescriptor(ENCODING, builder.getTypeDescriptor(Encoding.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                AudioMetaDataBuilder metaData = (AudioMetaDataBuilder) object;
                metaData.setAudioEncoding((AudioEncoding) value);
            }

            public Object get(Object object) {
                AudioMetaDataBuilder metaData = (AudioMetaDataBuilder) object;
                return metaData.getAudioEncoding();
            }
        }, false);
    }

    /**
     * Constructs the bean descriptor for the chart meta data.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildChartMetaData(ModelDescriptorBuilder builder) {
        BeanDescriptorBuilder chartMetaDataBuilder = builder.getBeanBuilder(
                ChartMetaDataBuilder.class, new ModelObjectFactory() {
            public Object createObject() {
                return POLICY_FACTORY.createChartMetaDataBuilder();
            }
        });

        chartMetaDataBuilder.addPropertyDescriptor(CHART_TYPE, builder.getTypeDescriptor(ChartType.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                if (value instanceof ChartType) {
                    metaData.setChartType((ChartType) value);
                } else {
                    throw new IllegalArgumentException("Chart type must be of type ChartType");
                }
            }

            public Object get(Object object) {
                return ((ChartMetaDataBuilder) object).getChartType();
            }
        }, false);

        chartMetaDataBuilder.addPropertyDescriptor(WIDTH_HINT, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                if (value instanceof Integer) {
                    metaData.setWidthHint(((Integer) value).intValue());
                } else {
                    metaData.setWidthHint(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((ChartMetaDataBuilder) object).getWidthHint());
            }
        }, false);

        chartMetaDataBuilder.addPropertyDescriptor(HEIGHT_HINT, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                if (value instanceof Integer) {
                    metaData.setHeightHint(((Integer) value).intValue());
                } else {
                    metaData.setHeightHint(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((ChartMetaDataBuilder) object).getHeightHint());
            }
        }, false);

        chartMetaDataBuilder.addPropertyDescriptor(X_INTERVAL, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                AxisMetaDataBuilder axis = metaData.getXAxisBuilder();
                if (axis == null) {
                    axis = POLICY_FACTORY.createAxisBuilder();
                    metaData.setXAxisBuilder(axis);
                }
                if (value instanceof Integer) {
                    axis.setInterval(((Integer) value).intValue());
                } else {
                    axis.setInterval(0);
                }
            }

            public Object get(Object object) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                Integer value = null;
                AxisMetaDataBuilder axis = metaData.getXAxisBuilder();
                if (axis != null) {
                    value = new Integer(axis.getInterval());
                }
                return value;
            }
        }, false);

        chartMetaDataBuilder.addPropertyDescriptor(X_TITLE, builder.getTypeDescriptor(String.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                AxisMetaDataBuilder axis = metaData.getXAxisBuilder();
                if (axis == null) {
                    axis = POLICY_FACTORY.createAxisBuilder();
                    metaData.setXAxisBuilder(axis);
                }
                if (value instanceof String) {
                    axis.setTitle((String) value);
                } else {
                    axis.setTitle(null);
                }
            }

            public Object get(Object object) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                String value = null;
                AxisMetaDataBuilder axis = metaData.getXAxisBuilder();
                if (axis != null) {
                    value = axis.getTitle();
                }
                return value;
            }
        }, false);

        chartMetaDataBuilder.addPropertyDescriptor(Y_INTERVAL, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                AxisMetaDataBuilder axis = metaData.getYAxisBuilder();
                if (axis == null) {
                    axis = POLICY_FACTORY.createAxisBuilder();
                    metaData.setYAxisBuilder(axis);
                }
                if (value instanceof Integer) {
                    axis.setInterval(((Integer) value).intValue());
                } else {
                    axis.setInterval(0);
                }
            }

            public Object get(Object object) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                Integer value = null;
                AxisMetaDataBuilder axis = metaData.getYAxisBuilder();
                if (axis != null) {
                    value = new Integer(axis.getInterval());
                }
                return value;
            }
        }, false);

        chartMetaDataBuilder.addPropertyDescriptor(Y_TITLE, builder.getTypeDescriptor(String.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                AxisMetaDataBuilder axis = metaData.getYAxisBuilder();
                if (axis == null) {
                    axis = POLICY_FACTORY.createAxisBuilder();
                    metaData.setYAxisBuilder(axis);
                }
                if (value instanceof String) {
                    axis.setTitle((String) value);
                } else {
                    axis.setTitle(null);
                }
            }

            public Object get(Object object) {
                ChartMetaDataBuilder metaData = (ChartMetaDataBuilder) object;
                String value = null;
                AxisMetaDataBuilder axis = metaData.getYAxisBuilder();
                if (axis != null) {
                    value = axis.getTitle();
                }
                return value;
            }
        }, false);
    }

    /**
     * Constructs the bean descriptor for the script meta data.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildScriptMetaData(ModelDescriptorBuilder builder) {
        BeanDescriptorBuilder scriptMetaDataBuilder = builder.getBeanBuilder(
                ScriptMetaDataBuilder.class, new ModelObjectFactory() {
            public Object createObject() {
                return POLICY_FACTORY.createScriptMetaDataBuilder();
            }
        });

        scriptMetaDataBuilder.addPropertyDescriptor(ENCODING, builder.getTypeDescriptor(Encoding.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ScriptMetaDataBuilder metaData = (ScriptMetaDataBuilder) object;
                metaData.setScriptEncoding((ScriptEncoding) value);
            }

            public Object get(Object object) {
                return ((ScriptMetaDataBuilder) object).getScriptEncoding();
            }
        }, false);

        scriptMetaDataBuilder.addPropertyDescriptor(CHARACTER_SET, builder.getTypeDescriptor(String.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((ScriptMetaDataBuilder) object).setCharacterSet((String) value);
            }

            public Object get(Object object) {
                return ((ScriptMetaDataBuilder) object).getCharacterSet();
            }
        }, false);
    }

    /**
     * Constructs the bean descriptor for the text meta data.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildTextMetaData(ModelDescriptorBuilder builder) {
        BeanDescriptorBuilder textMetaDataBuilder = builder.getBeanBuilder(
                TextMetaDataBuilder.class, new ModelObjectFactory() {
            public Object createObject() {
                return POLICY_FACTORY.createTextMetaDataBuilder();
            }
        });

        textMetaDataBuilder.addPropertyDescriptor(ENCODING, builder.getTypeDescriptor(Encoding.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                TextMetaDataBuilder metaData = (TextMetaDataBuilder) object;
                metaData.setTextEncoding((TextEncoding) value);
            }

            public Object get(Object object) {
                return ((TextMetaDataBuilder) object).getTextEncoding();
            }
        }, false);
    }

    /**
     * Constructs the bean descriptor for the video meta data.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildVideoMetaData(ModelDescriptorBuilder builder) {
        BeanDescriptorBuilder videoMetaDataBuilder = builder.getBeanBuilder(
                VideoMetaDataBuilder.class, new ModelObjectFactory() {
            public Object createObject() {
                return POLICY_FACTORY.createVideoMetaDataBuilder();
            }
        });

        videoMetaDataBuilder.addPropertyDescriptor(ENCODING, builder.getTypeDescriptor(Encoding.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                VideoMetaDataBuilder metaData = (VideoMetaDataBuilder) object;
                metaData.setVideoEncoding((VideoEncoding) value);
            }

            public Object get(Object object) {
                return ((VideoMetaDataBuilder) object).getVideoEncoding();
            }
        }, false);

        videoMetaDataBuilder.addPropertyDescriptor(WIDTH, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                Integer intVal = (Integer) value;
                if (intVal != null) {
                    ((PixelDimensionsMetaDataBuilder) object).setWidth(intVal.intValue());
                } else {
                    ((PixelDimensionsMetaDataBuilder) object).setWidth(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((PixelDimensionsMetaDataBuilder) object).getWidth());
            }
        }, false);

        videoMetaDataBuilder.addPropertyDescriptor(HEIGHT, builder.getTypeDescriptor(Integer.TYPE), new PropertyAccessor() {
            public void set(Object object, Object value) {
                Integer intVal = (Integer) value;
                if (intVal != null) {
                    ((PixelDimensionsMetaDataBuilder) object).setHeight(intVal.intValue());
                } else {
                    ((PixelDimensionsMetaDataBuilder) object).setHeight(0);
                }
            }

            public Object get(Object object) {
                return new Integer(((PixelDimensionsMetaDataBuilder) object).getHeight());
            }
        }, false);
    }

    /**
     * Constructs the model for the base URL policy.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildBaseURLPolicy(ModelDescriptorBuilder builder) {
        builder.addOpaqueClassDescriptor(BaseLocation.class);

        BeanDescriptorBuilder baseURLBuilder = builder.getBeanBuilder(
                BaseURLPolicyBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return POLICY_FACTORY.createBaseURLPolicyBuilder();
                    }
                });

        baseURLBuilder.addPropertyDescriptor(BASE_URL, builder.getTypeDescriptor(String.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((BaseURLPolicyBuilder) object).setBaseURL((String) value);
            }

            public Object get(Object object) {
                return ((BaseURLPolicyBuilder) object).getBaseURL();
            }
        }, false);

        baseURLBuilder.addPropertyDescriptor(BASE_LOCATION, builder.getTypeDescriptor(BaseLocation.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((BaseURLPolicyBuilder) object).setBaseLocation((BaseLocation) value);
            }

            public Object get(Object object) {
                return ((BaseURLPolicyBuilder) object).getBaseLocation();
            }
        }, false);
    }

    /**
     * Constructs the model for the rollover image policy.
     *
     * @param builder The model descriptor builder to use
     */
    private static void buildRolloverImagePolicy(ModelDescriptorBuilder builder) {
        BeanDescriptorBuilder rolloverImageBuilder = builder.getBeanBuilder(
                RolloverImagePolicyBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return POLICY_FACTORY.createRolloverImagePolicyBuilder();
                    }
                });

        rolloverImageBuilder.addPropertyDescriptor(NORMAL_POLICY, builder.getTypeDescriptor(PolicyReference.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((RolloverImagePolicyBuilder) object).setNormalPolicy((PolicyReference) value);
            }

            public Object get(Object object) {
                return ((RolloverImagePolicyBuilder) object).getNormalPolicy();
            }
        }, false);

        rolloverImageBuilder.addPropertyDescriptor(OVER_POLICY, builder.getTypeDescriptor(PolicyReference.class), new PropertyAccessor() {
            public void set(Object object, Object value) {
                ((RolloverImagePolicyBuilder) object).setOverPolicy((PolicyReference) value);
            }

            public Object get(Object object) {
                return ((RolloverImagePolicyBuilder) object).getOverPolicy();
            }
        }, false);

        rolloverImageBuilder.addPropertyDescriptor(ALTERNATE_POLICIES,
                builder.getStandardListDescriptor(ArrayList.class,
                        builder.getClassDescriptor(PolicyReference.class)),
                new PropertyAccessor() {
                    public void set(Object object, Object value) {
                        throw new UnsupportedOperationException();
                    }

                    public Object get(Object object) {
                        return ((RolloverImagePolicyBuilder) object).getAlternatePolicies();
                    }
                }, true);
    }

    private static class DeviceTargetedDefinitionTypeHandler
            implements DefinitionTypeHandler {

        public void reportDuplicate(
            ValidationContext context, SourceLocation source, Path path,
            Object identifier) {

            context.addDiagnostic(source, path, DiagnosticLevel.ERROR,
                    context.createMessage("duplicate.device.name", identifier));
        }
    }

    private static class CategoryTargetedDefinitionTypeHandler
            implements DefinitionTypeHandler {

        public void reportDuplicate(
            ValidationContext context, SourceLocation source, Path path,
            Object identifier) {

            context.addDiagnostic(source, path, DiagnosticLevel.ERROR,
                    context.createMessage("duplicate.category.name",
                            identifier));
        }
    }

    private static class DefaultSelectionDefinitionTypeHandler
            implements DefinitionTypeHandler {

        public void reportDuplicate(
            ValidationContext context, SourceLocation source, Path path,
            Object identifier) {

            context.addDiagnostic(source, path, DiagnosticLevel.ERROR,
                    context.createMessage("multiple.default.selection",
                            identifier));
        }
    }
}
