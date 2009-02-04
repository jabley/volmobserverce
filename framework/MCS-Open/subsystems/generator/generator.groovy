initializeProject {
    project, module ->

    dependencies {
        api {
            ref("volantis-jdom");

            def javaHome = new File(System.getProperty("java.home"));

            file(location: javaHome.resolve("../lib/tools.jar"))
        }
    }
}
