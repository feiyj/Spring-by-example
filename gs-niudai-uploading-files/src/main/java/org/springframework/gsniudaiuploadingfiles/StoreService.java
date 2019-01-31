package org.springframework.gsniudaiuploadingfiles;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 存储服务的接口, 用来存储, 删除, 调用用户传入
 * 的文件。
 */
@Service
public interface StoreService {

    /**
     * 储存用户传入的文件。
     * @param file
     */
    void store(MultipartFile file);

    /**
     * 初始化用户储存用户文件的文件夹。
     */
    void init();

    /**
     * 根据文件名返回用户存储的文件。
     * @param filename 文件名
     * @return 文件路径
     */
    Resource retrieve(String filename);

    /**
     * 根据用户名返回用户存储的文件的路径。
     * @param filename 文件名
     * @return 文件路径
     */
    Path retrievePath(String filename);

    /**
     * 返回用户传入所有文件的文件名，以Stream的形式。
     * @return 
     */
    Stream<Path> retrieveAllPath();

    /**
     * 删除所有用户传入的文件。
     */
    void deleteAll();

}