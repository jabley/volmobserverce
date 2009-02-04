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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.renderers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.protocols.widgets.styles.EffectDescriptor;
import com.volantis.mcs.protocols.widgets.styles.EffectParameters;
import com.volantis.mcs.protocols.widgets.styles.EffectRule;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.MCSEffectStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeDirectionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeSpeedKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeRepetitionKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.NestedStylesMock;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.StylesMock;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitionsMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class StylesExtractorTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();    
   
    public void testStyleListExtractor()  throws Exception {
        List expectedList = new ArrayList();
        
        expectedList.add(new EffectRule(
                new EffectDescriptor("none", null, 1.0, 1.0, null),
                "none",
                "none"));
        expectedList.add(new EffectRule(
                new EffectDescriptor("grow", "top-left", 2.0, 2.0, null),
                "none",
                "8"));        
        String randomParams = "wipe- top, wipe-top, slide-left, slide-right, random";
        EffectParameters params 
            = EffectParameters.getParser("random").parse(randomParams); 
        expectedList.add(new EffectRule(
                new EffectDescriptor("random", null, 3.0, 3.0, params),
                "16",
                "none"));
                
        List frameList = new ArrayList();
        frameList.add(STYLE_VALUE_FACTORY.getTime(null, 1.0, TimeUnit.S));
        frameList.add(STYLE_VALUE_FACTORY.getTime(null, 2.0, TimeUnit.S));
        frameList.add(STYLE_VALUE_FACTORY.getTime(null, 3.0, TimeUnit.S));
        StyleValue declaredEffectDuration = STYLE_VALUE_FACTORY.getList(frameList);
        
        List durationList = new ArrayList();
        durationList.add(STYLE_VALUE_FACTORY.getFrequency(null, 1.0, FrequencyUnit.HZ));
        durationList.add(STYLE_VALUE_FACTORY.getFrequency(null, 2.0, FrequencyUnit.HZ));
        durationList.add(STYLE_VALUE_FACTORY.getFrequency(null, 3.0, FrequencyUnit.HZ));
        StyleValue declaredFrameRate = STYLE_VALUE_FACTORY.getList(durationList);

        List effectList = new ArrayList();
        effectList.add(MCSEffectStyleKeywords.NONE);
        effectList.add(STYLE_VALUE_FACTORY.getString(null, "grow-top-left 8s"));
        effectList.add(STYLE_VALUE_FACTORY.getString(null, "random(" + randomParams + ") 16"));
        StyleValue declaredEffectStyle = STYLE_VALUE_FACTORY.getList(effectList);
                
        StylesMock styles = buildEffectStyles(
                declaredEffectStyle, declaredEffectDuration, declaredFrameRate);
        StylesExtractor extractor = new StylesExtractor(styles);
        List resultList = extractor.getEffectRules();
        compareList(expectedList, resultList);
    }

    public void testDefaultStyleListExtractor(){
        List expectedList = new ArrayList();
        
        expectedList.add(new EffectRule(
                new EffectDescriptor("none", null, 1.0, 10.0, null),
                "none",
                "none"));
        expectedList.add(new EffectRule(
                new EffectDescriptor("grow", "top-left", 1.0, 10.0, null),
                "none",
                "8"));
        expectedList.add(new EffectRule(
                new EffectDescriptor("grow", "center", 1.0, 10.0 , null),
                "10",
                "none"));
        
        List effectList = new ArrayList();
        effectList.add(MCSEffectStyleKeywords.NONE);
        effectList.add(STYLE_VALUE_FACTORY.getString(null, "grow-top-left 8s"));
        effectList.add(STYLE_VALUE_FACTORY.getString(null, "grow-center 10"));
        StyleValue declaredEffectStyle = STYLE_VALUE_FACTORY.getList(effectList);
        
        StylesMock styles = buildEffectStyles(
                declaredEffectStyle, null, null);
        StylesExtractor extractor = new StylesExtractor(styles);
        List resultList = extractor.getEffectRules();
        compareList(expectedList,resultList);        
    }

    
    public void testFillWithDefaultValues(){
        List expectedList = new ArrayList();
        
        expectedList.add(new EffectRule(
                new EffectDescriptor("none", null, 5.0, 15.0, null),
                "none",
                "none"));
        expectedList.add(new EffectRule(
                new EffectDescriptor("grow", "top-left", 5.0, 10.0, null),
                "none",
                "8"));
        expectedList.add(new EffectRule(
                new EffectDescriptor("grow", "center", 1.0, 10.0 , null),
                "10",
                "none"));
  
        List effectList = new ArrayList();
        effectList.add(MCSEffectStyleKeywords.NONE);
        effectList.add(STYLE_VALUE_FACTORY.getString(null, "grow-top-left 8s"));
        effectList.add(STYLE_VALUE_FACTORY.getString(null, "grow-center 10"));
        StyleValue declaredEffectStyle = STYLE_VALUE_FACTORY.getList(effectList);
        
        List durationList = new ArrayList();
        durationList.add(STYLE_VALUE_FACTORY.getTime(null, 5.0, TimeUnit.S));
        durationList.add(STYLE_VALUE_FACTORY.getTime(null, 5.0, TimeUnit.S));
        StyleValue declaredEffectDuration = STYLE_VALUE_FACTORY.getList(durationList);

        List frameList = new ArrayList();
        frameList.add(STYLE_VALUE_FACTORY.getFrequency(null, 15.0, FrequencyUnit.HZ));
        StyleValue declaredFrameRate = STYLE_VALUE_FACTORY.getList(frameList);
                
        StylesMock styles = buildEffectStyles(
                declaredEffectStyle, declaredEffectDuration, declaredFrameRate);
        StylesExtractor extractor = new StylesExtractor(styles);
        List resultList = extractor.getEffectRules();
        compareList(expectedList,resultList);
        
    }
    
    public void testDefaultEffect() throws Exception {        
        StylesMock styles = buildEffectStyles(null, null, null);
        StylesExtractor extractor = new StylesExtractor(styles);

        EffectRule rule = checkAndGetRule(extractor);
        assertEquals(createEffectRule(), rule);
    }
    
     
    public void testSimpleEffect() throws Exception {
        StylesMock styles = buildEffectStyles(MCSEffectStyleKeywords.PULSATE, null, null);
        StylesExtractor extractor = new StylesExtractor(styles);

        EffectRule rule = checkAndGetRule(extractor);
        assertEquals(createEffectRule("pulsate"), rule);
    }

   
    public void testEffectWithDirection() throws Exception {
        StylesMock styles = buildEffectStyles(MCSEffectStyleKeywords.GROW_CENTER, null, null);
        StylesExtractor extractor = new StylesExtractor(styles);

        EffectRule rule = checkAndGetRule(extractor);
        assertEquals(createEffectRule("grow", "center"), rule);
    }

  
    public void testEffectWithDoubleDirection() throws Exception {
        StylesMock styles = buildEffectStyles(MCSEffectStyleKeywords.GROW_TOP_LEFT, null, null);
        StylesExtractor extractor = new StylesExtractor(styles);

        EffectRule rule = checkAndGetRule(extractor);
        assertEquals(createEffectRule("grow", "top-left"), rule);
    }

    public void testFrameRate() throws Exception {
        StylesMock styles = buildStyles(StylePropertyDetails.MCS_FRAME_RATE,
            STYLE_VALUE_FACTORY.getFrequency(null, 15.0, FrequencyUnit.HZ));
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(15.0, extractor.getFrameRate(), 0.1);
    }

    public void testDefaultTransitionInterval() throws Exception {
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_TRANSITION_INTERVAL, null);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(1.0, extractor.getTransitionInterval(), 0.1);
    }

    public void testTransitionInterval() throws Exception {
        StylesMock styles = buildStyles(
            StylePropertyDetails.MCS_TRANSITION_INTERVAL,
            STYLE_VALUE_FACTORY.getTime(null, 3.0, TimeUnit.S));
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(3.0, extractor.getTransitionInterval(), 0.1);
    }

    public void testDefaultMarqueeStyle() throws Exception {
        StylesMock styles = buildStyles(StylePropertyDetails.MCS_MARQUEE_STYLE,
                null);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals("none", extractor.getMarqueeStyle());
    }

    public void testMarqueeStyle() throws Exception {
        StylesMock styles = buildStyles(StylePropertyDetails.MCS_MARQUEE_STYLE,
            MCSMarqueeStyleKeywords.SCROLL);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals("scroll", extractor.getMarqueeStyle());
    }

    public void testDefaultMarqueeDirection() throws Exception {
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_MARQUEE_DIRECTION, null);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals("left", extractor.getMarqueeDirection());
    }

    public void testMarqueeDirection() throws Exception {
        StylesMock styles = buildStyles(
            StylePropertyDetails.MCS_MARQUEE_DIRECTION,
            MCSMarqueeDirectionKeywords.RIGHT);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals("right", extractor.getMarqueeDirection());
    }

    public void testDefaultMarqueeSpeed() throws Exception {
        StylesMock styles = buildStyles(StylePropertyDetails.MCS_MARQUEE_SPEED,
                null);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(6.0, extractor.getMarqueeSpeed(), 0.1);
    }

    public void testNormalMarqueeSpeed() throws Exception {
        StylesMock styles = buildStyles(StylePropertyDetails.MCS_MARQUEE_SPEED,
            MCSMarqueeSpeedKeywords.NORMAL);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(6.0, extractor.getMarqueeSpeed(), 0.1);
    }

    public void testMarqueeSpeed() throws Exception {
        StylesMock styles = buildStyles(StylePropertyDetails.MCS_MARQUEE_SPEED,
            STYLE_VALUE_FACTORY.getLength(null, 12.0, LengthUnit.EM));
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(12.0, extractor.getMarqueeSpeed(), 0.1);
    }

    public void testDefaultMarqueeRepetitions() throws Exception {
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_MARQUEE_REPETITION, null);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(1, extractor.getMarqueeRepetitions());
    }

    public void testInfiniteMarqueeRepetitions() throws Exception {
        StylesMock styles = buildStyles(
            StylePropertyDetails.MCS_MARQUEE_REPETITION,
            MCSMarqueeRepetitionKeywords.INFINITE);
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(Integer.MAX_VALUE, extractor.getMarqueeRepetitions());
    }

    public void testMarqueeRepetitions() throws Exception {
        StylesMock styles = buildStyles(
            StylePropertyDetails.MCS_MARQUEE_REPETITION,
            STYLE_VALUE_FACTORY.getInteger(null, 10));
        StylesExtractor extractor = new StylesExtractor(styles);
        assertEquals(10, extractor.getMarqueeRepetitions());
    }

    public void testBaseStylesWithoutBaseWithoutNestedValue() throws Exception {
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE, null, 
                StatefulPseudoClasses.MCS_CONCEALED, null);
        StylesExtractor extractor = new StylesExtractor(styles);        

        assertEquals("none", extractor.getEffectStyle());
    }
   
    public void testBaseStylesWithoutBaseWithNestedValue() throws Exception {
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE,  null, 
                StatefulPseudoClasses.MCS_CONCEALED, MCSEffectStyleKeywords.PULSATE);
        StylesExtractor extractor = new StylesExtractor(styles);
        // Nested styles should not propagate to base styles
        assertEquals("none", extractor.getEffectStyle());
    }

    public void testBaseStylesWithBaseWithoutNestedValue() throws Exception {
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE, MCSEffectStyleKeywords.PULSATE,
                StatefulPseudoClasses.MCS_CONCEALED, null);
        StylesExtractor extractor = new StylesExtractor(styles);

        assertEquals("pulsate", extractor.getEffectStyle());
    }

    public void testBaseStylesWithBaseWithNestedValue() throws Exception {
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE, MCSEffectStyleKeywords.PULSATE,
                StatefulPseudoClasses.MCS_CONCEALED, MCSEffectStyleKeywords.PUFF);
        StylesExtractor extractor = new StylesExtractor(styles);

        assertEquals("pulsate", extractor.getEffectStyle());
    }

    public void testNestedStylesWithoutBaseWithoutNestedValue()
            throws Exception {
        StatefulPseudoClass pseudoClass = StatefulPseudoClasses.MCS_CONCEALED;
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE, null, 
                pseudoClass, null);
        StylesExtractor extractor = new StylesExtractor(styles, pseudoClass);

        assertEquals("none", extractor.getEffectStyle());
    }

    public void testNestedStylesWithoutBaseWithNestedValue() throws Exception {
        StatefulPseudoClass pseudoClass = StatefulPseudoClasses.MCS_CONCEALED;
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE, null, 
                pseudoClass, MCSEffectStyleKeywords.PULSATE);
        StylesExtractor extractor = new StylesExtractor(styles, pseudoClass);

        assertEquals("pulsate", extractor.getEffectStyle());
    }

    public void testNestedStylesWithBaseWithoutNestedValue() throws Exception {
        StatefulPseudoClass pseudoClass = StatefulPseudoClasses.MCS_CONCEALED;
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE, MCSEffectStyleKeywords.PULSATE, 
                pseudoClass, null);
        StylesExtractor extractor = new StylesExtractor(styles, pseudoClass);
        // base styles should propagate to nested styles
        assertEquals("pulsate", extractor.getEffectStyle());
    }

    public void testNestedStylesWithBaseWithNestedValue() throws Exception {
        StatefulPseudoClass pseudoClass = StatefulPseudoClasses.MCS_CONCEALED;
        StylesMock styles = buildStyles(
                StylePropertyDetails.MCS_EFFECT_STYLE, MCSEffectStyleKeywords.PUFF, 
                pseudoClass, MCSEffectStyleKeywords.PULSATE);
        StylesExtractor extractor = new StylesExtractor(styles, pseudoClass);

        assertEquals("pulsate", extractor.getEffectStyle());
    }

    public void testJavaScriptStyles() throws Exception {
        StylePropertyDefinitionsMock defs = new StylePropertyDefinitionsMock(
                "style property definitions", expectations);

        defs.expects.count().returns(5).any();

        defs.expects.getStyleProperty(0).returns(
                StylePropertyDetails.BACKGROUND_COLOR).fixed(1);
        defs.expects.getStyleProperty(1).returns(
                StylePropertyDetails.BACKGROUND_REPEAT).fixed(1);
        defs.expects.getStyleProperty(2).returns(
                StylePropertyDetails.MCS_EFFECT_STYLE).fixed(1);
        defs.expects.getStyleProperty(3).returns(
                StylePropertyDetails.BACKGROUND_IMAGE).fixed(1);
        defs.expects.getStyleProperty(4).returns(
                StylePropertyDetails.DISPLAY).fixed(1);

        MutablePropertyValuesMock values = new MutablePropertyValuesMock(
                "property values", expectations);

        values.expects.getSpecifiedValue(StylePropertyDetails.BACKGROUND_COLOR)
                .returns(StyleColorNames.BLUE).fixed(1);
        values.expects.getSpecifiedValue(StylePropertyDetails.BACKGROUND_REPEAT)
                .returns(null).fixed(1);
        values.expects.getSpecifiedValue(StylePropertyDetails.BACKGROUND_IMAGE)
                .returns(null).fixed(1);
        values.expects.getSpecifiedValue(StylePropertyDetails.DISPLAY).returns(
                DisplayKeywords.BLOCK).fixed(1);

        values.expects.getStylePropertyDefinitions().returns(defs).any();

        StylesMock styles = new StylesMock("styles", expectations);

        styles.expects.getPropertyValues().returns(values).any();

        StylesExtractor extractor = new StylesExtractor(styles);

        assertEquals("{'background-color': 'blue', 'display': 'block'}", extractor
                .getJavaScriptStyles());
    }
    
    private StylesMock buildEffectStyles(StyleValue effectStyle, StyleValue effectDuartion, StyleValue frameRate) {
        List propertyList = new ArrayList();
        propertyList.add(StylePropertyDetails.MCS_EFFECT_STYLE);
        propertyList.add(StylePropertyDetails.MCS_EFFECT_DURATION);
        propertyList.add(StylePropertyDetails.MCS_FRAME_RATE);

        List valuesList = new ArrayList();
        valuesList.add(effectStyle);
        valuesList.add(effectDuartion);
        valuesList.add(frameRate);
        
        return buildMultipleStyles(propertyList, valuesList);
    }

    private StylesMock buildStyles(StyleProperty property, StyleValue value) {
        StylesMock styles = new StylesMock("styles", expectations);

        addPropertyValueExpectation(styles, property, value);

        return styles;
    }

    private StylesMock buildStyles(StyleProperty property,
            StyleValue baseValue, StatefulPseudoClass nestedClass,
            StyleValue nestedValue) {
        StylesMock styles = buildStyles(property, baseValue);

        if (nestedClass != null) {
            NestedStyles nestedStyles = buildNestedStyles(property, nestedValue);

            styles.expects.findNestedStyles(nestedClass).returns(nestedStyles)
                    .any();
        }

        return styles;
    }
    
    private NestedStylesMock buildNestedStyles(StyleProperty property, StyleValue value) {
        NestedStylesMock styles = new NestedStylesMock("styles", expectations);

        addPropertyValueExpectation(styles, property, value);

        return styles;
    }

    private void addPropertyValueExpectation(
            StylesMock styles, StyleProperty property, StyleValue value) {
        MutablePropertyValuesMock values = new MutablePropertyValuesMock(
                "values", expectations);

        values.expects.getComputedValue(property).returns(value).any();
        
        styles.expects.getPropertyValues().returns(values).any();
    }

    private StylesMock buildMultipleStyles(List stylePropertiesList , List styleValuesList ) {
        MutablePropertyValuesMock values = new MutablePropertyValuesMock(
                "values", expectations);

        Iterator styleProperties = stylePropertiesList.iterator();
        Iterator styleValues = styleValuesList.iterator();
        while(styleProperties.hasNext()){
            values.expects.getComputedValue((StyleProperty)styleProperties.next())
                .returns(styleValues.next()).any();
        }
        
        StylesMock styles = new StylesMock("styles", expectations);
        styles.expects.getPropertyValues().returns(values).any();
        return styles;
    }
 
    private void compareList(List expectedList, List resultList){
        assertEquals(expectedList.size(), resultList.size());
        Iterator expectedIterator = expectedList.iterator();
        Iterator resultIterator = resultList.iterator();
        while(expectedIterator.hasNext()){
            assertEquals(expectedIterator.next(), resultIterator.next());
        }
    }

    private EffectRule checkAndGetRule(StylesExtractor extractor) {        
        List effects = extractor.getEffectRules();
        assertNotNull(effects);
        assertEquals(effects.size(), 1);
        assertTrue(effects.get(0) instanceof EffectRule);
        return (EffectRule)effects.get(0);         
    }

    private EffectRule createEffectRule(String name, String direction,  double duration, double fps) {
        return new EffectRule(
            new EffectDescriptor(name, direction, duration, fps, null),
            "none",
            "none");
    }

    private EffectRule createEffectRule(String name, String direction, double duration) {
        return createEffectRule(name, direction, duration, 10.0);
    }

    private EffectRule createEffectRule(String name, String direction) {
        return createEffectRule(name, direction, 1.0);
    }

    private EffectRule createEffectRule(String name) {
        return createEffectRule(name, null);
    }

    private EffectRule createEffectRule() {
        return createEffectRule("none");
    }
}
