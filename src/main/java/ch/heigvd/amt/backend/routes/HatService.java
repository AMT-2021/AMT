package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.DBSchema.Hat;
import ch.heigvd.amt.backend.repository.HatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.Optional;

@RestController
public class HatService {

  @Autowired
  private HatDAO hatDAO;

  @GetMapping("/hats/{hatId}")
  public ResponseEntity<Hat> getHatNameById(@PathVariable Integer hatId) {
    Optional<Hat> hat = hatDAO.getHatById(hatId);
    if (hat.isPresent()) {
      return ResponseEntity.ok().body(hat.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping(path = "/hats", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Hat> createHat(@RequestBody Hat newHat) throws ServerException {
    Hat hat = hatDAO.save(newHat);
    if (hat == null) {
      throw new ServerException("The hat couldn't be created");
    } else {
      return new ResponseEntity<>(hat, HttpStatus.CREATED);
    }
  }
}
