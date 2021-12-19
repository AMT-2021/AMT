package ch.heigvd.amt.backend.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HatPhotoService {
  private Path uploadsDirectory;

  /**
   * Handles storage and distribution of hat photos
   *
   * @param uploadsDirectoryPath Where to store images in the filesystem.
   */
  public HatPhotoService(
      @Value("${ch.heivd.amt.backend.datadir}") String uploadsDirectoryPath) {
    uploadsDirectory = Paths.get(uploadsDirectoryPath).normalize();
    uploadsDirectory.toFile().mkdirs();
  }

  /**
   * Saves a photo for the specified hat.
   *
   * If the specified hat has a photo, the photo is replaced.
   */
  public void saveHatPhoto(String id, InputStream contents) throws IOException {
    Path target = uploadsDirectory.resolve("id");
    Files.copy(contents, target, StandardCopyOption.REPLACE_EXISTING);
  }
}
