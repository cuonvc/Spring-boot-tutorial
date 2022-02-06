package com.tutorail.apidemo.Springboot.controllers;

import com.tutorail.apidemo.Springboot.models.ResponObject;
import com.tutorail.apidemo.Springboot.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path = "/api/v1/FileUpload")
public class FileUploadController {
    //this controller receive file/image/video/... from client

    //Inject storage Service here
    @Autowired
    private IStorageService storageService;
    @PostMapping("/insert")
    public ResponseEntity<ResponObject> uploadFile(@RequestParam("file")MultipartFile file) {
        try {
            //save file to folder on service/repositories => use a service
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponObject("Ok", "upload file successfully", generatedFileName)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponObject("Fail", e.getMessage(), "")
            );
        }
    }

    //get image url
    @GetMapping("/files/{fileName:.+}")
    //865f1f812b704bf9b51f986087350f20.png
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    //load all file đã upload về
    @GetMapping("/getAll")
    public ResponseEntity<ResponObject> getUploadFiles() {
        try {
            List<String> urls = storageService.loadAll()
                    .map(path -> {
                        //convert file name to url (sent request "readDetailFile")
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "readDetailFile", path.getFileName().toString())
                                .build().toUri().toString();
                        return urlPath;
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(new ResponObject("Ok", "List files successfully", urls));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponObject("Fail", "List files failed", new String[] {}));
        }
    }
}
