package ch.heigvd.amt.backend.services;

import java.io.File;// TODO NGY - unused import statement
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;// TODO NGY - unused import statement

@Component // TODO NGY - use @Service instead of @Component to be more specific
public class HatPhotoService {
  private Path uploadsDirectory;

  /**
   * Handles storage and distribution of hat photos
   *
   * @param uploadsDirectoryPath Where to store images in the filesystem.
   */
  public HatPhotoService(@Value("${ch.heivd.amt.backend.datadir}") String uploadsDirectoryPath) {
    uploadsDirectory = Paths.get(uploadsDirectoryPath).normalize();
    try {
      Files.createDirectories(uploadsDirectory);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // get image from resource
    try {
      InputStream in = getClass().getResourceAsStream("/static/images/default.png");
      assert in != null;
      Files.copy(in, uploadsDirectory.resolve("default.png"), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves a photo for the specified hat.
   *
   * If the specified hat has a photo, the photo is replaced.
   */
  public void saveHatPhoto(String id, InputStream contents) throws IOException {
    // TODO NGY - better to get the MIME Type File as hard coded the extension
    Path target = uploadsDirectory.resolve(id + ".png");
    Files.copy(contents, target, StandardCopyOption.REPLACE_EXISTING);
  }
}
