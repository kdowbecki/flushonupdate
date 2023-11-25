package io.github.kdowbecki.flushonupdateissue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpServerErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

// Using RANDOM_PORT to make sure MockMvc or test transaction rollback doesn't interfere
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TextControllerTest {

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @Value("http://localhost:${local.server.port}/text")
  private String basePath;

  @Test
  void shouldReadExisting() {
    // shouldReadExisting is affected by "AssertionError: Misalignment" directly
    //  test fails
    var text = restTemplateBuilder.build().getForObject(basePath + "/1", String.class);
    assertThat(text).isEqualTo("Hello");
  }

  @Test
  void shouldErrorMissing() {
    // shouldErrorMissing() is affected by "AssertionError: Misalignment" indirectly,
    //  test passes but there is a suppressed exception
    assertThatThrownBy(() -> restTemplateBuilder.build().getForObject(basePath + "/999", String.class))
        .isInstanceOf(HttpServerErrorException.InternalServerError.class)
        .hasMessageContaining("No value present");
  }

}
