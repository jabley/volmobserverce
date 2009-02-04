initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("cornerstone")
            subsystem("dynamo-core")
            subsystem("localization")
            subsystem("runtime")
            subsystem("servlet")

            pom(org: "com.atg", name: "dspjsp", rev: "5.6.1")
            ref("servlet-api")
            ref("jsp-api")
        }

        support {
            subsystem("testtools-runtime")
        }
    }
}

populateProject {
    project, module ->

    def subsystem = module.properties.subsystem;
    def outputDir = subsystem.builtDir.resolve("main/api/templates");

    // Add the target to update the paths.
    module.getTarget("prepare-main-paths").required << module.addTarget() {
        subsystem["api"].javaSourcePath << outputDir;
    }

    module.getTarget("generate-api").body = {
        target, original ->

        def templatesDir = module.resolve("main/api/templates");
        outputDir.mkdirs();

        def ant = module.antBuilder;

        ant.copy(todir: outputDir) {
            fileset(dir: templatesDir) {
                include(name: "**/*.java.template")
            }

            mapper(type: "glob", from: "*.template", to: "*")

            filterset {
                filter(token: "DYNAMO-5.6.1-TEMPLATE-START",
                        value: "//DYNAMO-5.6.1-TEMPLATE-START")
                filter(token: "DYNAMO-5.6.1-TEMPLATE-END",
                        value: "//DYNAMO-5.6.1-TEMPLATE-END")
                filter(token: "DYNAMO-7.0-TEMPLATE-START",
                        value: "/* DYNAMO-7.0-TEMPLATE-START")
                filter(token: "DYNAMO-7.0-TEMPLATE-END",
                        value: "DYNAMO-7.0-TEMPLATE-END */")
            }
        }
    }
}
