<img src="https://github.com/vzurauskas/nereides-jackson/blob/master/logo.svg" height="100px" />


[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/vzurauskas/nereides-jackson)](http://www.rultor.com/b/vzurauskas/nereides-jackson)

<!-- https://github.com/vzurauskas/nereides-jackson/issues/29 -->
<!-- ![nullfree status](https://iwillfailyou.com/nullfree/vzurauskas/nereides-jackson) -->
[![CircleCI](https://circleci.com/gh/vzurauskas/nereides-jackson/tree/master.svg?style=svg)](https://circleci.com/gh/vzurauskas/nereides-jackson/tree/master)
[![Codecov](https://img.shields.io/codecov/c/github/vzurauskas/nereides-jackson/master)](https://codecov.io/gh/vzurauskas/nereides-jackson)
[![Hits-of-Code](https://hitsofcode.com/github/vzurauskas/nereides-jackson)](https://hitsofcode.com/view/github/vzurauskas/nereides-jackson)
[![Maven Central](https://img.shields.io/maven-central/v/com.vzurauskas.nereides/nereides-jackson)](https://search.maven.org/search?q=a:nereides-jackson)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/vzurauskas/nereides-jackson/blob/master/LICENSE)

Nereid* for Jackson is an object oriented JSON library wrapper for [jackson-databind](https://github.com/FasterXML/jackson-databind) It allows developers to work with JSON documents in a purely object oriented way: everything is instantiated via constructors, there are no static methods, no nulls and no "mappers" or "builders". Most importantly, the core `Json` interface lends itself to easy custom implementations, making Nereides very extensible. 

*(Nereides are the sea nymphs who guided Jason's ship safely through the Wandering Rocks in his quest for the Golden Fleece.)

Available Nereides:  
-- [Nereid for Jackson](https://github.com/vzurauskas/nereides-jackson) (recommended)  
-- [Nereid for javax.json](https://github.com/vzurauskas/nereides-javax)

## Maven dependency
```
<dependency>
    <groupId>com.vzurauskas.nereides</groupId>
    <artifactId>nereides-jackson</artifactId>
    <version>1.1.1</version>
</dependency>
```

## Simple usage
### Create new `Json` object
```java
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

### SmartJson
Once we have the `Json` object, to use it in various ways, the [Smart Object pattern](https://www.yegor256.com/2016/04/26/why-inputstream-design-is-wrong.html) is employed.
```java
// Convert it to String:
String textual = new SmartJson(json).textual();

// Convert it to pretty formatted String:
String pretty = new SmartJson(json).pretty();

// Convert it to byte array:
byte[] bytes = new SmartJson(json).byteArray();

// Get a String field value:
Optional<String> leaf = new SmartJson(json).leaf("nymph");

// Get a deeply nested Json:
SmartJson nested = new SmartJson(json).at("/path/to/nested/json");
```

### MutableJson
While the main purpose of this library is to enable making custom implementations of the `Json` interface (see more on that below), if you need to quickly assemble a `Json` by hand, `MutableJson` can be used. This API has a very declarative notation.
```java
Json json = new MutableJson().with(
    "ocean",
    new MutableJson().with(
        "nereid1",
        new MutableJson()
            .with("name", "Thetis")
            .with("hair", "black")
    ).with(
        "nereid2",
        new MutableJson()
            .with("name", "Actaea")
            .with("hair", "blonde")
    )
    .with("stormy", true)
);
System.out.println(new SmartJson(json).pretty());
```
The code above would print this:
```json
{
  "ocean" : {
    "nereid1" : {
      "name" : "Thetis",
      "hair" : "black"
    },
    "nereid2" : {
      "name" : "Actaea",
      "hair" : "blonde"
    },
    "stormy" : true
  }
}
```

## Custom implementations
If you have an object which needs to be able to display itself as JSON, sometimes it might be useful to just treat it as a JSON to begin with. In that case that object will have to implement a JSON interface. In most (all?) other libraries, JSON interfaces are huge, making it very difficult to implement them. With Nereides, all you need to do is provide the JSON representation in a stream of bytes. The easiest way to do this is to encapsulate another `Json` and delegate to it, or construct one on the spot.

Let's say we have a bank account which we need to display as JSON. We need its IBAN, nickname and balance, which (to make this a less trivial example) we get from another service. One way to implement it is this:
```java
public final class BankAccount implements Json {
    private final String iban;
    private final String nickname;
    private final TransactionHistory transactions;

    // Constructor...

    public void makePayment(double amount) { /* Implementation... */ }
    // Other public methods...

    @Override
    public InputStream bytes() {
        return new MutableJson()
            .with("iban", iban)
            .with("nickname", nickname)
            .with("balance", transactions.balance(iban))
            .bytes();
    }
}
```
Even simpler way is to extend the `JsonEnvelope`, then you don't even need to implement `bytes()`:
```java
public final class BankAccount extends JsonEnvelope {
    public BankAccount(String iban, String nickname, TransactionHistory transactions) {
        super(new MutableJson()
            .with("iban", iban)
            .with("nickname", nickname)
            .with("balance", transactions.balance(iban))
        );
    }

    public void makePayment(double amount) { /* Implementation... */ }
    // Other public methods...
}
```
We can then make an HTTP response directly, e.g. with [Spring](https://spring.io/):
```java         
return new ResponseEntity<>(
    new SmartJson(
        new BankAccount(iban, nickname, transactions)
    ).byteArray(),
    HttpStatus.OK
);
```
...or with [Takes](https://github.com/yegor256/takes):
```java
return new RsWithType(
    new RsWithStatus(
        new RsWithBody(
            new BankAccount(iban, nickname, transactions).bytes()
        ),
        200
    ),
    "application/json"
);
```
...or insert it in some JSON datastore:
```java
accounts.insert(new BankAccount(iban, nickname));
```

...or compose it within a larger JSON:
```java
Json accounts = new MutableJson()
    .with("name", "John")
    .with("surname", "Smith")
    .with("account", new BankAccount(iban, nickname));
```

## Additional functionality
If available functionality in the current version of Nereid is not enough, the developer can always fall back to jackson-databind. Convert `Json` to `ObjectNode`, do what you need with it, and construct a new `Json`.
```java
ObjectNode node = new SmartJson(json).objectNode();
// Do stuff with node using Jackson's API.
Json updated = new Json.Of(node);
```

## Contributing
To contribute:
1. Fork this repository.
2. Clone your fork.
3. Create a branch from `master`.
4. Make changes in your branch.
5. Make sure the build passes with `mvn clean install -Pwall`. ("wall" is Maven profile with quality wall)
6. Commit and push your changes to your fork.
7. Make a pull request from your fork to this repository.

You can read more about contributing in GitHub in [this article](https://github.com/firstcontributions/first-contributions).
