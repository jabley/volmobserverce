initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("repository")
            subsystem("servlet")

            product("devices") {
                subsystem("api")
                subsystem("repository")
            }

            product("synergetics") {
                subsystem("repository")
            }

            ref("volantis-xerces")
            ref("our-digester")
        }
    }
}
