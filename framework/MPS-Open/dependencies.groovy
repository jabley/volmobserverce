// =============================================================================
//     Standard Java API Jars
// =============================================================================

pom(id: "mail", org: "javax", name: "mail", rev: "1.3.2")
pom(id: "activation", org: "javax", name: "activation", rev: "1.0.2")
pom(id: "servlet-api", org: "javax", name: "servlet", rev: "2.4")

// =============================================================================
//     Volantis Repackaged 3rd Party Jars
// =============================================================================

collection(id: "volantis-xerces") {
    pom(org: "com.volantis.apache", name: "xerces", rev: "2.6.2")
}
pom(id: "our-httpclient", org: "our.apache", name: "commons.httpclient", rev: "2.0.2")
pom(id: "our-commons-logging", org: "our.apache", name: "commons.logging", rev: "1.0.2")

//
// =============================================================================
//     Volantis Modified 3rd Party Jars
// =============================================================================
pom(id: "volantis-jdom", org: "com.volantis.jdom", name: "api", rev: "0.9")

// =============================================================================
//     Apache 3rd Party Jars
// =============================================================================

pom(id: "commons-pool", org: "org.apache", name: "commons.pool", rev: "1.2")
pom(id: "commons-collections", org: "org.apache", name: "commons.collections", rev: "2.1")

// =============================================================================
//     Eclipse 3rd Party Jars
// =============================================================================

// =============================================================================
//     Other 3rd Party Jars
// =============================================================================
pom(id: "jaxen", org: "org.codehaus", name: "jaxen", rev: "1.1.1")
pom(id: "jdom", org: "org.jdom", name: "api", rev: "1.0")
pom(id: "jibx-run", org: "org.jibx", name: "api", rev: "1.1.5")
pom(id: "junit", org: "org.junit", name: "api", rev: "3.8.2")
pom(id: "junit.addons", org: "net.sourceforge", name: "junit.addons", rev: "1.4")
pom(id: "hsqldb", org: "org.hsqldb", name: "runtime", rev: "1.8.0.7")
collection(id:"smpp") {
  pom(id: "smpp-runtime", org: "org.smpp", name: "runtime", rev: "2.0")
  pom(id: "smpp-smscsim", org: "org.smpp", name: "smsc.simulator", rev: "2.0")
}

//
// =============================================================================
//     Build Collections
// =============================================================================
collection(id: "common-test-support") {
    ref("junit")
    ref("junit.addons")

    product("synergetics") {
        subsystem("testtools")
    }
}

collection(id: "volantis-mock-task") {
    product("testtools") {
        subsystem("mock-generator", id: "volantis-mock-generator")
    }
}
// =============================================================================
//     Custom Collections
// =============================================================================