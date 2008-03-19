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
 * (c) Volantis Systems Ltd 2005,2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.accessors.xml;

import com.volantis.devrep.device.api.xml.DeviceSchemas;
import com.volantis.devrep.device.api.xml.definitions.Boolean;
import com.volantis.devrep.device.api.xml.definitions.Category;
import com.volantis.devrep.device.api.xml.definitions.DefinitionSet;
import com.volantis.devrep.device.api.xml.definitions.Field;
import com.volantis.devrep.device.api.xml.definitions.Int;
import com.volantis.devrep.device.api.xml.definitions.OrderedSet;
import com.volantis.devrep.device.api.xml.definitions.Policy;
import com.volantis.devrep.device.api.xml.definitions.Range;
import com.volantis.devrep.device.api.xml.definitions.Selection;
import com.volantis.devrep.device.api.xml.definitions.Structure;
import com.volantis.devrep.device.api.xml.definitions.Text;
import com.volantis.devrep.device.api.xml.definitions.Type;
import com.volantis.devrep.device.api.xml.definitions.TypeContainer;
import com.volantis.devrep.device.api.xml.definitions.TypeDeclaration;
import com.volantis.devrep.device.api.xml.definitions.TypeVisitor;
import com.volantis.devrep.device.api.xml.definitions.UnorderedSet;
import com.volantis.devrep.device.api.xml.hierarchy.Hierarchy;
import com.volantis.devrep.device.api.xml.hierarchy.HierarchyEntry;
import com.volantis.devrep.device.api.xml.identification.HeaderPattern;
import com.volantis.devrep.device.api.xml.identification.Identification;
import com.volantis.devrep.device.api.xml.identification.IdentificationEntry;
import com.volantis.devrep.device.api.xml.identification.UserAgentPattern;
import com.volantis.devrep.device.api.xml.policy.PolicyEntry;
import com.volantis.devrep.device.api.xml.policy.PolicyField;
import com.volantis.devrep.device.api.xml.policy.PolicySet;
import com.volantis.devrep.device.api.xml.tacidentification.TacIdentification;
import com.volantis.devrep.device.api.xml.tacidentification.TacIdentificationEntry;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.accessors.xml.DeviceRepositoryConstants;
import com.volantis.devrep.repository.api.accessors.xml.EclipseEntityResolver;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor;
import com.volantis.devrep.repository.impl.DeviceTACPair;
import com.volantis.devrep.repository.impl.TACValue;
import com.volantis.devrep.repository.impl.devices.category.DefaultCategoryDescriptor;
import com.volantis.devrep.repository.impl.devices.policy.DefaultPolicyDescriptor;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultBooleanPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultIntPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultOrderedSetPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultRangePolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultSelectionPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultStructurePolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultTextPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultUnorderedSetPolicyType;
import com.volantis.mcs.accessors.CollectionRepositoryEnumeration;
import com.volantis.mcs.accessors.xml.ZipArchive;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.schema.validator.SchemaValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JiBXDeviceRepositoryAccessor
        extends AbstractDeviceRepositoryAccessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JiBXDeviceRepositoryAccessor.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    JiBXDeviceRepositoryAccessor.class);

    // TODO This is a bit of a hack - the EclipseEntityResolver is required within Eclipse
    private static SchemaValidator deviceSchemaValidator = new SchemaValidator(new EclipseEntityResolver());
    static {
        deviceSchemaValidator.addSchema(DeviceSchemas.DEVICE_CURRENT);
        deviceSchemaValidator.addSchema(DeviceSchemas.CORE_CURRENT);
        deviceSchemaValidator.addSchema(DeviceSchemas.HEIRARCHY_CURRENT);
        deviceSchemaValidator.addSchema(DeviceSchemas.IDENTIFICATION_CURRENT);
        deviceSchemaValidator.addSchema(DeviceSchemas.TAC_IDENTIFICATION_CURRENT);
        deviceSchemaValidator.addSchema(DeviceSchemas.POLICY_DEFINITIONS_CURRENT);
        deviceSchemaValidator.addSchema(DeviceSchemas.REPOSITORY_CURRENT);
    }

    /**
     * Used for synchronization
     */
    private Object lock = new Object();

    private ZipArchive zipArchive;

    private Hierarchy hierarchy;

    private Identification identification;

    private TacIdentification tacIdentification;

    private Definitions definitions;

    private Map localeToProperties;

    private String deviceRepositoryPath;

    /**
     * Map from locales requestes to string representation of locales used to
     * serve the request.
     */
    private Map localeToLocaleFound;

    /**
     * Map from language representations to policy property files.
     */
    private Map languageToProperties;

    /**
     * Flag to determine whether schema validation should take place if a 
     * validator is provided. True by default.
     */ 
    private boolean schemaValidation = true;

    public JiBXDeviceRepositoryAccessor(LocalRepository repository,
                                        DeviceRepositoryLocation location,
                                        boolean schemaValidation) {
        super(repository);
        this.schemaValidation = schemaValidation;
        deviceRepositoryPath =
                createDeviceRepository(location.getDeviceRepositoryName())
                .getAbsolutePath();
        if (deviceRepositoryPath == null) {
            throw new IllegalStateException(EXCEPTION_LOCALIZER.format(
                    "xml-repository-not-found"));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Creating JiBX DeviceRepitoryAccessor");
        }
        localeToProperties = Collections.synchronizedMap(new HashMap());
        localeToLocaleFound = Collections.synchronizedMap(new HashMap());
    }
    
    public JiBXDeviceRepositoryAccessor(LocalRepository repository,
                                        DeviceRepositoryLocation location) {
        // Default to carrying out schema validation where possible
        this(repository, location, true);
    }

    // javadoc inherited
    public void refreshDeviceCache() {
        synchronized (lock) {
            super.refreshDeviceCache();
            zipArchive = null;
            hierarchy = null;
            identification = null;
            tacIdentification = null;
            definitions = null;
            localeToProperties.clear();
            localeToLocaleFound.clear();
            languageToProperties = null;
        }
    }

    public RepositoryEnumeration enumerateDevicePatterns(
            RepositoryConnection connection) throws RepositoryException {

        final Collection devicePatterns = new ArrayList();
        Identification identification = loadIdentification();
        Iterator iterator = identification.entries();
        while (iterator.hasNext()) {
            IdentificationEntry entry = (IdentificationEntry) iterator.next();
            final String deviceName = entry.getDeviceName();
            iterateGenericPatterns(entry, new GenericPatternIteratee() {
                public void next(String pattern) {
                    devicePatterns.add(new String[]{deviceName, pattern});
                }
            });
        }
        return new CollectionRepositoryEnumeration(devicePatterns);
    }


    protected void addDeviceImpl(
            RepositoryConnection connection,
            DefaultDevice device) throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    protected void removeDeviceImpl(RepositoryConnection connection,
                                    String deviceName) throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    protected DefaultDevice retrieveDeviceImpl(RepositoryConnection connection,
                                                String deviceName) throws RepositoryException {

        DefaultDevice device = null;

        // First check in the hierarchy to see if the device exists.
        Hierarchy hierarchy = loadHierarchy();
        final HierarchyEntry entry = hierarchy.find(deviceName);
        if (entry != null) {
            // The device name exists in the hierarchy.

            // Create the device by reading in it's standard and custom
            // polices, and also any device patterns and device TACs.
            device = createDevice(deviceName);

            // We must explicitly add the fallback policy for backward
            // compatibility.
            final HierarchyEntry parent = entry.getParent();
            String fallbackDeviceName;
            if (parent != null) {
                fallbackDeviceName = parent.getDeviceName();
            } else {
                // for backwards compatibility with the old JDOM accessor,
                // we add a fallback of null for Master. Not sure if we
                // really should?
                fallbackDeviceName = null;
            }
            device.setPolicyValue(
                    DeviceRepositoryConstants.FALLBACK_POLICY_NAME,
                    fallbackDeviceName);
        }
        return device;
    }

    private DefaultDevice createDevice(String deviceName)
            throws RepositoryException {

        // Create the device and set it's name.
        DefaultDevice device =
            new DefaultDevice(deviceName, new HashMap(), getPolicyValueFactory());

        // First add all the standard policies to the device.
        String standardDeviceFileName = getXMLFilePath(
                DeviceRepositoryConstants.STANDARD_DEVICE_DIRECTORY,
                deviceName);
        PolicySet standardPolicySet =
            (PolicySet) loadZipObject(standardDeviceFileName, PolicySet.class);
        if (standardPolicySet == null) {
            // No standard device. This is bad since this is mandatory.
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                    "device-definition-missing", deviceName));
        }
        addPolicySetToDevice(standardPolicySet, device);

        // Then add any custom policies to the device.
        String customDeviceFileName = getXMLFilePath(
                DeviceRepositoryConstants.CUSTOM_DEVICE_DIRECTORY,
                deviceName);
        PolicySet customPolicySet =
            (PolicySet) loadZipObject(customDeviceFileName, PolicySet.class);
        if (customPolicySet != null) {
            // we had some custom polices, add them too.
            addPolicySetToDevice(customPolicySet, device);
        }

        // Add the device patterns to the device.
        Identification identification = loadIdentification();
        final Map patterns = new HashMap();
        IdentificationEntry idEntry = identification.find(deviceName);
        if (idEntry != null) {
            iterateGenericPatterns(idEntry, new GenericPatternIteratee() {
                public void next(String pattern) {
                    // NOTE: yes this is intentional, why this must be a
                    // map I have no idea - a list would be fine.
                    patterns.put(pattern, null);
                }
            });
            if (patterns.size() != 0) {
                device.setPatterns(patterns);
            }
        }
        // Add the device TACs to the device.
        TacIdentification tacIdentification = loadTacIdentification();
        if (tacIdentification != null) {
            TacIdentificationEntry tacEntry = tacIdentification.find(
                    deviceName);
            if (tacEntry != null) {
                Set tacSet = convertTacEntryToTacSet(tacEntry);
                if (tacSet.size() != 0) {
                    device.setTACValues(tacSet);
                }
            }
        }
        return device;
    }

    protected RepositoryEnumeration enumerateDevicesChildrenImpl(
            RepositoryConnection connection,
            String deviceName)
            throws RepositoryException {

        Iterator children = null;
        Hierarchy hierarchy = loadHierarchy();
        HierarchyEntry entry = hierarchy.find(deviceName);
        if (entry != null) {
            children = entry.children();
        }
        return new IteratorRepositoryEnumeration(children) {

            public Object next() throws RepositoryException {
                HierarchyEntry child = (HierarchyEntry) super.next();
                return child.getDeviceName();
            }
        };
    }

    protected void renameDeviceImpl(RepositoryConnection connection,
                                    String deviceName, String newName) throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public void removePolicy(RepositoryConnection connection,
                             String policyName) throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public RepositoryEnumeration enumerateDeviceNames(
            RepositoryConnection connection) throws RepositoryException {

        Hierarchy hierarchy = loadHierarchy();
        Iterator entries = hierarchy.entries();
        return new IteratorRepositoryEnumeration(entries) {
            public Object next() throws RepositoryException {
                HierarchyEntry entry = (HierarchyEntry) super.next();
                return entry.getDeviceName();
            }
        };
    }

    public RepositoryEnumeration enumerateDeviceFallbacks(
            RepositoryConnection connection) throws RepositoryException {

        Collection deviceFallbacks = new ArrayList();
        Hierarchy hierarchy = loadHierarchy();
        Iterator entries = hierarchy.entries();
        while (entries.hasNext()) {
            HierarchyEntry entry = (HierarchyEntry) entries.next();
            HierarchyEntry parent = entry.getParent();
            if (parent != null) {
                deviceFallbacks.add(new String[] {
                    entry.getDeviceName(), parent.getDeviceName()});
            } else {
                // Add a special Master=null entry.
                // This is how the old JDOM accessor worked.
                deviceFallbacks.add(new String[] {
                    entry.getDeviceName(), null});
            }
        }
        return new CollectionRepositoryEnumeration(deviceFallbacks);
    }

    public RepositoryEnumeration enumerateDeviceTACs(
            RepositoryConnection connection) throws RepositoryException {

        Collection tacPairs = new ArrayList();
        TacIdentification tacIdentification = loadTacIdentification();
        if (tacIdentification != null) {
            Iterator entries = tacIdentification.entries();
            while (entries.hasNext()) {
                TacIdentificationEntry entry = (TacIdentificationEntry)
                        entries.next();
                String deviceName = entry.getDeviceName();
                Iterator numbers = entry.numbers();
                while (numbers.hasNext()) {
                    String number = (String) numbers.next();
                    try {
                        long tac = Long.parseLong(number);
                        tacPairs.add(new DeviceTACPair(tac, deviceName));
                    } catch (NumberFormatException nfe) {
                        // Shouldn't get any of these since the schema
                        // carries out validation - if we do, then ignore the
                        // invalid TAC and log it as a warning.
                        logger.warn("long-tac-conversion-error",
                                new Object[]{number});
                    }
                }
            }
        }
        // else, we return an empty enumeration.
        return new CollectionRepositoryEnumeration(tacPairs);
    }

    public void updatePolicyName(RepositoryConnection connection,
                                 String oldPolicyName, String newPolicyName)
            throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection) throws RepositoryException {

        List names = new ArrayList();
        Definitions definitions = createDefinitions();
        Iterator categories = definitions.categories();
        while (categories.hasNext()) {
            Category category = (Category) categories.next();
            extractCategoryPolicyNames(category, names);
        }

        // ... plus the fake fallback policy name.
        // Note that this will have a bit of a strange ordering, but I don't
        // think we make any guarantees about ordering...
        names.add(DeviceRepositoryConstants.FALLBACK_POLICY_NAME);

        return new CollectionRepositoryEnumeration(names);
    }

    public RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection, String categoryName)
            throws RepositoryException {

        List names = new ArrayList();
        Definitions definitions = createDefinitions();
        Category category = definitions.getCategory(categoryName);
        if (category != null) {
            extractCategoryPolicyNames(category, names);
        }

        // ... plus the fake fallback policy name, if the category is the
        // fallback category.
        // Note that this will have a bit of a strange ordering, but I don't
        // think we make any guarantees about ordering...
        if (DeviceRepositoryConstants.FALLBACK_POLICY_CATEGORY.equals(
                categoryName)) {
            names.add(DeviceRepositoryConstants.FALLBACK_POLICY_NAME);
        }

        return new CollectionRepositoryEnumeration(names);
    }

    public RepositoryEnumeration enumerateCategoryNames(
                final RepositoryConnection connection)
            throws RepositoryException {

        final Definitions definitions = createDefinitions();
        final Iterator categoriesIter = definitions.categories();
        final List categoryNames = new LinkedList();
        while (categoriesIter.hasNext()) {
            final Category category = (Category) categoriesIter.next();
            categoryNames.add(category.getName());
        }
        return new CollectionRepositoryEnumeration(categoryNames);
    }

    public PolicyDescriptor retrievePolicyDescriptor(
            RepositoryConnection connection,
            String policyName,
            Locale locale)
            throws RepositoryException {

        Definitions definitions = createDefinitions();
        return createPolicyDescriptor(definitions, policyName, locale);
    }

    public CategoryDescriptor retrieveCategoryDescriptor(
                final RepositoryConnection connection,
                final String categoryName,
                final Locale locale)
            throws RepositoryException {
        final Definitions definitions = createDefinitions();
        return createCategoryDescriptor(definitions, categoryName, locale);
    }

    public void addPolicyDescriptor(
            RepositoryConnection connection,
            String policyName,
            PolicyDescriptor descriptor)
            throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public void addCategoryDescriptor(
            RepositoryConnection connection,
            String categoryName,
            CategoryDescriptor descriptor)
            throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public void removePolicyDescriptor(
            RepositoryConnection connection,
            String policyName) throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public void removeAllPolicyDescriptors(RepositoryConnection connection)
            throws RepositoryException {
        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public void removeCategoryDescriptor(
            RepositoryConnection connection,
            String categoryName) throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    public void removeAllCategoryDescriptors(RepositoryConnection connection)
            throws RepositoryException {
        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    // ====================================================================
    //
    // SUPPORTING METHODS
    //
    // ====================================================================


    // ====================================================================
    // Hierarchy
    // ====================================================================

    /**
     *
     * @return the hierarchy, will not be null
     * @throws RepositoryException
     */
    private Hierarchy loadHierarchy()
            throws RepositoryException {

        if (hierarchy == null) {
            // Load in the hiearchy file via JiBX
            hierarchy = (Hierarchy) loadZipObject(
                DeviceRepositoryConstants.HIERARCHY_XML, Hierarchy.class);
            if (hierarchy == null) {
                throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                        "device-repository-file-missing",
                        DeviceRepositoryConstants.HIERARCHY_XML));
            }
        }
        return hierarchy;
    }


    // ====================================================================
    // Identification
    // ====================================================================

    /**
     *
     * @return the identification details, will not be null.
     * @throws RepositoryException
     */
    private Identification loadIdentification()
            throws RepositoryException {

        if (identification == null) {
            identification = (Identification) loadZipObject(
                    DeviceRepositoryConstants.IDENTIFICATION_XML,
                    Identification.class);
            if (identification == null) {
                throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                        "device-repository-file-missing",
                        DeviceRepositoryConstants.IDENTIFICATION_XML));
            }
        }

        return identification;
    }

    private interface GenericPatternIteratee {

        void next(String pattern);
    }

    private void iterateGenericPatterns(IdentificationEntry entry,
                                        GenericPatternIteratee iteratee) {

        Iterator userAgentPatterns = entry.userAgentPatterns();
        while (userAgentPatterns.hasNext()) {
            UserAgentPattern userAgent = (UserAgentPattern)
                    userAgentPatterns.next();
            String pattern = userAgent.getRegularExpression();
            iteratee.next(pattern);
        }

        Iterator headerPatterns = entry.headerPatterns();
        while (headerPatterns.hasNext()) {
            HeaderPattern header = (HeaderPattern) headerPatterns.next();
            final String pattern = convertHeaderPatternToGenericPattern(header);
            iteratee.next(pattern);
        }

    }

    private String convertHeaderPatternToGenericPattern(HeaderPattern header) {

        // As per AN055 Implementation Note (apart from backwards compatibility
        // notes below).
        StringBuffer patternBuffer = new StringBuffer();
        // Note: for backwards compatibility with the old JDOM accessor we do
        // not make the header name lower case.
        patternBuffer.append(header.getName()/*.toLowerCase()*/);
        // Note: for backwards compatibility with the old JDOM accessor we use
        // ": " instead of " :".
        patternBuffer.append(": "/*" :"*/);
        patternBuffer.append(header.getRegularExpression());
        if (header.getBaseDevice() != null) {
            patternBuffer.append(" ");
            patternBuffer.append(header.getBaseDevice());
        }
        return patternBuffer.toString();
    }


    // ====================================================================
    // TAC Identification
    // ====================================================================

    private TacIdentification loadTacIdentification()
            throws RepositoryException {

        if (tacIdentification == null) {
            tacIdentification = (TacIdentification) loadZipObject(
                    DeviceRepositoryConstants.TAC_IDENTIFICATION_XML,
                    TacIdentification.class);
        }

        return tacIdentification;
    }

    private Set convertTacEntryToTacSet(TacIdentificationEntry tacEntry) {
        Iterator numbers = tacEntry.numbers();
        Set tacSet = new HashSet();
        while (numbers.hasNext()) {
            String number = (String) numbers.next();
            try {
                long tac = Long.parseLong(number);
                tacSet.add(new TACValue(tac));
            } catch (NumberFormatException nfe) {
                // Shouldn't get any of these since the schema
                // carries out validation - if we do, then ignore the
                // invalid TAC and log it as a warning.
                logger.warn("long-tac-conversion-error", new Object[]{
                    number});
            }
        }
        return tacSet;
    }


    // ====================================================================
    // Policy
    // ====================================================================

    private void addPolicySetToDevice(PolicySet policySet,
                                      DefaultDevice device) {

        Iterator entries = policySet.entries();
        while (entries.hasNext()) {
            PolicyEntry entry = (PolicyEntry) entries.next();
            Iterator fields = entry.fields();
            Iterator values = entry.values();
            if (fields.hasNext()) {
                // it has fields, so this is a structure entry.
                // NOTE: schema says each field can contain multiple values but
                // this is not used so is not implemented.
                while (fields.hasNext()) {
                    PolicyField field = (PolicyField) fields.next();
                    String name = entry.getName() + "." + field.getName();
                    device.setPolicyValue(name, field.getValue());
                }
            } else if (values.hasNext()) {
                // it has values, so it is a simple entry which may have
                // multiple values.
                String value = (String) values.next();
                StringBuffer buffer = new StringBuffer(value);
                while (values.hasNext()) {
                    value = (String) values.next();
                    buffer.append(",");
                    buffer.append(value);
                }
                device.setPolicyValue(entry.getName(), buffer.toString());
            } else {
                // no values at all, for backwards compatibility with the
                // old JDOM accessor we add it with no value. Not sure if we
                // really should?
                device.setPolicyValue(entry.getName(), "");
            }
        }
    }


    // ====================================================================
    // Definitions
    // ====================================================================

    /**
     *
     * @return the definitions, will not be null.
     * @throws RepositoryException
     */
    private Definitions createDefinitions()
            throws RepositoryException {
        synchronized (lock) {
	    if (definitions == null) {

		// Create some empty definitions.
		definitions = new Definitions();

		// Add the standard set of definitions.
		DefinitionSet standardDefinitionSet = (DefinitionSet) loadZipObject(
		        DeviceRepositoryConstants.STANDARD_DEFINITIONS_XML,
                        DefinitionSet.class);
		if (standardDefinitionSet == null) {
		    throw new RepositoryException(EXCEPTION_LOCALIZER.format(
			       "device-repository-file-missing",
                               DeviceRepositoryConstants.STANDARD_DEFINITIONS_XML));
		}
		addDefinitionSetToDefinitions(standardDefinitionSet, definitions);

		// Add the custom set of definitions, if there are any.
		DefinitionSet customDefinitionSet = (DefinitionSet) loadZipObject(
                        DeviceRepositoryConstants.CUSTOM_DEFINITIONS_XML,
                        DefinitionSet.class);
		if (customDefinitionSet != null) {
		    // NOTE: here we only add categories, ignoring custom types.
		    // If we tried to add the types as well we blow up with a
		    // duplicate type exception. This is because there is a bug in
		    // the 2.9 -> 3.0 migration (DeviceDefinitionsMigrationJob)
		    // which adds the EmulateEmphasisTag type declaration to both
		    // standard and custom definitions files. So, here we just
		    // ignore any custom types for now since we cannot have any at
		    // the moment anyway. If we ever have any then we will need to
		    // handle this problem in a more intelligent fashion.
		    addDefinitionSetCategoriesToDefinitions(
                              customDefinitionSet, definitions);
		}

	    } 
        }
        return definitions;

    }

    private void addDefinitionSetToDefinitions(DefinitionSet definitionSet,
                                               Definitions definitions) {

        // NOTE: we assume that any types and categories in different sets
        // added to the definitions are disjoint. This should be the case as
        // custom definiton sets have no types and custom definitions are
        // always in the custom category.

        Iterator types = definitionSet.types();
        while (types.hasNext()) {
            TypeDeclaration type = (TypeDeclaration) types.next();
            definitions.addType(type);
        }

        addDefinitionSetCategoriesToDefinitions(definitionSet, definitions);
    }

    private void addDefinitionSetCategoriesToDefinitions(
            DefinitionSet definitionSet, Definitions definitions) {
        Iterator categories = definitionSet.categories();
        while (categories.hasNext()) {
            Category category = (Category) categories.next();
            definitions.addCategory(category);
        }
    }

    private void extractCategoryPolicyNames(Category category, List names) {
        Iterator policies = category.policies();
        while (policies.hasNext()) {
            Policy policy = (Policy) policies.next();
            names.add(policy.getName());
        }
    }

    private PolicyDescriptor createPolicyDescriptor(Definitions definitions,
                                                    String policyName, Locale locale) throws RepositoryException {

        // The fallback policy value is created at runtime in XML, so there is
        // no metadata associated with it in the repository files. Thus, we
        // must intercept any attempt to retrieve the descriptor for the
        // fallback policy and provide some "fake" metadata to describe it.
        // Otherwise the user will get a NPE when they do this. Also, adding
        // this here (and above) means that the import process will add this
        // metadata to the JDBC repository as well.
        if (DeviceRepositoryConstants.FALLBACK_POLICY_NAME.equals(policyName)) {
            DefaultPolicyDescriptor descriptor = new DefaultPolicyDescriptor();
            descriptor.setCategory(DeviceRepositoryConstants.FALLBACK_POLICY_CATEGORY);
            // NOTE: We intentionally set the descriptive name and help to
            // default values because localising it would be a hassle.
            // This is because the properties file is currently auto-generated
            // from the Proteus version each time we make a change and we
            // can't add the full metadata to the GUI so we'd have to manually
            // re-add these property value every time. Not worth it!
            descriptor.setPolicyDescriptiveName(policyName);
            descriptor.setPolicyHelp(policyName);
            descriptor.setPolicyType(new DefaultTextPolicyType());
            descriptor.setLanguage((String) localeToLocaleFound.get(locale));
            return descriptor;
        }

        DefaultPolicyDescriptor descriptor = null;

        Policy policy = definitions.getPolicy(policyName);
        if (policy != null) {

            // Create the basic policy descriptor
            descriptor = new DefaultPolicyDescriptor();

            // Set the descriptive name
            String nameProperty =
                    DeviceRepositoryConstants.POLICY_RESOURCE_PREFIX +
                    policyName +
                    DeviceRepositoryConstants.NAME_PROPERTY_SUFFIX;
            String name = createProperties(locale).getProperty(nameProperty);
            if (name == null) {
                descriptor.setPolicyDescriptiveName(policyName);
            } else {
                descriptor.setPolicyDescriptiveName(name);
            }
            descriptor.setLanguage((String) localeToLocaleFound.get(locale));

            // Set the help
            String descriptionProperty =
                    DeviceRepositoryConstants.POLICY_RESOURCE_PREFIX +
                    policyName +
                    DeviceRepositoryConstants.DESCRIPTION_PROPERTY_SUFFIX;
            String help = createProperties(locale).getProperty(descriptionProperty);
            if (help == null) {
                descriptor.setPolicyHelp(policyName);
            } else {
                descriptor.setPolicyHelp(help);
            }

            // Set the category (short) name
            descriptor.setCategory(policy.getCategory().getName());

            // TODO: later: set the category long name/description as well so
            // that the UpdateClient merge report can access it from there
            // rather than using the old JDOM accessor code as it does now.

            // Set the the type
            // Work out the type of the policy.
            final TypeContainer typeContainer = policy.getTypeContainer();
            String typeName = typeContainer.getName();
            Type type = typeContainer.getType();
            if (typeName != null && type == null) {
                // this is a reference to a declared type.
                TypeDeclaration declaration = definitions.getType(typeName);
                if (declaration == null) {
                    throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                            "repository-unable-to-find-type-information",
                            typeName));
                }
                type = declaration.getType();
            } else if (typeName == null && type != null) {
                // this is an anonymous type

            } else if (typeName == null && type == null) {
                // nothing specified, error.
                throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                        "repository-unable-to-find-type-information",
                        policyName));
            } else if (typeName != null && type != null) {
                // both specified, error.
                throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                        "repository-unable-to-find-type-information",
                        policyName));
            }

            // OK, we have the type, so create the descriptor for it.
            PolicyTypeCreator creator = new PolicyTypeCreator();
            PolicyType policyType = creator.calculate(type);
            descriptor.setPolicyType(policyType);
        }

        // And return the completed object
        return descriptor;
    }

    private CategoryDescriptor createCategoryDescriptor(
                final Definitions definitions,
                final String categoryName,
                final Locale locale)
            throws RepositoryException {

        DefaultCategoryDescriptor descriptor = null;

        final Category category = definitions.getCategory(categoryName);
        if (category != null) {
            // Create the basic category descriptor
            descriptor = new DefaultCategoryDescriptor();

            // Set the descriptive name
            final String nameProperty =
                    DeviceRepositoryConstants.CATEGORY_RESOURCE_PREFIX +
                    categoryName +
                    DeviceRepositoryConstants.NAME_PROPERTY_SUFFIX;
            final String name =
                createProperties(locale).getProperty(nameProperty);
            if (name == null) {
                descriptor.setCategoryDescriptiveName(categoryName);
            } else {
                descriptor.setCategoryDescriptiveName(name);
            }
            descriptor.setLanguage((String) localeToLocaleFound.get(locale));
        }

        return descriptor;
    }

    // ====================================================================
    // Properties
    // ====================================================================
    // NOTE: most of this was cut and pasted from the old accessor and is very
    // nasty, as it tries to re-implement ResourceBundle. Argh.
    // TODO: later: replace populateProperties with impl using ResourceBundle
    // and a classloader over the zip archive.

    private Properties createProperties(Locale locale)
            throws RepositoryException {

        Properties properties = (Properties) localeToProperties.get(locale);
        if (properties == null) {
            properties = createMergedProperties(locale);
            localeToProperties.put(locale, properties);
        }
        return properties;
    }

    /**
     * Creates a Properties object containing all standard and optional custom
     * properties.
     *
     * @return the Properties object
     * @param locale
     */
    private Properties createMergedProperties(Locale locale)
            throws RepositoryException {

        Properties allProps = new Properties();
        Properties customProps = new Properties();

        String localeFound = populateProperties(
                DeviceRepositoryConstants.STANDARD_POLICIES_PROPERTIES_PREFIX,
                allProps, locale);

        // There must be standard properties.
        if (localeFound == null) {
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                    "device-repository-file-missing",
                    DeviceRepositoryConstants.STANDARD_POLICIES_PROPERTIES_PREFIX));
        }

        localeToLocaleFound.put(locale, localeFound);

        localeFound = populateProperties(
                DeviceRepositoryConstants.CUSTOM_POLICIES_PROPERTIES_PREFIX,
                customProps, locale);

        // Custom properties are optional.
        if (localeFound != null) {
            Enumeration customEnum = customProps.propertyNames();

            while (customEnum.hasMoreElements()) {
                String propertyName = (String) customEnum.nextElement();
                String value = customProps.getProperty(propertyName);
                allProps.setProperty(propertyName, value);
            }
        }

        return allProps;
    }

    /**
     * Returns the pathname within the device repository of the properties file
     * with the given prefix, and populates the given properties object with
     * the properties found in this file.
     *
     * If properties for the current locale cannot be found then the default
     * non-localized properties file (i.e. English language) will be used. In
     * this latter case some debug will be logged.
     *
     * The rules for finding the locale specific properties file are to look
     * from most specific to most general.
     *
     * @param prefix the prefix of the properties file of interest
     * @param properties the Properties object to populate
     * @return the path of the properties file within the device repository
     */
    private String populateProperties(String prefix, Properties properties,
                                      Locale locale)
            throws RepositoryException {

        ZipArchive zipArchive;
        try {
            zipArchive = getZipArchive();
        } catch (IOException e) {
            throw new RepositoryException(
                EXCEPTION_LOCALIZER.format("device-repository-access-failure"));
        }

        // the language will be either a 2 character identifier or an empty
        // string
        String language = locale.getLanguage();
        // the country will be either a 2 character identifier or an empty
        // string
        String country = locale.getCountry();
        // the country will be an empty string or some other identifier
        String variant = locale.getVariant();

        String localeFound = null;
        String propertiesFile = null;
        // see if we have a properites file with the following suffix
        // _LANGUAGE_COUNTRY_VARIANT.properties
        if (language.length() != 0 &&
                country.length() != 0 &&
                variant.length() != 0) {
            String checkPath =
                    getPropertiesPath(prefix, language, country, variant);
            if (zipArchive.exists(checkPath)) {
                localeFound = language + "_" + country + "_" + variant;
                propertiesFile = checkPath;
            }
        }
        // If we haven't found the properties file then we try the less
        // specific suffix _LANGUAGE_COUNTRY.properites
        if (propertiesFile == null &&
                language.length() != 0 &&
                country.length() != 0) {
            String checkPath =
                    getPropertiesPath(prefix, language, country, null);
            if (zipArchive.exists(checkPath)) {
                localeFound = language + "_" + country + "_";
                propertiesFile = checkPath;
            }
        }
        // If we haven't found the properties file then we try the less
        // specific suffix _LANGUAGE.properites
        if (propertiesFile == null && language.length() != 0) {
            String checkPath = getPropertiesPath(prefix, language, null, null);
            if (zipArchive.exists(checkPath)) {
                localeFound = language + "__";
                propertiesFile = checkPath;
            }
        }
        // If we haven't found the properties file then we try the
        // .properties
        if (propertiesFile == null) {
            String checkPath = getPropertiesPath(prefix, null, null, null);
            if (zipArchive.exists(checkPath)) {
                localeFound = "__";
                propertiesFile = checkPath;
            }
        }
        if (propertiesFile != null) {
            // if we found a properties file the load its contents into the
            // properties instance
            try {
                properties.load(zipArchive.getInputFrom(propertiesFile));
            } catch (IOException e) {
                throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                        "device-repository-file-missing", propertiesFile));
            }
        } else if (logger.isDebugEnabled()) {
            logger.debug("Could not locate the properties file for the prefix "
                    + prefix);
        }
        return localeFound;
    }

    /**
     * Gets the default name for the properties file using the given prefix and
     * the default locale.
     * @param prefix the file's prefix
     * @param language the locale's language can be null
     * @param country the locale's country can be null
     * @param variant the locale's variant can be null
     * @return the default pathname
     */
    private String getPropertiesPath(String prefix,
                                     String language,
                                     String country,
                                     String variant) {
        StringBuffer buffer = new StringBuffer(prefix);
        if (language != null && language.length() != 0) {
            buffer.append('_').append(language);
            if (country != null && country.length() != 0) {
                buffer.append('_').append(country);
                if (variant != null && variant.length() != 0) {
                    buffer.append('_').append(variant);
                }
            }
        }
        buffer.append(DeviceRepositoryConstants.POLICIES_PROPERTIES_SUFFIX);
        return buffer.toString();
    }

    // javadoc inherited
    public List retrievePolicyDescriptors(final RepositoryConnection connection,
                                          final String policyName)
            throws RepositoryException {

        final List result = new LinkedList();
        final Map policyPropertiesMap = getPolicyPropertiesMap();
        final String nameProperty =
            DeviceRepositoryConstants.POLICY_RESOURCE_PREFIX + policyName +
            DeviceRepositoryConstants.NAME_PROPERTY_SUFFIX;
        for (Iterator iter = policyPropertiesMap.entrySet().iterator();
             iter.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String language = (String) entry.getKey();
            final Properties properties = (Properties) entry.getValue();
            if (properties.containsKey(nameProperty)) {
                final Locale locale =
                    convertPropertiesLanguageToLocale(language);
                final PolicyDescriptor policyDescriptor =
                    retrievePolicyDescriptor(connection, policyName, locale);
                if (policyDescriptor != null) {
                    result.add(policyDescriptor);
                }
            }
        }
        if(result.size() == 0){
            // property not found in any locale so wee need to add at least
            // default one
            final Locale locale =
                    convertPropertiesLanguageToLocale("");
            final PolicyDescriptor policyDescriptor =
                retrievePolicyDescriptor(connection, policyName, locale);
            if (policyDescriptor != null) {
                result.add(policyDescriptor);
            }            
        }
        return result;
    }

    // javadoc inherited
    public List retrieveCategoryDescriptors(
                final RepositoryConnection connection, final String categoryName)
            throws RepositoryException {

        final List result = new LinkedList();
        final Map policyPropertiesMap = getPolicyPropertiesMap();
        final String nameProperty =
            DeviceRepositoryConstants.CATEGORY_RESOURCE_PREFIX + categoryName +
            DeviceRepositoryConstants.NAME_PROPERTY_SUFFIX;
        for (Iterator iter = policyPropertiesMap.entrySet().iterator();
             iter.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) iter.next();
            String language = (String) entry.getKey();
            final Properties properties = (Properties) entry.getValue();
            if (properties.containsKey(nameProperty)) {
                final Locale locale =
                    convertPropertiesLanguageToLocale(language);
                final CategoryDescriptor categoryDescriptor =
                    retrieveCategoryDescriptor(connection, categoryName, locale);
                if (categoryDescriptor != null) {
                    result.add(categoryDescriptor);
                }
            }
        }
        return result;
    }

    /**
     * Converts a string representation of a language as extracted from a
     * properties file name to a Locale.
     *
     * @param language the language to convert, must not be null
     * @return the converted Locale
     */
    private Locale convertPropertiesLanguageToLocale(String language) {
        if (language.startsWith("_")) {
            language = language.substring(1);
        }
        final String[] parts = language.split("_", 3);
        final Locale locale;
        if (parts.length == 0) {
            locale = new Locale("", "", "");
        } else if (parts.length == 1) {
            locale = new Locale(parts[0]);
        } else if (parts.length == 2) {
            locale = new Locale(parts[0], parts[1]);
        } else {
            locale = new Locale(parts[0], parts[1], parts[2]);
        }
        return locale;
    }

    // ====================================================================
    // Shared
    // ====================================================================

    /**
     * Returns the repository filename that should be accessed.
     *
     * @return the repository filename that should be accessed
     */
    protected String getRepositoryFilename() {
        return deviceRepositoryPath;
    }

    private ZipArchive getZipArchive()
            throws IOException {

        if (zipArchive == null) {
            String archiveFileName = getRepositoryFilename();
            zipArchive = new ZipArchive(archiveFileName);
        }
        return zipArchive;
    }

    private Object loadZipObject(final String filename,
                                 final Class expectedClass)
            throws RepositoryException {

        try {
            ZipArchive archive = getZipArchive();
            InputStream stream = archive.getInputFrom(filename);
            Object object = null;
            if (stream != null) {
                BinaryContentInput content = new BinaryContentInput(stream);
                JiBXReader jibxReader = new JiBXReader(expectedClass,
                        schemaValidation ? deviceSchemaValidator : null);
                object = jibxReader.read(content, filename);
            }
            return object;
        } catch (IOException e) {
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                    "cannot-read-object", new Object[]{
                            expectedClass.getName(), filename}), e);
        }
    }

    /**
     * Take a directory and filename and return a full filepath to an xml
     * file, such as path/to/my/device-file.xml
     * <p> If the directory path is not suffixed by a path separator then one
     * is added.
     * <p> If the file name is not suffixed by a ".xml" extension then it is
     * appended.
     * @param directory The directory file path
     * @param filename The file name
     * @return A full file path to an xml file.
     */
    private String getXMLFilePath(String directory, String filename) {
        StringBuffer result = new StringBuffer(directory);
        if (!(directory.endsWith("/") || directory.endsWith("\\"))) {
            result.append(File.separator);
        }
        result.append(filename);

        if (!(filename.endsWith(DeviceRepositoryConstants.XML_SUFFIX))) {
            result.append(DeviceRepositoryConstants.XML_SUFFIX);
        }
        return result.toString();
    }

    /**
     * Returns a map that contains the localized policy/category names and
     * policy descriptions.
     *
     * <p>The key of the map is the string representation of the language, the
     * values are the properties.</p>
     *
     * @return the map for the localized names/descriptions
     * @throws RepositoryException if the repository cannot be read
     */
    private Map getPolicyPropertiesMap() throws RepositoryException {
        if (languageToProperties == null) {
            languageToProperties = Collections.synchronizedMap(new HashMap());
            try {
                final ZipInputStream zipStream = new ZipInputStream(
                    new FileInputStream(getRepositoryFilename()));
                for (ZipEntry entry = zipStream.getNextEntry(); entry != null;
                     entry = zipStream.getNextEntry()) {
                    final String name = entry.getName();
                    final boolean standardPrefix = name.startsWith(
                        DeviceRepositoryConstants.
                            STANDARD_POLICIES_PROPERTIES_PREFIX);
                    final boolean customPrefix = name.startsWith(
                        DeviceRepositoryConstants.
                            CUSTOM_POLICIES_PROPERTIES_PREFIX);
                    if ((standardPrefix || customPrefix) &&
                        name.endsWith(DeviceRepositoryConstants.
                            POLICIES_PROPERTIES_SUFFIX)) {

                        Properties properties = new Properties();
                        properties.load(zipStream);
                        final String language;
                        if (standardPrefix) {
                            language = name.substring(DeviceRepositoryConstants.
                                    STANDARD_POLICIES_PROPERTIES_PREFIX.length(),
                                name.length() - DeviceRepositoryConstants.
                                    POLICIES_PROPERTIES_SUFFIX.length());
                        } else {
                            language = name.substring(DeviceRepositoryConstants.
                                    CUSTOM_POLICIES_PROPERTIES_PREFIX.length(),
                                name.length() - DeviceRepositoryConstants.
                                    POLICIES_PROPERTIES_SUFFIX.length());
                        }
                        languageToProperties.put(language, properties);
                    }
                }
            } catch (IOException e) {
                throw new RepositoryException("Cannot read repository", e);
            }
        }
        return languageToProperties;
    }

    private static class PolicyTypeCreator implements TypeVisitor {

        private static final DefaultBooleanPolicyType BOOLEAN_POLICY_TYPE =
                new DefaultBooleanPolicyType();

        private static final DefaultIntPolicyType INT_POLICY_TYPE =
                new DefaultIntPolicyType();

        private static final DefaultTextPolicyType TEXT_POLICY_TYPE =
                new DefaultTextPolicyType();

        private PolicyType policyType;

        public PolicyType calculate(Type type) {
            type.accept(this);
            return policyType;
        }

        public void visit(Boolean type) {
            policyType = BOOLEAN_POLICY_TYPE;
        }

        public void visit(Int type) {
            policyType = INT_POLICY_TYPE;
        }

        public void visit(Text type) {
            policyType = TEXT_POLICY_TYPE;
        }

        public void visit(Range type) {
            policyType = new DefaultRangePolicyType(type.getMinInclusive(),
                    type.getMaxInclusive());
        }

        public void visit(OrderedSet type) {
            policyType = new DefaultOrderedSetPolicyType(
                    calculate(type.getType()));
        }

        public void visit(UnorderedSet type) {
            policyType = new DefaultUnorderedSetPolicyType(
                    calculate(type.getType()));
        }

        public void visit(Selection type) {
            DefaultSelectionPolicyType selection =
                    new DefaultSelectionPolicyType();
            Iterator keywords = type.keywords();
            while (keywords.hasNext()) {
                String keyword = (String) keywords.next();
                selection.addKeyword(keyword);
            }
            selection.complete();
            policyType = selection;

        }

        public void visit(Structure type) {
            DefaultStructurePolicyType structure =
                    new DefaultStructurePolicyType();
            Iterator fields = type.fields();
            while (fields.hasNext()) {
                Field field = (Field) fields.next();
                structure.addFieldType(field.getName(),
                        calculate(field.getType()));
            }
            structure.complete();
            policyType = structure;
        }
    }

    /**
     * Resolve the file name given and create it for use as the device
     * repository.
     *
     * @param deviceRepositoryName the file name to resolve and create
     * @return the device repository file, or null it did not map to a file
     * @throws IllegalArgumentException if the file found was not valid
     */
    private static File createDeviceRepository(String deviceRepositoryName) {

        File deviceRepository = createRepositoryFile(deviceRepositoryName);

        if(deviceRepository != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Device repository location is " +
                             deviceRepository.getAbsolutePath());
            }

            if (!deviceRepository.exists()) {
                throw new IllegalArgumentException(
                        EXCEPTION_LOCALIZER.format("file-missing",
                                                   deviceRepository));
            } else if (deviceRepository.isDirectory()) {
                throw new IllegalArgumentException(
                        EXCEPTION_LOCALIZER.format("file-is-directory",
                                                   deviceRepository));
            } else if (!deviceRepository.canRead()) {
                throw new IllegalArgumentException(
                        EXCEPTION_LOCALIZER.format("file-cannot-be-read",
                                                   deviceRepository));
            }
        } else if (logger.isDebugEnabled()) {
            logger.debug("No device repository location specified");
        }
        return deviceRepository;
    }

    /**
     * Utility method to avoid code duplication. Ensures that if a path is
     * specified it is a valid absolute path. Does not check if the file exists.
     *
     * @param path absolute path to file
     * @return File mapping to that path, or null if no file found
     * @throws IllegalArgumentException if the path is not absolute
     */
    private static File createRepositoryFile(String path) {
        File f = null;
        if ((path != null) && (path.length() != 0)) {
            f = new File(path);
            if (!f.isAbsolute()) {
                // should not happen - paths should be validated by this point
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "file-path-is-not-absolute", path));
            }
        }
        return f;
    }
}
