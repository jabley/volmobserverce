// =============================================================================
//     Standard Java API Jars
// =============================================================================

pom(id: "servlet-api", org: "javax", name: "servlet", rev: "2.3")
jar (id: "jsp-api", org: "javax", name: "servlet.jsp", rev: "1.2")

// =============================================================================
//     Volantis Repackaged 3rd Party Jars
// =============================================================================

pom(id: "volantis-xerces", org: "com.volantis.apache", name: "xerces", rev: "2.6.2")
pom(id: "volantis-xalan", org: "com.volantis.apache", name: "xalan", rev: "2.6.0")

// =============================================================================
//     Volantis Modified 3rd Party Jars
// =============================================================================

// =============================================================================
//     Apache 3rd Party Jars
// =============================================================================

pom(id: "commons-httpclient", org: "org.apache", name: "commons.httpclient", rev: "3.0.1")
pom(id: "log4j", org: "org.apache", name: "log4j", rev: "1.2.8")

// =============================================================================
//     Other 3rd Party Jars
// =============================================================================

pom(id: "junit", org: "org.junit", name: "api", rev: "3.8.2")
pom(id: "junit.addons", org: "net.sourceforge", name: "junit.addons", rev: "1.4")

// =============================================================================
//     Commercial 3rd Party Jars
// =============================================================================

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
