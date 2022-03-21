[![](https://github.com/noncat-lang/java-template-engine/actions/workflows/unit-test.yml/badge.svg)](https://github.com/noncat-lang/java-template-engine/actions/workflows/unit-test.yml?query=branch%3Amain)
[![](https://github.com/noncat-lang/java-template-engine/actions/workflows/semgrep.yml/badge.svg)](https://github.com/noncat-lang/java-template-engine/actions/workflows/semgrep.yml?query=branch%3Amain)
[![](https://github.com/noncat-lang/java-template-engine/actions/workflows/dependency-check.yml/badge.svg)](https://github.com/noncat-lang/java-template-engine/actions/workflows/dependency-check.yml?query=branch%3Amain)

# Java Template Engine

TODO

## Usage

TODO

### Format

```Java
Token token = Token.of("[a-zA-Z]+");
Template template = Template.of("Hello ${name}!")
    .withToken("name", token);

Map<String, String> values = Map.of("name", "World");
String output = template.format(values);

System.out.println(output);
```
```
Hello World!
```

### Parse

```Java
Token token = Token.of("[a-zA-Z]+");
Template template = Template.of("Hello ${name}!")
    .withToken("name", token);

Map<String, String> data = template.parse("Hello World!");

System.out.println(data);
```
```
{name=World}
```
