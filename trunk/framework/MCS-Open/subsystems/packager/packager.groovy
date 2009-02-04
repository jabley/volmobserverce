initializeProject {
    project, module ->

    // Insert dependencies here.
    dependencies {
        api {
          subsystem("localization")
        }

        impl {
          ref("ant")
          ref("commons-cli")
          ref("commons-httpclient")
          ref("commons-io")
          ref("volantis-jdom")
        }

        support {

        }

        tests {

        }
    }
}

populateProject {
    project, module ->

    // Insert custom targets here.
}
