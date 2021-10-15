package ch.heigvd.amt.backend.repository;

import ch.heigvd.amt.backend.DBSchema.Hat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HatDAO extends JpaRepository<Hat, Integer> {
  Optional<Hat> getHatById(Integer id);
}
