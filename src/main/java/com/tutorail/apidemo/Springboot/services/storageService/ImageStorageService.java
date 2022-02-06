package com.tutorail.apidemo.Springboot.services.storageService;

import com.tutorail.apidemo.Springboot.services.IStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService {
    private final Path storageFolder = Paths.get("uploads");
    //constructor
    public ImageStorageService() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            //nếu khởi tạo không thành công vì lý do nào đó
            throw new RuntimeException("Cannot initialize storage", e);
        }
    }

    //check file is image?
    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()); //get đuôi file
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp", "gif", "pdf"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("start");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }
            //check file is image? (check file name extensitons)
            //use Commons IO library maven (google search)
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            //file must be <= 5mb
            float fileSizeInMegabytes = file.getSize() / 1000000.0f;
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be less 5 megabytes");
            }
            //file must be rename before save to server
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            //random một chuỗi UUID bất kì làm tên file (google search UUID)
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            //resolve: https://www.geeksforgeeks.org/path-resolve-method-in-java-with-examples/
            //normalize: https://www.geeksforgeeks.org/path-normalize-method-in-java-with-examples/
            //toAbsolutePath: https://www.geeksforgeeks.org/path-toabsolutepath-method-in-java-with-examples/

            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                //copy file mới (inputStream) vào destinationFilePath
                //nếu đã tồn tại thì replace
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            //list all files in storageFolder
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder))
                    .map(this.storageFolder::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load storage files", e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
