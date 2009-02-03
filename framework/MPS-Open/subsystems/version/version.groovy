import java.text.SimpleDateFormat

initializeProject {
    project, module ->

    dependencies {
        api {
            product("mcs") {
                subsystem("common")
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
        def formatted_date = new SimpleDateFormat("d-MMMM-yyyy hh:mm:ss aa",
                Locale.ENGLISH).format(date);

        def ant = module.antBuilder;
        def generatedFile = outputDir.resolve("com/volantis/mcs/application/MPSVersion.java")
        ant.copy(file: module.resolve("main/api/template/MPSVersion.java.src"),
                tofile: generatedFile, overwrite: true) {
            filterset(filtersfile: project.resolve("build-version.properties")) {
                filter(token: "build.date", value: formatted_date)
            }
        }
    }
}
