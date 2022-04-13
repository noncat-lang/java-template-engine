package io.github.noncat_lang.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.github.noncat_lang.Values;
import lombok.Value;

@Value(staticConstructor = "of")
public final class ValuesImpl implements Values {
  Map<String, String> data = new HashMap<>();

  @Override
  public Values set(String field, String value) {
    data.put(field, value);
    return this;
  }

  @Override
  public Values setAll(Map<String, String> values) {
    data.putAll(values);
    return this;
  }

  @Override
  public Optional<String> get(String field) {
    return Optional.ofNullable(data.get(field));
  }

  @Override
  public int size() {
    return data.size();
  }

}
