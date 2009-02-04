initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("configuration")
            subsystem("repository")
            subsystem("runtime")
            subsystem("servlet")
            subsystem("testtools")
            subsystem("xml-validation")

            product("synergetics") {
                subsystem("repository")
                subsystem("testtools")
            }

            ref("servlet-api")
            ref("volantis-jdom")
        }
    }
}
