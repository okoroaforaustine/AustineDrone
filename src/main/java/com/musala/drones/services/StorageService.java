package com.musala.drones.services;

import com.musala.drones.exception.DispatcherException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class StorageService {
    private final Path rootLocation = new File("files/").toPath();

    private String hostUrl = "http://localhost:8080";

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    public String store( MultipartFile file, String path, String name) {

        String filename = StringUtils.cleanPath("./" + path + "/" + name);

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            if (filename.contains("../")) {
                throw new DispatcherException(
                        "Cannot store file with relative path outside of the  current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                if (!this.rootLocation.resolve(filename).toFile().exists()) {
                    this.rootLocation.resolve(filename).toFile().mkdirs();
                }
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {

            throw new RuntimeException("Failed to store file: " + filename, e);
        }

        String url = hostUrl + StringUtils.cleanPath(filename);

        return url;
    }

    public Resource loadAsResource( String filename) {
        try {

            Path file = rootLocation.resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new DispatcherException(
                        "Could not read the file: " + filename, HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            throw new DispatcherException("Could not read  the file: " + filename, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteOne(String path) {

        String filename = path;
        URL url;

        if (path.contains("://")) {
            try {
                url = new URL(path);
                filename = url.getFile();
            } catch (Exception ex) {
            }
        } else {

        }
        rootLocation.resolve(filename).toFile().delete();
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
