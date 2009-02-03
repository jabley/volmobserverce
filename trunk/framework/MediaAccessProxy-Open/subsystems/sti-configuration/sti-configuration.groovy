initializeProject {
    project, module ->

    dependencies {
        api {
            product("synergetics") {
                subsystem("runtime")
            }
        }

        impl {
            subsystem("localization")
	}
    }
}
