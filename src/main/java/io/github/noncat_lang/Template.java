package io.github.noncat_lang;

import io.github.noncat_lang.internal.TemplateImpl;

public interface Template {

  static Template of(String templateString) {
    return TemplateImpl.of(templateString);
  }

  Template withToken(String field, Token token);

  String format(Values values);

  String unparse(Values values);

  Values parse(String value);

}
