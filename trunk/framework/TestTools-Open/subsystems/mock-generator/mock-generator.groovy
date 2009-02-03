initializeProject {
    project, module ->

    dependencies {
        api {
            pom(org: "org.codehaus", name: "plexus.utils", rev: "1.0.4")
            pom(org: "org.apache", name: "ant", rev: "1.7.0")
            pom(org: "com.thoughtworks", name: "qdox", rev: "1.6.3")
        }
    }
}
