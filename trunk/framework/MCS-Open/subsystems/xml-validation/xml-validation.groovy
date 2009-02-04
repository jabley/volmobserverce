initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            product("synergetics") {
                subsystem("runtime")
            }
        }

        impl {

            product("synergetics") {
                subsystem("cornerstone")
            }
        }
    }
}
