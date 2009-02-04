initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("dom")
            subsystem("repository")
            subsystem("styling")

            product("synergetics") {
                subsystem("runtime")
            }
        }

        impl {
            subsystem("cornerstone")
            subsystem("localization")
        }
    }
}
