import java.text.SimpleDateFormat

initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("runtime")
            }

            ref("jaxen")
            ref("regexp")
            ref("volantis-jdom")
            ref("volantis-xerces")
        }

        tests {
            subsystem("cornerstone")
            subsystem("repository")

            product("synergetics") {
                subsystem("metadata")
            }

            product("devices") {
                subsystem("api")
            }
        }
    }
}

populateProject {
    project, module ->

    def subsystem = module.properties.subsystem;
    def outputDir = subsystem.builtDir.resolve("generate-version");

    // Add the target to update the paths.
    module.getTarget("prepare-main-paths").required << module.addTarget() {
        subsystem["api"].javaSourcePath << outputDir;
    }

    def generate = module.getTarget("generate-api")
    generate.body = {
        target, original ->

        // Invoke the original body.
        original.call();

        def date = new Date();
        def formatted = new SimpleDateFormat("d-MMMM-yyyy hh:mm:ss aa",
                Locale.ENGLISH).format(date);
        def properties = new Properties();
        properties.load(project.resolve("build-version.properties").newInputStream());

        def ant = module.antBuilder;
        def generatedFile = outputDir.resolve("com/volantis/mcs/utilities/VolantisVersion.java")
        ant.copy(file: module.resolve("main/api/template/VolantisVersion.java.src"),
                tofile: generatedFile, overwrite: true)
        ant.replace(file: generatedFile) {
            replacefilter(token: "DateTOKEN", value: formatted);
            replacefilter(token: "NameTOKEN", value: properties."build.name");
            replacefilter(token: "MajorTOKEN", value: properties."build.version.major");
            replacefilter(token: "MinorTOKEN", value: properties."build.version.minor");
            replacefilter(token: "RevisionTOKEN", value: properties."build.version.revision");
            replacefilter(token: "PatchLevelTOKEN", value: properties."build.version.patchlevel");
        }
    }
}
