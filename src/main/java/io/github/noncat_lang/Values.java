package io.github.noncat_lang;

import java.util.Map;
import java.util.Optional;

import io.github.noncat_lang.internal.ValuesImpl;

public interface Values {

  static Values create() {
    return of();
  }

  static Values of() {
    return ValuesImpl.of();
  }

  static Values of(String field, String value) {
    return of().set(field, value);
  }

  static Values of(Map<String, String> values) {
    return of().setAll(values);
  }

  Values set(String field, String value);

  Values setAll(Map<String, String> values);

  Optional<String> get(String field);

  int size();

}
