initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            product("devices") {
                subsystem("api")
            }

            product("synergetics") {
                subsystem("runtime")
            }
        }
    }
}
