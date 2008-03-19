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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.api.framework;


/**
 * An object which builds {@link ResourceMigrator} instances.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User extensions of this class are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ResourceMigratorBuilder {

    /**
     * Called to set the version that the migrator will target as the latest
     * version.
     * <p>
     * NOTE: This method must be called before {@link #startType}.
     *
     * @param version the target version.
     */
    void setTarget(Version version);

    /**
     * Called to indicate that a new type should be opened.
     *
     * @param typeName the name of the type to be opened.
     */
    void startType(String typeName);

    /**
     * Called to set a regexp path recogniser for the currently open type.
     *
     * @param re the regexp to recognise paths with.
     */
    void setRegexpPathRecogniser(String re);

    /**
     * Called to set a custom path recogniser for the currently open type.
     *
     * @param pathRecogniser the custom path recogniser to use.
     */
    void setCustomPathRecogniser(PathRecogniser pathRecogniser);

    /**
     * Called to add a regexp content recogniser to the currently open type.
     *
     * @param version the version of the resource that this content identifier
     *      identifies.
     * @param re the regexp to recognise the content with.
     */
    void addRegexpContentRecogniser(Version version, String re);

    /**
     * Called to add a custom content recogniser to the currently open type.
     *
     * @param version the version that this content identifier identifies.
     * @param contentRecogniser the custom content recogniser to use.
     */
    void addCustomContentRecogniser(Version version,
            ContentRecogniser contentRecogniser);

    /**
     * Called to create a builder for XSL stream migrators.
     *
     * @return the created builder.
     */
    XSLStreamMigratorBuilder createXSLStreamMigratorBuilder();

    /**
     * Called to add a custom migration step to the currently open type.
     *
     * @param inputVersion the version that the custom stream migrator migrates
     *      from.
     * @param outputVersion the version that the custom stream migrator
     *      migrates to.
     * @param streamMigrator the custom stream migrator that does the migration.
     */
    void addStep(Version inputVersion, Version outputVersion,
            StreamMigrator streamMigrator);

    /**
     * Called to indicate that the current type should be closed.
     */
    void endType();

    /**
     * Returns the completed resource migrator.
     * <p>
     * This should be called once the target version has been set and all
     * required types have been added.
     *
     * @return the resource migrator built by this object.
     */
    ResourceMigrator getCompletedResourceMigrator();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 19-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
