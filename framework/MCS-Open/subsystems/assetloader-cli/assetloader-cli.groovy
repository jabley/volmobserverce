initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("repository")

            product("devices") {
                subsystem("api")
            }

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("repository")
            }

            ref("commons-cli")
        }
    }
}
