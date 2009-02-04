initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("architecture")
            subsystem("common")
            subsystem("cornerstone")
            subsystem("eclipse-common")
            subsystem("eclipse-controls")
            subsystem("eclipse-validation")
            subsystem("localization")
            subsystem("interaction")
            subsystem("repository")

            product("devices") {
                subsystem("api")
                subsystem("repository")
            }

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("repository")
                subsystem("runtime")
            }

            ref("eclipse-core-resources")
            ref("eclipse-core-runtime")
            ref("eclipse-jface")
            ref("eclipse-swt")
            ref("eclipse-ui-workbench")
            ref("eclipse-ui-workbench-texteditor")
            ref("eclipse-ui-forms")
            ref("eclipse-ui-editors")
            ref("eclipse-ui-ide")
            ref("eclipse-ui-views")

            ref("regexp")

            ref("volantis-jdom")
            ref("volantis-xerces")
        }
    }
}
