package org.springframework.gsniudaiuploadingfiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StoreFileService implements StoreService {
    private final Logger logger = LoggerFactory.getLogger(StoreFileService.class); // 用Logger打印日志
    private final Path rootDirectory; // 存储用户名的文件夹位置

    /**
     * 传入了一个参数：storeProperties，就是“存储属性”，一切关于存储
     * 有关的属性，比如用户文件存储在哪里，一个文件夹里最多储存多少文件等，这些
     * 属性都存储在storeProperties中。
     * 使用@Autowired让IoC容器自动注入你的StoreProperties依赖。
     * @param storeProperties
     */
    @Autowired
    public StoreFileService(StoreProperties storeProperties) {
        rootDirectory = Paths.get(storeProperties.getLocation());
    }

    /**
     * 先使用File API中的 .getInputStream() 获得传入文件的输入流（InputStream），
     * 再调用Files API 中的 .copy(InputStream, target, copyType) 来将传入的文件
     * 写进目标路径中, target就是我们的目标路径, 详情可查阅相关文档。
     */
    @Override
    public void store(MultipartFile file) {
        String filename = file.getOriginalFilename();
        logger.info("filename");
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, this.rootDirectory.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file ");
        }
    }

    /**
     * 使用File API中的.mkdir()创建一个目录, 目录是由rootDirectory指定的。
     */
    @Override
    public void init() {
        File file = new File(rootDirectory.toString());
        file.mkdir();
    }

    /**
     * 使用Spring Mvc推荐的UrlResource API来获取我们的储存
     * 文件，先把Path路径转换成URI，再传入到UrlResource中，获得
     * 我们的指定文件。
     */
    @Override
    public Resource retrieve(String filename) {
        Path path = retrievePath(filename);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            return resource;
        } catch (MalformedURLException e) {
            throw new StorageException("Failed to store file ");
        }
    }

    /**
     * 使用了Path API中的 .resolve 方法, 让文件名附着在存储文件夹的路径后, 构成
     * 文件的完整路径。
     */
    @Override
    public Path retrievePath(String filename) {
        return rootDirectory.resolve(filename);
    }

    /**
     * 使用Files API 中的 .walk 方法, 对根目录中所有直接子
     * 文件进行扫描，该方法返回一个Stream对象，关于Java Stream
     * API的内容大家可以去查阅相关内容。
     */
    @Override
    public Stream<Path> retrieveAllPath() {
        try {
            return Files.walk(rootDirectory, 1).
                filter(path -> !path.equals(this.rootDirectory))
                .map(rootDirectory::relativize);
        }
        catch(IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    /**
     * 使用 FileSystemUtils API 中的 .deleteRecursively()方法将文件夹中
     * 的所有文件全部删除, 用于文件夹的清零。
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootDirectory.toFile());
    }

}