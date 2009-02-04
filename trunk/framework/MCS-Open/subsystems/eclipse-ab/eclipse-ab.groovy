initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("cornerstone")
            subsystem("eclipse-common")
            subsystem("eclipse-builder")
            subsystem("eclipse-controls")
            subsystem("eclipse-validation")
            subsystem("interaction")
            subsystem("localization")
            subsystem("repository")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("repository")
                subsystem("xml-utils")
            }

            ref("eclipse-core-resources")
            ref("eclipse-core-runtime")
            ref("eclipse-jface")
            ref("eclipse-jface-text")
            ref("eclipse-swt")
            ref("eclipse-ui-workbench")
            ref("eclipse-ui-workbench-texteditor")
            ref("eclipse-ui-forms")
            ref("eclipse-ui-editors")
            ref("eclipse-ui-ide")
            ref("eclipse-ui-views")
            ref("eclipse-text")
            ref("eclipse-search")

            ref("regexp")

            ref("volantis-jdom")
            ref("volantis-xerces")
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
