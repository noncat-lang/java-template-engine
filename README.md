<!-- markdown-link-check-disable -->
[![Maven Central](https://img.shields.io/maven-central/v/io.github.noncat-lang/java-template-engine)](https://search.maven.org/artifact/io.github.noncat-lang/java-template-engine)
[![Unit Test](https://github.com/noncat-lang/java-template-engine/actions/workflows/unit-test.yml/badge.svg)](https://github.com/noncat-lang/java-template-engine/actions/workflows/unit-test.yml?query=branch%3Amain)
[![Semgrep](https://github.com/noncat-lang/java-template-engine/actions/workflows/semgrep.yml/badge.svg)](https://github.com/noncat-lang/java-template-engine/actions/workflows/semgrep.yml?query=branch%3Amain)
[![Dependency Check](https://github.com/noncat-lang/java-template-engine/actions/workflows/dependency-check.yml/badge.svg)](https://github.com/noncat-lang/java-template-engine/actions/workflows/dependency-check.yml?query=branch%3Amain)
[![Markdown Link Check](https://github.com/noncat-lang/java-template-engine/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/noncat-lang/java-template-engine/actions/workflows/markdown-link-check.yml?query=branch%3Amain)
<!-- markdown-link-check-enable -->
# Java Template Engine

This repository demonstrates one concept of [Noncat](https://github.com/noncat-lang/noncat). Instead of introducing a new programming language, we chose to use the Java programming language for this proof of concept.

Java Template Engine uses the well known idea of templates to solve the not well known problem of defining languages for program input and output.
By defining these languages programs are able to reject malicious input and create well formed output to prevent input based vulnerabilities.
Java Template Engine helps you to do exactly this without the need to write a grammar.

Using Java Template Engine one can only define regular languages, hence it is not possible to define all language features of context free languages, e.g., HTML or SQL.


## Usage

TODO

### Format / Unparse
```Java
createOutput("new,World");
```
```Java
String createOutput(String name) {
  Token token = Token.of("[a-zA-Z ]+").withEncoding(Encoding.of(",", " "));
  Template template = Template.of("Hello ${name}!")
      .withToken("name", token);

  Values values = Values.of("name", name);
  return template.format(values);
}
```
```
Hello new World!
```

### Parse

```Java
handleInput("Hello new World!");
```
```Java
Optional<String> handleInput(String input) {
  Token token = Token.of("[a-zA-Z ]+").withDecoding(Decoding.of(" ", ","));
  Template template = Template.of("Hello ${name}!")
      .withToken("name", token);

  Values data = template.parse(input);
  return data.get("name");
}
```
```
Optional[new,World]
```
