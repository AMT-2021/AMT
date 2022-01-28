package ch.heigvd.amt.backend.routes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HatPhotoController {

  @Value("${ch.heivd.amt.backend.datadir}")
  String uploadPath;

  @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
  public @ResponseBody byte[] getImageWithMediaType(@PathVariable int id) throws IOException {
    Path dirPath = Paths.get(uploadPath);

    Path imagePath = dirPath.resolve(id + ".png");
    if (!imagePath.toFile().exists()) {
      imagePath = dirPath.resolve("default.png");
    }

    InputStream in = new FileInputStream(imagePath.toFile());
    return in.readAllBytes();
  }
}
