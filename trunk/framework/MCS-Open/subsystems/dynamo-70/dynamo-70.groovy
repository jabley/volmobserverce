initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("cornerstone")
            subsystem("localization")
            subsystem("runtime")
            subsystem("servlet")

            pom(org: "com.atg", name: "dspjsp", rev: "7.0")

            ref("servlet-api")
            ref("jsp-api")
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

        def dynamo561Dir = module.resolve("../dynamo-561")
        def templatesDir = dynamo561Dir.resolve("main/api/templates");

        outputDir.mkdirs();

        def ant = module.antBuilder;

        ant.copy(todir: outputDir) {
            fileset(dir: templatesDir) {
                include(name: "**/*.java.template")
            }

            mapper(type: "glob", from: "*.template", to: "*")

            filterset {
                filter(token: "DYNAMO-5.6.1-TEMPLATE-START",
                        value: "/* DYNAMO-5.6.1-TEMPLATE-START")
                filter(token: "DYNAMO-5.6.1-TEMPLATE-END",
                        value: "DYNAMO-5.6.1-TEMPLATE-END */")
                filter(token: "DYNAMO-7.0-TEMPLATE-START",
                        value: "//DYNAMO-7.0-TEMPLATE-START")
                filter(token: "DYNAMO-7.0-TEMPLATE-END",
                        value: "//DYNAMO-7.0-TEMPLATE-END */")
            }
        }

        ant.copy(todir: outputDir) {
            fileset(dir: dynamo561Dir.resolve("main/api/java")) {
                include(name: "**/*.java")
            }
        }
    }
}
