initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                subsystem("styling")

                product("devices") {
                    subsystem("repository")
                }
            }

            subsystem("architecture")
            subsystem("charset")
            subsystem("common")
            subsystem("configuration")
            subsystem("cornerstone")
            subsystem("dom")
            subsystem("dom2theme")
            subsystem("localization")
            subsystem("migrate-api")
            subsystem("model")
            subsystem("project")
            subsystem("repository")
            subsystem("runtime-css")
            subsystem("service")

            product("pipeline") {
                subsystem("pipeline")
            }

            product("synergetics") {
                subsystem("cache")
                subsystem("cornerstone")
                subsystem("metadata")
                subsystem("net")
                subsystem("old-cache")
                subsystem("performance")
                subsystem("repository")
            }

            product("map") {
                subsystem("media-agent")
            }

            ref("activation")
            ref("commons-lang")
            ref("commons-httpclient")
            ref("mail")
            ref("servlet-api")
            ref("volantis-xerces")
            ref("jibx-run")

            // @todo remove and use "commons-httpclient" instead
            ref("our-httpclient")
        }

        impl {
            subsystem("client")
        }
    }
}

populateProject {
    project, module ->

    // Get a target to resolve the execution path for the volantis xerces
    // library.
    def resolveDependencyTarget = getTarget("product/resolve-dependency-path")

    def codeGenerators = [
            [
                    schemaPath: "marlin/src/marlin-cdm-internal.xsd",
                    className: "com.volantis.mcs.build.marlin.RuntimeParseMarinerSchema",
                    identity: "papi-sources",
            ],

            [
                    schemaPath: "Mariner-imdapi.xsd",
                    className: "com.volantis.mcs.build.marlin.ParseIMDAPISchema",
                    identity: "imdapi-sources",
            ],
    ]

    def subsystems = project.properties.subsystems;
    def subsystem = module.properties.subsystem;

    // Add the target to update the paths.
    module.getTarget("prepare-main-paths").required << module.addTarget() {
        codeGenerators.each {
            def outputDir = subsystem.builtDir.resolve(it.identity)
            subsystem["api"].javaSourcePath << outputDir
        }
    }

    // Add a dependency between this and the target that builds the generator
    // classes used by this.
    def generateSubsystem = subsystems["generator"];
    def generator = generateSubsystem.module;
    def prepareGenerator = generator.getTarget("prepare-impl")
    def generate = module.getTarget("generate-api")
    generate.depends << prepareGenerator

    generate.body = {
        target, original ->

        def xercesPath = resolveDependencyTarget.invoke("volantis-xerces");

        def architectureSubsystem = subsystems["architecture"];
        def architectureModule = architectureSubsystem.module;

        def invokeCodeGenerator = (Object) {
            schemaPath, className, identity ->

            def schemaFile = architectureModule.resolve("xml-schema/$schemaPath")
            def outputDir = subsystem.builtDir.resolve(identity)

            def uptodateFile = subsystem.builtDir.resolve("${identity}.uptodate");
            def uptodate = true;
            if (!uptodateFile.exists()) {
                uptodate = false;
            }
            if (schemaFile.lastModified() > uptodateFile.lastModified()) {
                uptodate = false;
            }

            def ant = module.antBuilder;
            if (!uptodate) {
                uptodateFile.parentFile.mkdirs();
                ant.touch(file: uptodateFile)
                def worked = false;
                try {
                    println("Generating code from schema file: " + schemaFile)
                    ant.java(classname: className,
                            failonerror: true) {
                        classpath {
                            generateSubsystem.mainPaths.execute.each {
                                pathelement(location: it)
                            }
                            xercesPath.each {
                                pathelement(location: it)
                            }
                        }
                        arg(value: schemaFile)
                        arg(value: outputDir)
                    }
                    worked = true;
                } finally {
                    if (!worked) {
                        uptodateFile.delete();
                    }
                }
            }

        }

        codeGenerators.each {
            invokeCodeGenerator.call(it.schemaPath, it.className, it.identity)
        }
    }
}
