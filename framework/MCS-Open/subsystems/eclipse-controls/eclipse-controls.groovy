initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("cornerstone")
            subsystem("eclipse-common")
            subsystem("eclipse-validation")
            subsystem("localization")
            subsystem("repository")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("runtime")
            }

            ref("eclipse-core-resources")
            ref("eclipse-core-runtime")
            ref("eclipse-jface")
            ref("eclipse-swt")
            ref("eclipse-ui-workbench")
            ref("eclipse-ui-forms")

            ref("jai")
            ref("jai-codec")

            ref("volantis-jdom")
        }

        tests {
            subsystem("testtools")
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
