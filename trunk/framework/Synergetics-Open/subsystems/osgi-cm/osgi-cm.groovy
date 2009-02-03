initializeProject {
    project, module ->

    dependencies {
        impl {
            ref("osgi")
            ref("osgi-services")
        }

        tests {
            subsystem("mock")
            subsystem("testtools")
        }
    }
}
