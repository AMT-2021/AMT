package ch.heigvd.amt.backend.routes;

import ch.heigvd.amt.backend.DBSchema.Hat;
import ch.heigvd.amt.backend.repository.HatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
