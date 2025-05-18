package com.easychat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.easychat.entity.enums.ResponseCodeEnum;
import com.easychat.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName JsonUtils
 * @Author chenhongxin
 * @Date 2025/5/15 下午12:52
 * @mood happy
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    public static final SerializerFeature[] FEATURES = { SerializerFeature.WriteMapNullValue };

    public static String convertObj2Json(Object obj) {
        return JSON.toJSONString(obj, FEATURES);
    }

    public static <T> T convertJson2Obj(String json, Class<T> classz) {
        try {
            return JSON.parseObject(json, classz);
        } catch (Exception e) {
            logger.error("convertJson2Obj异常, json: {}", json, e);
            throw new BusinessException(ResponseCodeEnum.CODE_601);
        }
    }

    public static <T> List<T> convertJsonArray2List(String json, Class<T> classz) {
        try {
            return JSON.parseArray(json, classz);
        } catch (Exception e) {
            logger.error("convertJsonArray2List异常, json: {}", json, e);
            throw new BusinessException(ResponseCodeEnum.CODE_601);
        }
    }
}
