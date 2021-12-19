package ch.heigvd.amt.backend.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

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
    try {
      Files.createDirectories(uploadsDirectory);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // uploadsDirectory.toFile().mkdirs();


    // get image from ressource
    try {
      File file = ResourceUtils.getFile("classpath:img/default.png");
      Files.copy(file.toPath(), uploadsDirectory.resolve("default.png"), StandardCopyOption.REPLACE_EXISTING);
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
    Path target = uploadsDirectory.resolve(id+".png");
    Files.copy(contents, target, StandardCopyOption.REPLACE_EXISTING);
  }
}
