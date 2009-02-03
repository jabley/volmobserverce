initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            ref("jibx-run")
            ref("volantis-xerces")
            ref("volantis-xalan")
        }

        tests {
            subsystem("testtools")
        }
    }
}
