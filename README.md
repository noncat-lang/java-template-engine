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
