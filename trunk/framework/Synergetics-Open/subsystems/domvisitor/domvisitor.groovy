initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            ref("volantis-jdom")
        }

        support {
            subsystem("testtools")
        }
    }
}
