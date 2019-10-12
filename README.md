<img src="https://github.com/vzurauskas/nereides-jackson/blob/master/logo.svg" height="100px" />


[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/vzurauskas/nereides-jackson)](http://www.rultor.com/b/vzurauskas/nereides-jackson)

<!-- https://github.com/vzurauskas/nereides-jackson/issues/29 -->
<!-- ![nullfree status](https://iwillfailyou.com/nullfree/vzurauskas/nereides-jackson) -->
[![CircleCI](https://circleci.com/gh/vzurauskas/nereides-jackson/tree/master.svg?style=svg)](https://circleci.com/gh/vzurauskas/nereides-jackson/tree/master)
[![Codecov branch](https://img.shields.io/codecov/c/github/vzurauskas/nereides-jackson/master)](https://codecov.io/gh/vzurauskas/nereides-jackson)
[![Hits-of-Code](https://hitsofcode.com/github/vzurauskas/nereides-jackson)](https://hitsofcode.com/view/github/vzurauskas/nereides-jackson)
[![Maven Central](https://img.shields.io/maven-central/v/com.vzurauskas.nereides/nereides-jackson)](https://search.maven.org/search?q=a:nereides-jackson)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/vzurauskas/nereides-jackson/blob/master/LICENSE)

Nereides* for Jackson is an object oriented JSON library wrapper for [jackson-databind](https://github.com/FasterXML/jackson-databind). It allows developers to work with JSONs in a purely object oriented way: everything is instantiated via constructors, there are no static methods, no nulls, no "mappers" or "factories"and no configuration files. Most importantly, the core `Json` interface lends itself to easy custom implementations, making Nereides very extendable. 

*(Nereides are the sea nymphs who guided Jason's ship safely through the Wandering Rocks in his quest for the Golden Fleece.)

# Maven dependencies
```
<dependency>
    <groupId>com.vzurauskas.nereides</groupId>
    <artifactId>nereides-jackson</artifactId>
    <version>0.0.6-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.9.2</version>
</dependency>
```

# Simple usage
## Create new `Json` object
```
// From String:
String jsonAsString = "{\"nymph\": \"nereid\"}";
Json json = new Json.Of(jsonAsString);

// From InputStream:
InputStream stream = new ByteArrayInputStream(jsonAsString.getBytes());
json = new Json.Of(stream);

// From Jackson's JsonNode:
JsonNode node = new ObjectMapper().readTree(jsonAsString);
json = new Json.Of(node);
```

## `SmartJson`
Once we have the `Json` object, to use it in various ways, the [Smart Object pattern](https://www.yegor256.com/2016/04/26/why-inputstream-design-is-wrong.html) is employed.
