package io.github.noncat_lang.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import io.github.noncat_lang.Template;
import io.github.noncat_lang.TemplateLexer;
import io.github.noncat_lang.TemplateParser;
import io.github.noncat_lang.TemplateParser.ArgContext;
import io.github.noncat_lang.TemplateParser.ElementContext;
import io.github.noncat_lang.TemplateParser.TemplateContext;
import io.github.noncat_lang.Token;
import io.github.noncat_lang.exceptions.MissingTokenException;
import io.github.noncat_lang.exceptions.MissingValueException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TemplateImpl implements Template {
  TemplateContext parseTree;
  Map<String, Token> tokens = new HashMap<>();

  public static TemplateImpl of(@NonNull String templateString) {
    TemplateLexer lexer = new TemplateLexer(CharStreams.fromString(templateString));
    TemplateParser parser = new TemplateParser(new CommonTokenStream(lexer));
    return new TemplateImpl(parser.template());
  }

  @Override
  public TemplateImpl withToken(@NonNull String field, @NonNull Token token) {
    if (field.isBlank()) {
      throw new IllegalArgumentException("Token field should not be blank/empty");
    }
    if (!field.matches("[a-zA-Z0-9]+")) {
      throw new IllegalArgumentException("Token field does not match pattern [a-zA-Z0-9]+");
    }
    tokens.put(field, token);
    return this;
  }

  @Override
  public String format(@NonNull Map<String, String> values) {
    return unparse(values);
  }

  @Override
  public String unparse(@NonNull Map<String, String> values) {
    return parseTree.element().stream().map(element -> unparseElement(element, values)).collect(Collectors.joining());
  }

  private String unparseElement(ElementContext element, Map<String, String> values) {
    ArgContext arg = element.arg();
    return arg == null ? element.TEXT().getText() : unparseArg(arg, values);
  }

  private String unparseArg(ArgContext arg, @NonNull Map<String, String> values) {
    String id = getId(arg);
    Token token = tokens.get(id);
    if (token == null) {
      throw new MissingTokenException(String.format("Token for field '%s' is missing", id));
    }
    String value = values.get(id);
    if (value == null) {
      throw new MissingValueException(String.format("Value for field '%s' is missing", id));
    }
    value = token.encode(value);
    return value;
  }

  @Override
  public Map<String, String> parse(@NonNull String value) {
    String pattern = parseTree.element().stream().map(this::parseElement).collect(Collectors.joining());
    Matcher matcher = Pattern.compile(pattern).matcher(value);
    if (!matcher.matches()) {
      throw new IllegalArgumentException(
          String.format("Error during parsing: value '%s' does not match pattern '%s'", value, pattern));
    }
    return decodeAllToken(matcher);
  }

  private Map<String, String> decodeAllToken(Matcher matcher) {
    return tokens.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> decodeToken(entry, matcher)));
  }

  private String decodeToken(Entry<String, Token> entry, Matcher matcher) {
    return entry.getValue().decode(matcher.group("x" + entry.getKey()));
  }

  private String parseElement(ElementContext element) {
    ArgContext id = element.arg();
    return id == null ? Pattern.quote(element.TEXT().getText()) : parseArg(id);
  }

  private String parseArg(ArgContext arg) {
    String id = getId(arg);
    Token token = tokens.get(id);
    if (token == null) {
      throw new MissingTokenException(String.format("Token for field '%s' is missing", id));
    }
    return String.format("(?<x%s>%s)", id, token.getRegex());
  }

  private String getId(ArgContext arg) {
    String id = arg.ID().getText();
    return id.substring(2, id.length() - 1);
  }

}
