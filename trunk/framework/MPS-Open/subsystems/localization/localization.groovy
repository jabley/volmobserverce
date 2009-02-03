initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                product("synergetics") {
                    subsystem("localization")
                }
            }
        }

    }
}
