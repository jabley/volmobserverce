initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("runtime")
        }

        impl {
            ref("osgi")
        }

        tests {
            subsystem("testtools")
        }
    }
}
