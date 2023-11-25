package io.github.kdowbecki.flushonupdateissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TextService {

  @Autowired
  private TextRepository repository;

  @NewSpan
  @Transactional
  public String readText(Integer id) {
    return repository.findById(id)
        .orElseThrow()
        .getText();
  }

}
