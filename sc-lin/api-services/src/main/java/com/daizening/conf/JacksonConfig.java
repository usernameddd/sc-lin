package com.daizening.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    /**
     * 将返回给请求端的内容是null的字段进行过滤（返回给前端的数据中再也没有null这个关键字了，null就表示无，无就不应该传递给前端）
     * @param builder
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 替换非空（将null值变成空字符串""）
//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object o, JsonGenerator jsonGenerator,SerializerProvider serializerProvider)throws IOException, JsonProcessingException {
//                jsonGenerator.writeString("");
//            }
//        });
        return objectMapper;
    }
}
