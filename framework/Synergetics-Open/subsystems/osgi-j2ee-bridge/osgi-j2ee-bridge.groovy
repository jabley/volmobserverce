initializeProject {
    project, module ->

    dependencies {
        impl {
            subsystem("osgi-boot")
            subsystem("osgi-framework")
            subsystem("runtime")

            ref("osgi")
            ref("osgi-services")
            ref("servlet-api")
        }
    }
}
