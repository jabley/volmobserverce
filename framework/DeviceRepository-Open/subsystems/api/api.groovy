initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                product("synergetics") {
                    subsystem("runtime")
                    subsystem("metadata")
                }
            }
        }
    }
}
