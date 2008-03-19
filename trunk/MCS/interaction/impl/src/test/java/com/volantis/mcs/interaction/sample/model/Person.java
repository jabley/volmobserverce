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

package com.volantis.mcs.interaction.sample.model;

import com.volantis.mcs.model.jibx.JiBXSourceLocation;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 * A sample class representing a person.
 */
public class Person
        implements Validatable {

    public static final PropertyIdentifier FIRST_NAME = new PropertyIdentifier(
            Person.class, "firstName");

    public static final PropertyIdentifier LAST_NAME = new PropertyIdentifier(
            Person.class, "lastName");

    public static final PropertyIdentifier AGE = new PropertyIdentifier(
            Person.class, "age");

    public static final PropertyIdentifier ADDRESS = new PropertyIdentifier(
            Person.class, "address");

    private String firstName;

    private String lastName;

    private int age;

    private Address address;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void validate(ValidationContext context) {
        Step step;

        step = context.pushPropertyStep(FIRST_NAME);
        checkIsSet(context, firstName, FIRST_NAME);
        context.popStep(step);

        step = context.pushPropertyStep(LAST_NAME);
        checkIsSet(context, lastName, LAST_NAME);
        context.popStep(step);

        step = context.pushPropertyStep(AGE);
        if (age < 0 || age > 100000) {
            context.addDiagnostic(null,
                    DiagnosticLevel.ERROR, context.createMessage(
                            "out.of.range",
                            new Object[]{
                                new Integer(0),
                                new Integer(100000),
                                new Integer(age)}));
        }
        context.popStep(step);

        step = context.pushPropertyStep(ADDRESS);
        if (address == null) {
            propertyNotSet(context, ADDRESS);
        } else {
            address.validate(context);
        }
        context.popStep(step);
    }

    private void checkIsSet(
            ValidationContext context, final String value,
            final PropertyIdentifier property) {

        if (value == null || value.length() == 0) {
            propertyNotSet(context, property);
        }
    }

    private void propertyNotSet(
            ValidationContext context, final PropertyIdentifier property) {
        context.addDiagnostic(null, DiagnosticLevel.ERROR,
                context.createMessage("not.set",
                        property.getDescription()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 25-Oct-05	9961/4	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
