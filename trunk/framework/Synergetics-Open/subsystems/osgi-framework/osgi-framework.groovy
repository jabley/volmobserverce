initializeProject {
    project, module ->

    dependencies {
        api {
            ref("osgi")
        }

        impl {
            subsystem("osgi-boot")
        }
    }
}
