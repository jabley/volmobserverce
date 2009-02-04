initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("dom")
            subsystem("localization")
            subsystem("runtime")
            subsystem("xml-validation")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("runtime")
            }
        }

        impl {

            subsystem("architecture")
            subsystem("common")
            subsystem("cornerstone")

            product("map") {
                subsystem("media-agent")
                subsystem("common")
            }

            product("pipeline") {
                subsystem("pipeline")
            }

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("net")
                subsystem("repository")
            }
        }

        tests {
            subsystem("dom2theme")
        }
    }
}
