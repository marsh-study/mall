package com.mall.backend.aspect;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MultipartFile序列化器
 * 用于解决MultipartFile对象无法序列化的问题
 */
public class MultipartFileSerializer extends JsonSerializer<MultipartFile> {

    @Override
    public void serialize(MultipartFile value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        Map<String, Object> fileInfo = new HashMap<>();
        fileInfo.put("name", value.getOriginalFilename());
        fileInfo.put("size", value.getSize());
        fileInfo.put("contentType", value.getContentType());
        fileInfo.put("isEmpty", value.isEmpty());
        
        gen.writeObject(fileInfo);
    }
}