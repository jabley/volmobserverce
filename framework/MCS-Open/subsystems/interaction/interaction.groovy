initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                subsystem("model")
            }

            subsystem("localization")

            product("synergetics") {
                subsystem("runtime")
            }
        }
    }
}
