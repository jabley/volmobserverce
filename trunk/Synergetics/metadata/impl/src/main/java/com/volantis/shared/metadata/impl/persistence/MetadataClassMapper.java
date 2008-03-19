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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.impl.persistence;

import com.volantis.shared.inhibitor.Inhibitor;
import com.volantis.shared.metadata.impl.DefaultMetaDataFactory;
import com.volantis.shared.metadata.impl.type.DefaultMetaDataTypeFactory;
import com.volantis.shared.metadata.impl.type.constraint.DefaultConstraintFactory;
import com.volantis.shared.security.impl.acl.ACLFactoryImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Type safe enumeration.
 *
 * A class that allows construction off types based on their name
 *
 */
public class MetadataClassMapper {

    /**
     * Map the names of the created enums to thier instances. Note that the
     * position of the map is important. It must be initialized before the enum
     * instances are created
     */
    private static final Map enums = new HashMap();

    private static final DefaultMetaDataFactory FACTORY =
        new DefaultMetaDataFactory();

    /**
     * An unsupported persistence entity
     */
    public static final MetadataClassMapper UNSUPPORTED =
        new MetadataClassMapper(
            "UNS",
            new FactoryBinding() {
                public Inhibitor create() {
                   throw new PersistenceException("persistence-not-supported",
                                                  "UNS");
                }
            });

    /**
     * An persistence entry that holds either a NULL inhibitor a NULL
     * serialized object or a custom Object persisted as a string
     */
    public static final MetadataClassMapper NULL =
        new MetadataClassMapper(
            "NUL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return null;
                }
            });

    /**
     * An persistence entry that holds
     */
    public static final MetadataClassMapper SERIALIZED =
        new MetadataClassMapper(
            "SER",
            new FactoryBinding() {
                public Inhibitor create() {
                    throw new PersistenceException("persistence-not-supported",
                                                   "SER");
                }
            });
       
    /**
     * A IMMUTABLE_NUMBER_VALUE instance
     */
    public static final MetadataClassMapper IMMUTABLE_NUMBER_VALUE =
        new MetadataClassMapper(
            "IVN",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createNumberValue().createImmutable();
                }
            });

    /**
     * A MUTABLE_NUMBER_VALUE instance
     */
    public static final MetadataClassMapper MUTABLE_NUMBER_VALUE =
        new MetadataClassMapper(
            "MVN",
            new FactoryBinding(){
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createNumberValue();
                }
            });

    /**
     * A IMMUTABLE_STRING_VALUE instance
     */
    public static final MetadataClassMapper IMMUTABLE_STRING_VALUE =
        new MetadataClassMapper(
            "IVS",
            new FactoryBinding(){
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createStringValue().createImmutable();
                }
            });

    /**
     * A MUTABLE_STRING_VALUE instance
     */
    public static final MetadataClassMapper MUTABLE_STRING_VALUE =
        new MetadataClassMapper(
            "MVS",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createStringValue();
                }
            });

    /**
     * A IMMUTABLE_BOOLEAN_VALUE instance
     */
    public static final MetadataClassMapper IMMUTABLE_BOOLEAN_VALUE =
        new MetadataClassMapper(
            "IVB",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createBooleanValue()
                        .createImmutable();
                }
            });
    /**
     * A MUTABLE_BOOLEAN_VALUE instance
     */
    public static final MetadataClassMapper MUTABLE_BOOLEAN_VALUE =
        new MetadataClassMapper(
            "MVB",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createBooleanValue();
                }
            });

    /**
     * A IMMUTABLE_UNIT_VALUE instance (There is no mutable version of this
     * class)
     */
    public static final MetadataClassMapper IMMUTABLE_UNIT_VALUE =
        new MetadataClassMapper(
            "IVU",
            new FactoryBinding() {
                public Inhibitor create() {
                    throw new PersistenceException(
                        "persistence-not-supported", "IVU");
                }
            });

    /**
     * A MUTABLE_QUANTITY_VALUE instance
     */
    public static final MetadataClassMapper MUTABLE_QUANTITY_VALUE =
        new MetadataClassMapper(
            "MVQ",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createQuantityValue();
                }
            });

    /**
     * A IMMUTABLE_QUANTITY_VALUE instance
     */
    public static final MetadataClassMapper IMMUTABLE_QUANTITY_VALUE =
        new MetadataClassMapper(
            "IVQ",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createQuantityValue().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_LIST_VALUE instance
     */
    public static final MetadataClassMapper MUTABLE_LIST_VALUE =
        new MetadataClassMapper(
            "MVL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createListValue();
                }
            });

    /**
     * A IMMUTABLE_LIST_VALUE instance
     */
    public static final MetadataClassMapper IMMUTABLE_LIST_VALUE =
        new MetadataClassMapper(
            "IVL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createListValue().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_SET_VALUE instance (S is used by String so we use P)
     */
    public static final MetadataClassMapper MUTABLE_SET_VALUE =
        new MetadataClassMapper(
            "MVP",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createSetValue();
                }
            });

    /**
     * A IMMUTABLE_SET_VALUE instance (S is used by String so we use P)
     */
    public static final MetadataClassMapper IMMUTABLE_SET_VALUE =
        new MetadataClassMapper(
            "IVP",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createSetValue().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_STRUCTURE_VALUE instance (S is used by String so we use T)
     */
    public static final MetadataClassMapper MUTABLE_STRUCTURE_VALUE =
        new MetadataClassMapper(
            "MVT",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createStructureValue();
                }
            });

    /**
     * A IMMUTABLE_STRUCTURE_VALUE instance (S is used by String so we use T)
     */
    public static final MetadataClassMapper IMMUTABLE_STRUCTURE_VALUE =
        new MetadataClassMapper(
            "IVT",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createStructureValue().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_CHOICE_VALUE instance
     */
    public static final MetadataClassMapper MUTABLE_CHOICE_VALUE =
        new MetadataClassMapper(
            "MVC",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createChoiceValue();
                }
            });

    /**
     * A IMMUTABLE_CHOICE_VALUE instance
     */
    public static final MetadataClassMapper IMMUTABLE_CHOICE_VALUE =
        new MetadataClassMapper(
            "IVC",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getValueFactory().createChoiceValue().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_UNIQUE_MEMBER_CONSTRAINT instance
     */
    public static final MetadataClassMapper MUTABLE_UNIQUE_MEMBER_CONSTRAINT =
        new MetadataClassMapper(
            "MCM",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().getUniqueMemberConstraint();
                }
            });

    /**
     * A IMMUTABLE_UNIQUE_MEMEBER_CONSTRAINT instance
     */
    public static final MetadataClassMapper IMMUTABLE_UNIQUE_MEMEBER_CONSTRAINT =
        new MetadataClassMapper(
            "ICM",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().getUniqueMemberConstraint().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_MINIMUM_VALUE_CONSTRAINT instance
     */
    public static final MetadataClassMapper MUTABLE_MINIMUM_VALUE_CONSTRAINT =
        new MetadataClassMapper(
            "MCMV",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMinimumValueConstraint();
                }
            });

    /**
     * A IMMUTABLE_MINIMUM_VALUE_CONSTRAINT instance
     */
    public static final MetadataClassMapper IMMUTABLE_MINIMUM_VALUE_CONSTRAINT =
        new MetadataClassMapper(
            "ICMV",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMinimumValueConstraint().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_MAXIMUM_VALUE_CONSTRAINT instance
     */
    public static final MetadataClassMapper MUTABLE_MAXIMUM_VALUE_CONSTRAINT =
        new MetadataClassMapper(
            "MCIV",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMaximumValueConstraint();
                }
            });

    /**
     * A IMMUTABLE_MAXIMUM_VALUE_CONSTRAINT instance
     */
    public static final MetadataClassMapper IMMUTABLE_MAXIMUM_VALUE_CONSTRAINT =
        new MetadataClassMapper(
            "ICIV",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMaximumValueConstraint().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_MINIMUM_LENGTH_CONSTRAINT instance
     */
    public static final MetadataClassMapper MUTABLE_MINIMUM_LENGTH_CONSTRAINT =
        new MetadataClassMapper(
            "MCIL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMinimumLengthConstraint();
                }
            });

    /**
     * A IMMUTABLE_MINIMUM_LENGTH_CONSTRAINT instance
     */
    public static final MetadataClassMapper IMMUTABLE_MINIMUM_LENGTH_CONSTRAINT =
        new MetadataClassMapper(
            "ICIL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMinimumLengthConstraint().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_MAXIMUM_LENGTH_CONSTRAINT instance
     */
    public static final MetadataClassMapper MUTABLE_MAXIMUM_LENGTH_CONSTRAINT =
        new MetadataClassMapper(
            "MCML",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMaximumLengthConstraint();
                }
            });

    /**
     * A IMMUTABLE_MAXIMUM_LENGTH_CONSTRAINT instance
     */
    public static final MetadataClassMapper IMMUTABLE_MAXIMUM_LENGTH_CONSTRAINT =
        new MetadataClassMapper(
            "ICML",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMaximumLengthConstraint().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_ENUMERATED_CONSTRAINT instance
     */
    public static final MetadataClassMapper MUTABLE_ENUMERATED_CONSTRAINT =
        new MetadataClassMapper(
            "MCE",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createEnumeratedConstraint();
                }
            });

    /**
     * A IMMUTABLE_ENUMERATED_CONSTRAINT instance
     */
    public static final MetadataClassMapper IMMUTABLE_ENUMERATED_CONSTRAINT =
        new MetadataClassMapper(
            "ICE",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createEnumeratedConstraint().
                        createImmutable();
                }
            });

    /**
     * A MUTABLE_NUMBER_SUBTYPE instance
     */
    public static final MetadataClassMapper MUTABLE_NUMBER_SUBTYPE =
        new MetadataClassMapper(
            "MCNS",
            new FactoryBinding() {
                public Inhibitor create() {
                    DefaultConstraintFactory dcf =
                        new DefaultConstraintFactory();
                    return dcf.getUninitializedNumberSubtypeConstraint().createMutable();
                }
            });

    /**
     * A IMMUTABLE_NUMBER_SUBTYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_NUMBER_SUBTYPE =
        new MetadataClassMapper(
            "ICNS",
            new FactoryBinding() {
                public Inhibitor create() {
                    DefaultConstraintFactory dcf =
                        new DefaultConstraintFactory();
                    return dcf.getUninitializedNumberSubtypeConstraint();
                }
            });

    /**
     * A MUTABLE_MEMBERTYPE_CONSTRAINT instance
     */
    public static final MetadataClassMapper MUTABLE_MEMBERTYPE_CONSTRAINT =
        new MetadataClassMapper(
            "MCMT",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMemberTypeConstraint();
                }
            });

    /**
     * A IMMUTABLE_MEMBERTYPE_CONSTRAINT instance
     */
    public static final MetadataClassMapper IMMUTABLE_MEMBERTYPE_CONSTRAINT =
        new MetadataClassMapper(
            "ICMT",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().
                        getConstraintFactory().createMemberTypeConstraint()
                        .createImmutable();
                }
            });


    /**
     * A MUTABLE_UNIT_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_UNIT_TYPE =
        new MetadataClassMapper(
            "MTU",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createUnitType();
                }
            });

    /**
     * A IMMUTABLE_UNIT_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_UNIT_TYPE =
        new MetadataClassMapper(
            "ITU",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createUnitType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_STRING_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_STRING_TYPE =
        new MetadataClassMapper(
            "MTS",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createStringType();
                }
            });

    /**
     * A IMMUTABLE_STRING_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_STRING_TYPE =
        new MetadataClassMapper(
            "ITS",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createStringType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_BOOLEAN_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_BOOLEAN_TYPE =
        new MetadataClassMapper(
            "MTB",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createBooleanType();
                }
            });

    /**
     * A IMMUTABLE_BOOLEAN_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_BOOLEAN_TYPE =
        new MetadataClassMapper(
            "ITB",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createBooleanType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_NUMBER_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_NUMBER_TYPE =
        new MetadataClassMapper(
            "MTN",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createNumberType();
                }
            });

    /**
     * A IMMUTABLE_NUMBER_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_NUMBER_TYPE =
        new MetadataClassMapper(
            "ITN",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createNumberType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_SET_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_SET_TYPE =
        new MetadataClassMapper(
            "MTP",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createSetType();
                }
            });

    /**
     * A IMMUTABLE_SET_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_SET_TYPE =
        new MetadataClassMapper(
            "ITP",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createSetType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_LIST_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_LIST_TYPE =
        new MetadataClassMapper(
            "MTL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createListType();
                }
            });

    /**
     * A IMMUTABLE_LIST_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_LIST_TYPE =
        new MetadataClassMapper(
            "ITL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createListType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_STRUCTURE_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_STRUCTURE_TYPE =
        new MetadataClassMapper(
            "MTT",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createStructureType();
                }
            });

    /**
     * A IMMUTABLE_STRUCTURE_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_STRUCTURE_TYPE =
        new MetadataClassMapper(
            "ITT",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createStructureType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_CHOICE_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_CHOICE_TYPE =
        new MetadataClassMapper(
            "MTC",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createChoiceType();
                }
            });

    /**
     * A IMMUTABLE_CHOICE_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_CHOICE_TYPE =
        new MetadataClassMapper(
            "ITC",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createChoiceType()
                        .createImmutable();
                }
            });

    /**
     * A MUTABLE_QUANTITY_TYPE instance
     */
    public static final MetadataClassMapper MUTABLE_QUANTITY_TYPE =
        new MetadataClassMapper(
            "MTQ",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createQuantityType();
                }
            });

    /**
     * A IMMUTABLE_QUANTITY_TYPE instance
     */
    public static final MetadataClassMapper IMMUTABLE_QUANTITY_TYPE =
        new MetadataClassMapper(
            "ITQ",
            new FactoryBinding() {
                public Inhibitor create() {
                    return FACTORY.getTypeFactory().createQuantityType()
                        .createImmutable();
                }
            });

    /**
     * A IMMUTABLE_CHOICE_DEFINITION instance
     */
    public static final MetadataClassMapper IMMUTABLE_CHOICE_DEFINITION =
        new MetadataClassMapper(
            "IDC",
            new FactoryBinding() {
                public Inhibitor create() {
                    return new DefaultMetaDataTypeFactory().
                        createChoiceDefinition(null).createImmutable();
                }
            });

    /**
     * A MUTABLE_CHOICE_DEFINITION instance
     */
    public static final MetadataClassMapper MUTABLE_CHOICE_DEFINITION =
        new MetadataClassMapper(
            "MDC",
            new FactoryBinding() {
                public Inhibitor create() {
                    // the name value does not matter here as it is populated
                    // later by the persistence mechanism
                    return new DefaultMetaDataTypeFactory().
                        createChoiceDefinition(null).createMutable();
                }
            });


    /**
     * A IMMUTABLE_FIELD_DEFINITION instance
     */
    public static final MetadataClassMapper IMMUTABLE_FIELD_DEFINITION =
        new MetadataClassMapper(
            "IDF",
            new FactoryBinding() {
                public Inhibitor create() {
                    return new DefaultMetaDataTypeFactory().
                        createFieldDefinition().createImmutable();
                }
            });

    /**
     * A MUTABLE_FIELD_DEFINITION instance
     */
    public static final MetadataClassMapper MUTABLE_FIELD_DEFINITION =
        new MetadataClassMapper(
            "MDF",
            new FactoryBinding() {
                public Inhibitor create() {
                    return new DefaultMetaDataTypeFactory().
                        createFieldDefinition();
                }
            });

    /**
     * A MUTABLE_ACL instance
     */
    public static final MetadataClassMapper MUTABLE_ACL =
        new MetadataClassMapper(
            "MACL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return new ACLFactoryImpl().createUninitializedACL();
                }
            });

    /**
     * A IMMUTABLE_ACL instance
     */
    public static final MetadataClassMapper IMMUTABLE_ACL =
        new MetadataClassMapper(
            "IACL",
            new FactoryBinding() {
                public Inhibitor create() {
                    return new ACLFactoryImpl().
                        createUninitializedACL().createMutable();
                }
            });

    /**
     * The name of the enumeration entry
     */
    private final String name;

    /**
     * The factory binding to invoke
     */
    private FactoryBinding binding;

    /**
     * Construct a type safe enum instance.
     *
     * @param name the name of the enumeration entry.
     * @param factoryBinding the factory binding to use
     */
    private MetadataClassMapper(String name, FactoryBinding factoryBinding) {
        this.name = name;
        this.binding = factoryBinding;
        if (enums.containsKey(name)) {
            throw new IllegalArgumentException(
                "Enum '" + name + "' already exists");
        }
        enums.put(name, this);
    }

    /**
     * Return the name of the enumeration entry
     */
    public String toString() {
        if (this == UNSUPPORTED) {
            throw new PersistenceException("persistence-not-supported", name);
        }
        return name;
    }

    /**
     * Return the MetadataClassMapper instance corresponding to the specified
     * name.
     *
     * @param name the name of the instance to return.
     * @throws IllegalArgumentException if an enum does not exist for the
     *                                  specified name.
     */
    public static MetadataClassMapper literal(String name) {
        MetadataClassMapper result = null;
        if (enums.containsKey(name)) {
            result = (MetadataClassMapper) enums.get(name);
        } else {
            throw new PersistenceException("persistence-not-supported", name);
        }
        return result;
    }

    /**
     * Get an instance of the class represented by this object
     *
     * @return an instance of the class represented by this object
     */
    public Inhibitor getInstance() {
        return binding.create();
    }

    /**
     * Implementors of this interface are used to invoke the correct factory
     * in order to create an {@link Inhibitor} of some kind
     */
    static interface FactoryBinding {

        /**
         * This method will be called to create an instance of the appropriate
         * type.
         *
         * @return an {@link Inhibitor} instance.
         * @throws PersistenceException if a problem occurs
         */
        Inhibitor create();
    }

}
