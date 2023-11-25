package io.github.kdowbecki.flushonupdateissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/text")
public class TextController {

  @Autowired
  private TextService service;

  @GetMapping("/{id}")
  public ResponseEntity<String> handleGet(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(service.readText(id));
  }

  @ExceptionHandler
  public ResponseEntity<String> handleException(Exception ex) {
    return ResponseEntity.internalServerError()
        .body(ex.getMessage());
  }

}
