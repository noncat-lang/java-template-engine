package io.github.noncat_lang;

import java.util.Map;

import io.github.noncat_lang.internal.TemplateImpl;

public interface Template {

  static Template of(String templateString) {
    return TemplateImpl.of(templateString);
  }

  Template withToken(String field, Token token);

  String format(Map<String, String> values);

  String unparse(Map<String, String> values);

  Map<String, String> parse(String value);

}
