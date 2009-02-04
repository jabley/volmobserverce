initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                ref("our-digester")
            }

            subsystem("localization")
            subsystem("repository")

            product("pipeline") {
                subsystem("pipeline")
            }

            product("synergetics") {
                subsystem("cornerstone")
            }

            ref("jibx-run")
            ref("our-beanutils")
            ref("our-commons-logging")
        }
    }
}
