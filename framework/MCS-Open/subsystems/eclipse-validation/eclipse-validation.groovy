initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("eclipse-common")
            subsystem("repository")

            ref("eclipse-core-resources")
            ref("eclipse-core-runtime")
            ref("eclipse-swt")

            ref("log4j")
        }
    }
}

populateProject {
    project, module ->

    module.getTarget("validate").required << addTarget("unpack-eclipse-native") {

        def productModule = project.getModule("product")
        def unpackTarget = productModule.getTarget("unpack-eclipse-native")
        unpackTarget.invoke(module);
    }
}
