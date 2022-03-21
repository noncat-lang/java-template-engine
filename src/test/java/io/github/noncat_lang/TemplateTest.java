package io.github.noncat_lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.Test;

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

  @Test
  void unparseSimple() {
    // given
    Token token = Token.of("[a-zA-Z]+");
    Template template = Template.of("Hello ${world}!").withToken("world", token);
    // when
    Map<String, String> values = Map.of("world", "LangSec");
    String result = template.unparse(values);
    // then
    assertThat(result).isEqualTo("Hello LangSec!");
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
  void parseSimple() {
    // given
    Token token = Token.of("[a-zA-Z]+");
    Template template = Template.of("Hello ${world}!").withToken("world", token);
    // when
    Map<String, String> result = template.parse("Hello LangSec!");
    // then
    assertThat(result).containsEntry("world", "LangSec");
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

  @Test
  void tokenFieldNull() {
    Template template = Template.of("any");
    Token token = Token.of("any");
    String field = null;
    assertThatThrownBy(() -> template.withToken(field, token)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void tokenFieldEmpty() {
    Template template = Template.of("any");
    Token token = Token.of("any");
    String field = "";
    assertThatThrownBy(() -> template.withToken(field, token)).isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void tokenFieldBlank() {
    Template template = Template.of("any");
    Token token = Token.of("any");
    String field = " ";
    assertThatThrownBy(() -> template.withToken(field, token)).isExactlyInstanceOf(IllegalArgumentException.class);
  }

}
