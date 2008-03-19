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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/PropertyRenderer.java,v 1.4 2002/05/24 10:28:44 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Allan           VBM:2002042404 - Created. Abstract base class 
 *                              for css property renders. 
 * 15-May-02    Allan           VBM:2002042404 - Added renderValue().
 * 22-May-02    Doug            VBM:2002051701 - Modified the abstract method -
 *                              getKeywordMapper() to take a RendererContext
 *                              argument. Modified the renderValue() method
 *                              so that a RendererContext argument is passed 
 *                              to the getKeywordMapper() method call.
 * 24-May-02    Doug            VBM:2002051701 - Removed the KeywordMapper
 *                              property.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.mappers.CSSPropertyNameMapper;
import com.volantis.styling.properties.StyleProperty;

import java.io.IOException;
import java.io.Writer;

/**
 * The base class of the classes which are responsible for rendering a property
 * or set of properties. Simple property renderer only need to implement the
 * abstract methods of this class. More complex renderers should not support
 * the abstract methods (except getKeywordMapper() where required) and should
 * instead override render().
 */
public abstract class PropertyRenderer {

  /**
   * Render the value part of the property.
   * @param value the StyleValue to render
   * @param context the RendererContext within which to render the value
   * @throws IOException if there is a problem writing to the writer
   */
  public void renderValue(StyleValue value, RendererContext context)
    throws IOException {

    context.setKeywordMapper(getKeywordMapper(context));
    context.renderValue(value);
  }

  /**
   * Get the string that represents the name of the property rendered by
   * this renderer.
   * @return the name of the property rendered by this renderer
   */
  public abstract String getName();

    /**
     * Get the string that represents the external representation of
     * the name of the property rendered by this renderer. May be the
     * same as returned by {@link #getName}.
     *
     * @return String external representation of the name of the property
     * rendered by this renderer.
     */
    public String getExternalName() {
        return CSSPropertyNameMapper.getDefaultInstance().
                getExternalPropertyName(getName());
    }

  /**
   * Get the KeywordMapper used by this renderer.
   * @param context the RendererContext for which we are rendering.
   * @return the KeywordMapper for this renderer
   */
  public abstract KeywordMapper getKeywordMapper(RendererContext context);
  
  /**
   * Get the StyleValue to render.
   * @return the StyleValue to render.
   */
  public abstract StyleValue getValue(StyleProperties properties);

    /**
     * Get the {@link PropertyValue} to render.
     * @param properties The properties that contain the values.
     * @return the {@link PropertyValue} to render.
     */
    public abstract PropertyValue getPropertyValue(StyleProperties properties);

  /**
   * Render a property. Sub-classes should overide this method for more
   * specific rendering requirements.
   *
   * @param properties the StyleProperties containing the background properties
   *                   if any.
   * @param context    the RendererContext in which to render the background.
   * @throws IOException if the is a problem writing the rendered property.
   */
  public void render(StyleProperties properties, RendererContext context)
          throws IOException {

      String name = getExternalName();
      if (name == null) {
          return;
      }

      PropertyValue propertyValue = getPropertyValue(properties);
      if (propertyValue == null) {
          return;
      }

      StyleValue value = propertyValue.getValue();

      Writer writer = context.getWriter();
      writer.write(name);
      writer.write(':');
      renderValue(value, context);
      context.renderPriority(propertyValue.getPriority());
      writer.write(';');
  }

    /**
     * Get a group of values.
     *
     * @return a group of values for the specified properties.
     */
    protected StyleValue[] getValues(StyleProperty[] group,
                                     StyleProperties properties) {
        StyleValue values[] = new StyleValue[group.length];
        for (int i = 0; i < group.length; i++) {
            StyleProperty property = group[i];
            values[i] = properties.getStyleValue(property);
        }

        return values;
    }

    /**
     * Get a group of values.
     *
     * @return a group of values for the specified properties.
     */
    protected PropertyValue[] getPropertyValues(StyleProperty[] group,
                                     StyleProperties properties) {
        PropertyValue values[] = new PropertyValue[group.length];
        for (int i = 0; i < group.length; i++) {
            StyleProperty property = group[i];
            values[i] = properties.getPropertyValue(property);
        }

        return values;
    }

    protected StyleValue [] getStyleValues(
            StyleProperty[] group, StyleProperties properties) {

        StyleValue values[] = new StyleValue[group.length];
        for (int i = 0; i < group.length; i++) {
            StyleProperty property = group[i];
            StyleValue propertyValue = properties.getStyleValue(property);
            values[i] = propertyValue;
        }
        return values;
    }

    protected StyleValue [] getStyleValues(PropertyValue[] propertyValues) {
        StyleValue values[] = new StyleValue[propertyValues.length];
        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            if (propertyValue != null) {
                values[i] = propertyValue.getValue();
            }
        }
        return values;
    }

    protected Integer identityHashCode(StyleValue value) {
        return new Integer(System.identityHashCode(value));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
