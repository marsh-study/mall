package com.mall.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.backend.model.form.UserForm;
import com.mall.backend.model.vo.FileInfo;
import com.mall.backend.service.system.UserService;
import com.mall.backend.util.Result;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private UserService userService;

    @Value("${file.upload.path}")
    private String uploadPath;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadPath, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 检测文件内容类型
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping
    public Result<String> deleteFile(@RequestParam("filePath") String filePath) {
        try {
            String fileName = extractFileName(filePath);

            // 安全性增强：禁止文件名中含路径分隔符
            if (fileName.contains("/") || fileName.contains("\\")) {
                return Result.fail("文件删除失败: 非法文件名");
            }

            // 保持相对路径一致性
            Path basePath = Paths.get(uploadPath);
            Path safePath = basePath.resolve(fileName);

            // 规范化路径用于比较
            Path normalizedBase = basePath.normalize();
            Path normalizedSafe = safePath.normalize();

            // 使用规范化后的路径进行比较
            if (!normalizedSafe.startsWith(normalizedBase)) {
                return Result.fail("文件删除失败: 非法文件路径");
            }

            boolean deleted = Files.deleteIfExists(safePath);
            if (deleted) {
                return Result.success("文件删除成功");
            } else {
                return Result.fail("文件删除失败: 文件不存在");
            }
        } catch (IOException e) {
            return Result.fail("文件删除失败");
        }
    }


    private String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        if (filePath.startsWith("/api/files/")) {
            return filePath.substring("/api/files/".length());
        } else if (filePath.startsWith("http")) {
            String[] parts = filePath.split("/");
            return parts[parts.length - 1];
        } else {
            return Paths.get(filePath).getFileName().toString();
        }
    }




    @PostMapping
    public Result<FileInfo> upload(@RequestParam("file") MultipartFile file) {
        try {
//             检查文件是否为空
            if (file.isEmpty()) {
                return Result.fail("文件不能为空");
            }

            // 创建上传目录（如果不存在）
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // 构建文件保存路径
            Path filePath = Paths.get(uploadPath, uniqueFilename);

            // 保存文件
            Files.write(filePath, file.getBytes());

            // 返回文件访问路径
            String fileUrl = "/api/files/"+uniqueFilename;

            FileInfo fileInfo = new FileInfo(uniqueFilename, fileUrl);
            return Result.success(fileInfo);
        } catch (IOException e) {
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) {
        try {
            // 上传到第三方图床服务
            String imageUrl = uploadToImageHosting(file);
            UserForm userForm=new UserForm() ;
            userForm.setId(id);
            userForm.setAvatar(imageUrl);
            boolean flag = userService.updateUser(userForm);
            if (flag) {
                return Result.success("上传成功");
            } else {
                return Result.fail("上传失败");
            }
        } catch (Exception e) {
            return Result.fail("上传失败: " + e.getMessage());
        }
    }

    private String uploadToImageHosting(MultipartFile file) throws IOException {
        String url = "https://img.scdn.io/api/v1.php";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("image", file.getInputStream(),
                ContentType.DEFAULT_BINARY, file.getOriginalFilename());
        builder.addTextBody("outputFormat", "auto");

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        String jsonResponse = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        if (rootNode.get("success").asBoolean()) {
            return rootNode.get("url").asText();
        } else {
            throw new RuntimeException("上传失败");
        }
    }


}
