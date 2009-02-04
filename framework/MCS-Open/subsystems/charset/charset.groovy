initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            ref("volantis-xerces")
            ref("our-digester")
        }
    }
}
