// =============================================================================
//     Standard Java API Jars
// =============================================================================

pom(id: "mail", org: "javax", name: "mail", rev: "1.3.2")
pom(id: "activation", org: "javax", name: "activation", rev: "1.0.2")
pom(id: "osgi", org: "org.eclipse", name: "equinox.osgi", rev: "3.3.1.1")
pom(id: "osgi-services", org: "org.eclipse", name: "equinox.osgi.services", rev: "3.1.200")

// =============================================================================
//     Volantis Repackaged 3rd Party Jars
// =============================================================================

pom(id: "volantis-xerces", org: "com.volantis.apache", name: "xerces", rev: "2.6.2")
pom(id: "our-httpclient", org: "our.apache", name: "commons.httpclient", rev: "2.0.2")

// =============================================================================
//     Volantis Modified 3rd Party Jars
// =============================================================================

pom(id: "volantis-jdom", org: "com.volantis.jdom", name: "api", rev: "0.9")

// =============================================================================
//     Other 3rd Party Jars
// =============================================================================

pom(id: "jibx-run", org: "org.jibx", name: "api", rev: "1.1.5")
pom(id: "junit", org: "org.junit", name: "api", rev: "3.8.2")
pom(id: "junit.addons", org: "net.sourceforge", name: "junit.addons", rev: "1.4")
pom(id: "hsqldb", org: "org.hsqldb", name: "runtime", rev: "1.8.0.7")

// =============================================================================
//     Build Collections
// =============================================================================

collection(id: "common-test-support") {
    ref("junit")
    ref("junit.addons")

    product("testtools") {
        subsystem("mock", id: "volantis-mock")
    }
}

collection(id: "volantis-mock-task") {
    product("testtools") {
        subsystem("mock-generator", id: "volantis-mock-generator")
    }
}
