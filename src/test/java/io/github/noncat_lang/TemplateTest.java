package io.github.noncat_lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.noncat_lang.exceptions.MissingTokenException;
import io.github.noncat_lang.exceptions.MissingValueException;

class TemplateTest {

  @Test
  void formatSimple() {
    // given
    Token token = Token.of("[a-zA-Z]+");
    Template template = Template.of("Hello ${world}!").withToken("world", token);
    // when
    Map<String, String> values = Map.of("world", "LangSec");
    String result = template.format(values);
    // then
    assertThat(result).isEqualTo("Hello LangSec!");
  }

  static Stream<Arguments> simpleParams() {
    return Stream.of(
        Arguments.of("[a-zA-Z]+", "Hello ${world}!", "world", "LangSec", "Hello LangSec!"),
        Arguments.of("[a-zA-Z]+", "Hello {${world}}!", "world", "LangSec", "Hello {LangSec}!"),
        Arguments.of("[a-zA-Z]+", "Hello $${world}!", "world", "LangSec", "Hello $LangSec!"),
        Arguments.of("[a-zA-Z]+", "Hello ${} ${world}!", "world", "LangSec", "Hello ${} LangSec!"),
        Arguments.of("[a-zA-Z]+", "Hello ${0}!", "0", "LangSec", "Hello LangSec!"));
  }

  @ParameterizedTest
  @MethodSource("simpleParams")
  void unparse(String regex, String templateString, String field, String value, String expected) {
    // given
    Token token = Token.of(regex);
    Template template = Template.of(templateString).withToken(field, token);
    // when
    Map<String, String> values = Map.of(field, value);
    String result = template.unparse(values);
    // then
    assertThat(result).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("simpleParams")
  void parse(String regex, String templateString, String field, String expected, String value) {
    // given
    Token token = Token.of(regex);
    Template template = Template.of(templateString).withToken(field, token);
    // when
    Map<String, String> result = template.parse(value);
    // then
    assertThat(result).containsEntry(field, expected);
  }

  @Test
  void unparseMultipleFields() {
    // given
    Token token = Token.of("[a-zA-Z]+");
    Template template = Template.of("${a}, ${b}!").withToken("a", token).withToken("b", token);
    // when
    Map<String, String> values = Map.of("a", "foo", "b", "bar");
    String result = template.unparse(values);
    // then
    assertThat(result).isEqualTo("foo, bar!");
  }

  @Test
  void unparseWithEncoding() {
    // given
    Token token = Token.of("'[a-zA-Z]+'").withEncoding(Encoding.of(".+", "'$0'"));
    Template template = Template.of("Hello ${world}!").withToken("world", token);
    // when
    Map<String, String> values = Map.of("world", "LangSec");
    String result = template.unparse(values);
    // then
    assertThat(result).isEqualTo("Hello 'LangSec'!");
  }

  @Test
  void parseMultipleFields() {
    // given
    Token token = Token.of("[a-zA-Z]+");
    Template template = Template.of("${a}, ${b}!").withToken("a", token).withToken("b", token);
    // when
    Map<String, String> result = template.parse("foo, bar!");
    // then
    assertThat(result).containsEntry("a", "foo").containsEntry("b", "bar");
  }

  @Test
  void parseWithDecoding() {
    // given
    Token token = Token.of("'[a-zA-Z]+'").withDecoding(Decoding.of("'", ""));
    Template template = Template.of("Hello ${world}!").withToken("world", token);
    // when
    Map<String, String> result = template.parse("Hello 'LangSec'!");
    // then
    assertThat(result).containsEntry("world", "LangSec");
  }

  @Test
  void unparseWithMissingToken() {
    Template template = Template.of("${any}");
    Map<String, String> values = Map.of("any", "any");
    assertThatThrownBy(() -> template.unparse(values)).isExactlyInstanceOf(MissingTokenException.class)
        .hasMessage("Token for field 'any' is missing");
  }

  @Test
  void unparseWithMissingValue() {
    Template template = Template.of("${any}").withToken("any", Token.of("[a-z]+"));
    Map<String, String> values = Map.of();
    assertThatThrownBy(() -> template.unparse(values)).isExactlyInstanceOf(MissingValueException.class)
        .hasMessage("Value for field 'any' is missing");
  }

  @Test
  void parseWithMissingToken() {
    Template template = Template.of("${any}");
    String value = "any";
    assertThatThrownBy(() -> template.parse(value)).isExactlyInstanceOf(MissingTokenException.class)
        .hasMessage("Token for field 'any' is missing");
  }

  @Test
  void templateStringNull() {
    String templateString = null;
    assertThatThrownBy(() -> Template.of(templateString)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void tokenNull() {
    Template template = Template.of("any");
    Token token = null;
    assertThatThrownBy(() -> template.withToken("any", token)).isExactlyInstanceOf(NullPointerException.class);
  }

  static Stream<Arguments> invalidTokenFields() {
    return Stream.of(
        Arguments.of(null, NullPointerException.class, "field is marked non-null but is null"),
        Arguments.of("", IllegalArgumentException.class, "Token field should not be blank/empty"),
        Arguments.of(" ", IllegalArgumentException.class, "Token field should not be blank/empty"),
        Arguments.of("#", IllegalArgumentException.class, "Token field does not match pattern [a-zA-Z0-9]+"));
  }

  @ParameterizedTest
  @MethodSource("invalidTokenFields")
  void tokenFieldEmpty(String field, Class<Exception> expectedException, String expectedMessage) {
    Template template = Template.of("any");
    Token token = Token.of("any");
    assertThatThrownBy(() -> template.withToken(field, token)).isExactlyInstanceOf(expectedException)
        .hasMessage(expectedMessage);
  }

}
