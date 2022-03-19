package io.github.noncat_lang;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import io.github.noncat_lang.TemplateParser.ArgContext;
import io.github.noncat_lang.TemplateParser.ElementContext;
import io.github.noncat_lang.TemplateParser.TemplateContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Template {
  TemplateContext parseTree;
  Map<String, Token> tokens = new HashMap<>();

  public static Template of(@NonNull String templateString) {
    TemplateLexer lexer = new TemplateLexer(CharStreams.fromString(templateString));
    TemplateParser parser = new TemplateParser(new CommonTokenStream(lexer));
    return new Template(parser.template());
  }

  public Template withToken(@NonNull String field, @NonNull Token token) {
    tokens.put(field, token);
    return this;
  }

  public String unparse(@NonNull Map<String, String> values) {
    return parseTree.element().stream().map(element -> unparseElement(element, values)).collect(Collectors.joining());
  }

  private String unparseElement(ElementContext element, Map<String, String> values) {
    ArgContext arg = element.arg();
    return arg == null ? element.TEXT().getText() : unparseArg(arg, values);
  }

  private String unparseArg(ArgContext arg, @NonNull Map<String, String> values) {
    String id = arg.ID().getText();
    String value = values.get(id);
    Token token = tokens.get(id);
    if (value != null && token != null) {
      value = token.encode(value);
    }
    return value;
  }

  public Map<String, String> parse(@NonNull String value) {
    String pattern = parseTree.element().stream().map(element -> parseElement(element)).collect(Collectors.joining());
    Matcher matcher = Pattern.compile(pattern).matcher(value);
    if (!matcher.matches()) {
      throw new IllegalArgumentException(
          format("Error during parsing: value '%s' does not match pattern '%s'", value, pattern));
    }
    Map<String, String> values = new HashMap<>();
    for (Entry<String, Token> entry : tokens.entrySet()) {
      String id = entry.getKey();
      String val = entry.getValue().decode(matcher.group(id));
      values.put(id, val);
    }
    return values;
  }

  private String parseElement(ElementContext element) {
    ArgContext arg = element.arg();
    return arg == null ? element.TEXT().getText() : parseArg(arg);
  }

  private String parseArg(ArgContext arg) {
    String id = arg.ID().getText();
    Token token = tokens.get(id);
    return format("(?<%s>%s)", id, token.getRegex());
  }

}
