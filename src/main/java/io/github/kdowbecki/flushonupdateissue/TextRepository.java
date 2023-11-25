package io.github.kdowbecki.flushonupdateissue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<TextEntity, Integer> {

}
