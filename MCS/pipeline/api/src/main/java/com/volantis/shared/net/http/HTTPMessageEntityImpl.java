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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.http;

import com.volantis.shared.net.http.HTTPMessageEntity;

/**
 * An implementation of HTTPMessageEntity.
 */
public abstract class HTTPMessageEntityImpl
        implements HTTPMessageEntity {

    /**
     * The name of this HTTPMessageEntity.
     */
    private String name;

    /**
     * The value of this HTTPMessageEntity.
     */
    private String value;

    /**
     * Protect the constructor to help prevent invalid construction. Note
     * that this constructor relies on specializations to call setName().
     */
    protected HTTPMessageEntityImpl() {
    }

    /**
     * Protect the constructor to help prevent invalid construction. This
     * constructor sets the name.
     * @param name The name of this HTTPMessageEntity.
     * @throws java.lang.IllegalArgumentException if name is null or an empty String.
     */
    protected HTTPMessageEntityImpl(String name) {
        setName(name);
    }

    // javadoc inherited
    public String getName() {
        return name;
    }

    /**
     * Allow sub-classes to set the name of this HTTPMessageEntityImpl.
     * @param name The name.
     * @throws java.lang.IllegalArgumentException if name is null or an empty String.
     */
    protected void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name cannot be 0 length.");
        }
        this.name = name;
    }

    // javadoc inherited
    public String getValue() {
        return value;
    }

    // javadoc inherited
    public void setValue(String value) {
        this.value = value;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean equals = o != null && o instanceof HTTPMessageEntityImpl;
        if (equals) {
            HTTPMessageEntity entity = (HTTPMessageEntity)o;
            equals = getClass().equals(entity.getClass()) &&
                    this.getIdentity().identityEquals(entity.getIdentity());
            if (equals) {
                equals = getValue() == null ? entity.getValue() == null :
                        getValue().equals(entity.getValue());
            }
        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        return getClass().hashCode() + getIdentity().hashCode() +
                (getValue() == null ? 0 : getValue().hashCode());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	217/10	allan	VBM:2003071702 Rename and re-write HttpCookie

 31-Jul-03	217/8	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 28-Jul-03	217/6	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
