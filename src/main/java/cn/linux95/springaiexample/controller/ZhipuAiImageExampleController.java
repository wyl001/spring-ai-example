package cn.linux95.springaiexample.controller;

import groovy.util.logging.Slf4j;
import jakarta.annotation.Resource;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/zhipuai/image")
public class ZhipuAiImageExampleController {
    
    private static final Logger log = LoggerFactory.getLogger(ZhipuAiImageExampleController.class);
    @Resource
    private ZhiPuAiImageModel zhiPuAiImageModel;
    
    /**
     * 图像
     */
    @GetMapping("/image")
    public void image() {
        ImagePrompt imagePrompt = new ImagePrompt("a cat", ZhiPuAiImageOptions.builder().build());
        ImageResponse imageResponse = zhiPuAiImageModel.call(imagePrompt);
        log.info("imageResponse:{}", imageResponse.getResult().getOutput().getUrl());
    }
}
