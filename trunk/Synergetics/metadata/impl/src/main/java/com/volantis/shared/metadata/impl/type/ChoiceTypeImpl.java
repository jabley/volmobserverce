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

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.ImmutableGeneratingTypedSet;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.type.ChoiceDefinition;
import com.volantis.shared.metadata.type.ChoiceType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.value.ChoiceValue;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.ChoiceType}.
 */
abstract class ChoiceTypeImpl
        extends MetaDataTypeImpl
        implements ChoiceType {

    /**
     * The contents of the set, this must never be exposed outside the object
     * without being wrapped.
     */
    private HashSet choiceDefinitions = new HashSet();

    /**
     * A mutable wrapper around the contents.
     *
     * <p>This does two additional things that a normal set does not do.
     * It checks to make sure that only instances of allowable types are
     * added and throws an exception otherwise. It also makes sure that only
     * immutable instances are added, creating one if necessary.</p>
     *
     * <p>This is marked as transient because it is not a fundamental part of
     * the object's state and does not need to be persisted.</p>
     */
    private transient Set mutableChoiceDefinitions;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected ChoiceTypeImpl(ChoiceType value) {
        this(new HashSet(value.getChoiceDefinitions()));
    }

    /**
     * Protected constructor for sub classes.
     */
    protected ChoiceTypeImpl() {
        this(new HashSet());
    }

    /**
     * Private constructor to perform initialisation common to all
     * constructors.
     */
    private ChoiceTypeImpl(HashSet contents) {
        this.choiceDefinitions = contents;
    }

    /**
     * Adds a new choice definition to the Choice.
     *
     * @note Added for JiBX only.
     *
     * @param choiceDefinition the choice definition to add
     */
    public void addChoiceDefinition(final ChoiceDefinition choiceDefinition) {
        choiceDefinitions.add(choiceDefinition);
    }

    /**
     * Returns the choice definitions of this Choice type.
     *
     * @note Added for JiBX only.
     *
     * @return an iterator over the current choice definitions
     */
    public Iterator getChoiceDefinitionsIterator() {
        return getChoiceDefinitions().iterator();
    }

    // Javadoc inherited.
    public Set getChoiceDefinitions() {
        return Collections.unmodifiableSet(getInternalChoiceDefinitions());
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableChoiceTypeImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableChoiceTypeImpl(this);
    }

    /**
     * Get a mutable set of
     * {@link com.volantis.shared.metadata.type.immutable.ImmutableChoiceDefinition}
     * instances.
     *
     * <p>Modifications made to the returned set will change the state of this
     * object. The objects contained within the set are not themselves
     * mutable so if their state needs to be changed then a mutable copy will
     * need to be obtained, changed and then used to replace the original
     * object.</p>
     *
     * <p>The returned set only stores immutable instances of
     * {@link com.volantis.shared.metadata.type.ChoiceDefinition}. If a user attempts to add a mutable instance
     * to it then the set will obtain an immutable instance and store that
     * instead.</p>
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @return a mutable set of
     * {@link com.volantis.shared.metadata.type.immutable.ImmutableChoiceDefinition}
     * instances.
     */
    public Set getMutableChoiceDefinitions() {

        // Create this lazily as it will never be needed for immutable
        // objects and it does not need to be synchronized as mutable objects
        // are not thread safe.
        if (mutableChoiceDefinitions == null) {
            mutableChoiceDefinitions = new ImmutableGeneratingTypedSet(
                getInternalChoiceDefinitions(), ChoiceDefinition.class);
        }
        return mutableChoiceDefinitions;
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(getInternalChoiceDefinitions());
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof ChoiceTypeImpl) &&
            equalsChoiceType((ChoiceTypeImpl) other);
    }

    /**
     * Private access method
     */
    private HashSet getInternalChoiceDefinitions() {
        return choiceDefinitions;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>ChoiceType</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>ChoiceType</code> are equal to this one.
     */
    protected boolean equalsChoiceType(ChoiceTypeImpl other) {
        return MetaDataHelper.equals(getInternalChoiceDefinitions(), other.getInternalChoiceDefinitions());
    }

    // javadoc inherited
    protected Class getExpectedValueType() {
        return ChoiceValue.class;
    }

    // Javadoc inherited.
    protected Collection verify(final MetaDataValue value, final String path) {
        // Perform standard verification, which includes value type checking.
        final Collection errors = super.verify(value, path);

        if (value instanceof ChoiceValue) {
            // Perform choice-value-specific verification. These are the rules:
            // * Choice name may be null only and only if set of choice 
            //   definitions is empty.
            // * Choice name must be equal to the name of one of the choice
            //   definitions.
            // * Choice value must pass verification of its choice definition 
            //   type.
            final ChoiceValue choiceValue = (ChoiceValue) value;
            
            final String choiceName = choiceValue.getChoiceName();
            
            if (choiceName == null) {
                if (!choiceDefinitions.isEmpty()) {
                    errors.add(new VerificationError(
                            VerificationError.CHOICE_NAME_NULL,
                            path,
                            value,
                            null,
                            "Null choice name."));
                }
            } else {
                final ChoiceDefinition choiceDefinition = 
                    getChoiceDefinition(choiceName);
                
                if (choiceDefinition == null) {
                    // No choice definition for selected choice name is an error.
                    errors.add(new VerificationError(
                            VerificationError.TYPE_UNEXPECTED_VALUE,
                            path,
                            value,
                            null,
                            "Choice '" + choiceName + "' is not expected here."));
                } else {
                    // Verify choice value against its type.
                    Collection valueErrors = 
                        choiceDefinition.getType().verify(choiceValue.getValue());
                    
                    errors.addAll(valueErrors);
                }
            }
        }
        
        return errors;
    }
    
    /**
     * Returns choice definition for given name, or null of not found.
     * 
     * @param name The name of the choice.
     * @return The choice definition.
     */
    private ChoiceDefinition getChoiceDefinition(String name) {
        ChoiceDefinition choiceDefinition = null;
        
        Iterator iterator = choiceDefinitions.iterator();
        
        while (iterator.hasNext()) {
            ChoiceDefinition choiceDefinitionCandidate = 
                (ChoiceDefinition) iterator.next();
            
            if (choiceDefinitionCandidate.getName().equals(name)) {
                choiceDefinition = choiceDefinitionCandidate;
            }
        }
                
        return choiceDefinition;
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // fetch myself
        EntryDAO mySelf = visitor.getNextEntry();
        collectionInhibitorInitializerHelper(visitor, choiceDefinitions);
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        // push myself
        visitor.push(getClassMapper().toString(), getClassMapper());
        collectionInhibitorVisitorHelper(visitor, choiceDefinitions);
        visitor.pop();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05  6686/1  pduffin VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05  6560/9  tom VBM:2004122401 Changed Javadoc

 14-Jan-05  6560/7  tom VBM:2004122401 Added Inhibitor base class

 14-Jan-05  6560/5  tom VBM:2004122401 Commit prior to creating Inhibitor baseclass

 13-Jan-05  6560/1  tom VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
