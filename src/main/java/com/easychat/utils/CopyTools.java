package com.easychat.utils;

import com.easychat.entity.enums.BeautyAccountStatusEnum;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CopyTools
 * @Author chenhongxin
 * @Date 2025/5/7 下午2:49
 * @mood happy
 */
public class CopyTools {
    public static<T,S> List<T> copyList(List<S> slist, Class<T> classz) {
        List<T> list = new ArrayList<T>();
        for(S s : slist){
            T t = null;
            try{
                t = classz.newInstance();
            }catch (Exception e){
                e.printStackTrace();
            }
            BeanUtils.copyProperties(s,t);
            list.add(t);
        }
        return list;
    }

    public static <T,S> T copy(S s, Class<T> classz) {
        T t = null;
        try{
            t = classz.newInstance();

        }catch (Exception e){
            e.printStackTrace();
        }
        BeanUtils.copyProperties(s,t);
        return t;
    }
}
