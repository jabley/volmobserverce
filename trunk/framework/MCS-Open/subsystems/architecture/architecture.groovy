initializeProject {
    project, module ->


    dependencies {
        api {
            product("devices") {
                subsystem("api")
            }

            product("synergetics") {
                subsystem("cornerstone")
            }
        }
    }
}

populateProject {
    project, module ->

    def tasks = project.properties.tasks;

    def subsystem = module.properties.subsystem

    def compileSchemataTarget = module.addTarget("compile-schemata",
            description: "Compile the architectural schemata",
            visibility: "external") {
        target ->

        def ant = module.antBuilder;

        // Make sure that the transform operation is registered.
        tasks.transform.invoke(ant);

        def api = subsystem["api"]
        def schemaCatalog = api.compileOutputDir.resolve(
                "com/volantis/schema")

        def intermediateDir = subsystem.builtDir.resolve("xml-schema");

        def xmlSchemaDir = module.resolve("xml-schema");

        def twoStageTransformations = []

        def marlinXSDs = xmlSchemaDir.resolve("marlin/src").list();
        def pattern =  ~/(.*?)(-internal)?(\.xsd)/
        marlinXSDs.each {
            def matcher = pattern.matcher(it)
            if (matcher.matches()) {
                twoStageTransformations << [
                        name: matcher.group(1),
                        srcPath: "marlin",
                        destPath: "marlin/v3.0",
                        xsltPath: "GeneratePublicSchemaCore.xsl"
                ]
            }
        }
        
        twoStageTransformations << [
                name: "websphere/mcs-integration",
                srcPath: "marlin",
                destPath: "marlin/v3.0/websphere",
                xsltPath: "GeneratePublicSchemaCore.xsl"
        ]

        // Copy the repository schemata to the output directory.
        def legacy = [
                [src: "2-7-1", dst: "v2.7.1"],
                [src: "2-9-1", dst: "v2.9.1"],
                [src: "3-0-0", dst: "v3.0"],
        ]
        legacy.each {
            old ->
            ant.copy(todir: schemaCatalog.resolve(old.dst)) {
                fileset(dir: xmlSchemaDir.resolve("repository/${old.src}")) {
                    include(name: "*.xsd")
                }
            }
        }

        // Add new style repository transformations
        ["2005/09", "2005/12", "2006/02"].each {
            twoStageTransformations << [
                    name: "marlin-lpdm",
                    srcPath: "repository/$it",
                    destPath: it,
                    xsltPath: "GeneratePublicLPDMSchema.xsl"
            ] << [
                    name: "marlin-rpdm",
                    srcPath: "repository/$it",
                    destPath: it,
                    xsltPath: "GeneratePublicRPDMSchema.xsl"
            ]
        }

        // Add project transformation
        twoStageTransformations << [
                name: "mcs-project",
                srcPath: "project/3-5",
                xsltPath: "BuildPublicProjectSchema.xsl"
        ]

        // Add the configuration transformation
        twoStageTransformations << [
                name: "mcs-config",
                srcPath: "config",
                destPath: "config/v3.5",
                xsltPath: "GenerateConfigurationSchema.xsl"
        ] << [
                name: "mss-config",
                srcPath: "config",
                destPath: "config/v3.5",
                xsltPath: "GenerateConfigurationSchema.xsl"
        ]

        // Add XDIME CP transformations.
        def destPath = "2004/06/xdime-cp"
        def srcPath = "xdime-cp"
        def commonPath = "xdime-common"
        twoStageTransformations << [
                name: "xml",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXMLSchema.xsl"
        ] << [
                name: "xforms",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXDIMECPXFormsSchema.xsl"
        ] << [
                name: "xml-events",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXMLEventsSchema.xsl"
        ] << [
                name: "xhtml2",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: xmlSchemaDir.resolve("xdime-common/transforms/GeneratePublicXHTML2Schema.xsl")
        ] << [
                name: "diselect",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicDISelectSchema.xsl"
        ] << [
                name: "xdime-cp",
                srcPath: srcPath,
                destPath: destPath,
        ] << [
                name: "xdime-cp-mcs",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: xmlSchemaDir.resolve("xdime-common/transforms/GeneratePublicXDIMEMCSSchema.xsl")
        ] << [
                name: "xdime-cp-si",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXDIMECPSISchema.xsl"
        ]

        // Add XDIME 2 transformations
        destPath = "2006/01/xdime2"
        srcPath = "xdime2"
        twoStageTransformations << [
                name: "xml",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXMLSchema.xsl"
        ] << [
                name: "xforms",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXDIME2XFormsSchema.xsl"
        ] << [
                name: "xml-events",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXMLEventsSchema.xsl"
        ] << [
                name: "xhtml2",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: xmlSchemaDir.resolve("xdime-common/transforms/GeneratePublicXHTML2Schema.xsl")
        ] << [
                name: "diselect",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicDISelectSchema.xsl"
        ] << [
                name: "widgets",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicWidgetsSchema.xsl"
        ] << [
                name: "widgets-response",
                srcPath: commonPath,
                destPath: destPath,
                xsltPath: "GeneratePublicWidgetsResponseSchema.xsl"
        ] << [
                name: "xdime2",
                srcPath: srcPath,
                destPath: destPath,
        ] << [
                name: "xdime2-mcs",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: xmlSchemaDir.resolve("xdime-common/transforms/GeneratePublicXDIMEMCSSchema.xsl")
        ] << [
                name: "xdime2-si",
                srcPath: srcPath,
                destPath: destPath,
                xsltPath: "GeneratePublicXDIME2SISchema.xsl"
        ]

        twoStageTransformations.each {
            info ->

            def srcDir = xmlSchemaDir.resolve(info.srcPath);
            def dstPath = info.destPath == null ? info.srcPath : info.destPath;
            def destDir = schemaCatalog.resolve(dstPath);
            def buildDir = intermediateDir.resolve(dstPath);

            do2StageSchemaTransformation(ant,
                    info.name, srcDir, info.xsltPath, buildDir, destDir)
        }

        def themesBuiltDir = subsystem.builtDir.resolve("themes")
        themesBuiltDir.mkdirs();
        
        def themesDir = xmlSchemaDir.resolve("repository/2006/02/themes")
        def themeDefinitions = themesDir.resolve("themePropertyDefinitions.xml")

        // Generate the theme schema by combining the architecture definitions
        // with the engineering annotations using the GenerateMarinerTheme.xsl.
        ant.transform(file: themeDefinitions,
                tofile: themesBuiltDir.resolve("Mariner-Theme.xsd"),
                xslt: module.resolve("generator/com/volantis/mcs/build/themes/GenerateMarinerTheme.xsl"))

        // Generate the theme structure XML that can be used to validate the
        // structure of a style value.
        ant.transform(file: themeDefinitions,
                tofile: themesBuiltDir.resolve("ThemeStructure.xml"),
                xslt: module.resolve("generator/com/volantis/mcs/build/themes/GenerateThemeStructure.xsl"))

        // Copy the current config schema into the source.
        ant.copy(file: schemaCatalog.resolve("config/v3.5/mcs-config.xsd"),
                tofile: api.compileOutputDir.resolve("com/volantis/mcs/runtime/configuration/xml/mcs-config.xsd"))
    }

    def compile = module.getTarget("compile-api");
    compile.depends << compileSchemataTarget
}

private def do2StageSchemaTransformation(ant, schemaName, srcDir, xslt,
                                         buildDir, destDir) {


    def transformsDir = srcDir.resolve("transforms");
    def srcSchema = srcDir.resolve("src/${schemaName}-internal.xsd")
    if (!srcSchema.exists()) {
        srcSchema = srcDir.resolve("src/${schemaName}.xsd")
    }

    def finalSchema
    if (xslt == null) {
        finalSchema = srcSchema
    } else {
        def invalidSchema = buildDir.resolve("${schemaName}-invalid.xsd")
        invalidSchema.parentFile.mkdirs();
        ant.transform(file: srcSchema,
                tofile: invalidSchema, xslt: transformsDir.resolve(xslt))

        finalSchema = buildDir.resolve("${schemaName}.xsd")
        finalSchema.parentFile.mkdirs();
        ant.transform(file: invalidSchema,
                tofile: finalSchema,
                xslt: "xml-schema/utilities/transforms/PostProcessSchema.xsl")
    }

    destDir.mkdirs();
    ant.copy(file: finalSchema, todir: destDir)
}
