initializeProject {
    project, module ->

    dependencies {
        api {

            // Declare that the API of this subsystem uses the synergetics
            // localization subsystem, which means that any subsystem that
            // depends on this will also depend on synergetics localization.
            // The reason for doing this is because it is practically
            // impossible to use this subsystem without also using synergetics
            // localization subsystem.
            uses {
                product("synergetics") {
                    subsystem("localization")
                }
            }
        }
    }
}
