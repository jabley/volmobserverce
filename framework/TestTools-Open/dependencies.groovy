collection(id: "common-test-support") {
    pom(org: "org.junit", name: "api", rev: "3.8.2")
    pom(org: "net.sourceforge", name: "junit.addons", rev: "1.4")
}

collection(id: "volantis-mock-task") {
    subsystem("mock-generator")
}
