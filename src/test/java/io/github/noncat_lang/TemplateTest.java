package io.github.noncat_lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

class TemplateTest {

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
    assertThat(result.get("world")).isEqualTo("LangSec");
  }

  @Test
  void parseWithDecoding() {
    // given
    Token token = Token.of("'[a-zA-Z]+'").withDecoding(Decoding.of("'", ""));
    Template template = Template.of("Hello ${world}!").withToken("world", token);
    // when
    Map<String, String> result = template.parse("Hello 'LangSec'!");
    // then
    assertThat(result.get("world")).isEqualTo("LangSec");
  }

}
