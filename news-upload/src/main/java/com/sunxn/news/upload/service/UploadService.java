package com.sunxn.news.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * @description:
 * @data: 2020/4/8 17:08
 * @author: xiaoNan
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties uploadProperties;

    public String uploadImage(MultipartFile file) {
        try {
            // 校验文件类型
            String contentType = file.getContentType();
            if (!uploadProperties.getAllowTypes().contains(contentType)) {
                throw new SunxnNewsException(NewsSystemExceptionEnum.INVALID_FILE_TYPE);
            }

            // 校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new SunxnNewsException(NewsSystemExceptionEnum.INVALID_FILE_TYPE);
            }

            // 上传到FastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);

            // 返回路径
            return uploadProperties.getBaseUrl() +storePath.getFullPath();
        } catch (Exception e) {
            log.error("【文件上传服务】 上传图片失败！", e);
            throw new SunxnNewsException(NewsSystemExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }

}
