package lumosblog.utils;

import lombok.Data;
import org.apache.tomcat.jni.OS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * @author 冠麟
 * @date 2019/10/24 8:51
 */
public class SettingUtils {


    /**
     * 设置评论来源ip{@link lumosblog.controller.ArticleController}
     */
    public static String SOURCEIP;




    /**
     * 文件存储地址
     */
    public static  String UPLOAD_FILE;




    static {
        readProperty();
    }

    /**
     * 读取配置文件
     */
    private static void readProperty() {
        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("setting.properties");
            SOURCEIP = properties.getProperty("source_IP");
            UPLOAD_FILE = properties.getProperty("saveUploadFile");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
