package org.springframework.gsniudaiuploadingfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

    private final StoreService storeService;

    /**
     * 构造函数，传入一个StoreService，IoC容器会自动注入该实例，
     * 也就是我们的StoreFileService的实例。
     * @param storeService
     */
    @Autowired
    public UploadController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * 将“/”对应的Post请求映射到这个方法，然后存储文件，返回一个跳转页面，并且将成功上传页面的信息
     * 传给下一个页面。
     * 知识点：FlashAttribute 详情请参见本人文章：
     * 《Spring MVC 之 Flash Attribute详解》https://zhuanlan.zhihu.com/p/55983646
     * @param file 用户上传的文件
     * @param redirectAttributes 跳转属性
     * @return 跳转页面名
     */
    @PostMapping("/")
    public String uploadHandler(@RequestParam("file") MultipartFile file, 
                RedirectAttributes redirectAttributes) {
        // we need an interface for storing the file, and an implementation of the store interface.
        storeService.store(file);
        redirectAttributes.addFlashAttribute("message", "you successfully uploaded");
        // store the file
        return "redirect:/";
    }

    @GetMapping("/files/{filename:.+}")
    public @ResponseBody ResponseEntity<Resource> reclaimHandler(@PathVariable String filename) {
        Resource file = storeService.retrieve(filename);
        ResponseEntity<Resource> responseEntity = ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\\" +
            file.getFilename() + "\\").body(file);
        return responseEntity;
    }

    /**
     * 映射到入口页面。
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

}