package org.springframework.gsniudaiuploadingfiles;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 知识点讲解:
 * 1. @ConfigurationProperties 注解
 *    注有该注解的类, 类中的变量可以通过外接的.properties文件进行配置,
 *    比如下面这个例子, 虽然location被赋予了"upload-dir", 但是由于我再
 *    application.properties文件中声明了"storage.location=upload", 再
 *    Spring运行过程中, location的值会被配置为"upload", 这也是一种依赖注入
 *    的方式.
 */
@ConfigurationProperties("store")
public class StoreProperties {
    private String location;

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}