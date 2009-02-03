initializeProject {
    project, module ->

    dependencies {
        impl {
            subsystem("localization")
        }

        tests {
            subsystem("testtools")
            ref("apache-xerces")
        }
    }
}
