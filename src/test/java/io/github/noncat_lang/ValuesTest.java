package io.github.noncat_lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

class ValuesTest {

  @Test
  void empty() {
    // given
    // when
    Values values = Values.of();
    // then
    assertThat(values.size()).isEqualTo(0);
  }

  @Test
  void OptionalEmptyWhenFieldNotFound() {
    // given
    // when
    Values values = Values.create();
    // then
    assertThat(values.get("nope")).isEmpty();
  }
  
  @Test
  void initializedWithData() {
    // given
    // when
    Values values = Values.of("foo", "bar");
    // then
    assertThat(values.get("foo")).hasValue("bar");
  }
  
  @Test
  void initializedFromMap() {
    // given
    // when
    Values values = Values.of(Map.of("foo", "bar"));
    // then
    assertThat(values.get("foo")).hasValue("bar");
  }

  @Test
  void setData() {
    // given
    Values values = Values.of();
    // when
    values.set("foo", "bar");
    // then
    assertThat(values.get("foo")).hasValue("bar");
  }

  @Test
  void setDataFromMap() {
    // given
    Values values = Values.of();
    // when
    values.setAll(Map.of("foo", "bar"));
    // then
    assertThat(values.get("foo")).hasValue("bar");
  }

}
