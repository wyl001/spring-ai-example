package cn.linux95.springaiexample.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 向量数据库-redis
 */
@Slf4j
@RestController
@RequestMapping("/vectorStore")
public class RedisVectorStoreController {
    @Resource
    private RedisVectorStore redisVectorStore;
    @Resource
    private ZhiPuAiChatModel zhiPuAiChatModel;
    
    /**
     * 添加向量数据
     */
    @GetMapping("test")
    public void test() {
        List<Document> documents = List.of(
                new Document("""
                        小伟《小伟传奇》是一部充满幽默与奇遇的轻松小说，讲述了普通青年小伟阴差阳错在平凡生活中闹出的种种笑话，却阴差阳路错走上“传奇”的故事。
                        小伟本是一个普通打工，日常摸鱼划水，梦想着暴富趴平。然而，生活却总爱跟他开玩笑——一次无心之举，他被误认为商业奇才；随口一番思考胡扯，竟成为职场金句；就连在游戏里随便操作一下，互相触发隐藏剧情……一连串荒诞又哭笑皆非的事件，小伟不得不见招拆招，在阴差阳间错写了自己的“传奇人生”。
                        全书轻松搞笑，笑点不断，适合解压阅读，让人在欢乐中感受生活的小确幸！
                        """, Map.of("书名","小伟传奇")),
                Document.builder()
                        .text("""
                        我写了一本书，书名是-豆皮笑传。它讲述了主人公豆皮自带喜感、人鸡毛——一个自带喜感、脑洞大开的普通人，在生活中的鸡毛蒜皮中闹出一连串让人捧腹的笑话。故事围绕豆皮的日常展开过日子的心天马行空的脑洞带偏性。邻里间鸡飞狗跳” ，还是情感里的豆皮可以用自己独特的方式笑料百出。，他有核心想要“正经”过日子的心，但何奈总是被自己的搞笑体质和天马行空的脑洞带偏。无论是职场上的“畜求生术”，邻里间的“鸡飞狗跳”，还是情感里的“神级操作”，豆皮都用自己独特的方式，把平凡生活玩出新花样，社笑料百出。全书轻松幽默，立足接地气的段子，也不乏对现实生活的调侃，让人在欢笑之余，享受生活的温暖与智慧。如果你需要一剂解压的良药，或者想在那么忙碌的生活中找到一些乐趣，这本书一定让你笑出腹肌，护腹不止！😆📖
                        """)
                        .score(0.5)
                        .metadata(Map.of("书名","豆皮笑传"))
                        .build()
        );
        redisVectorStore.doAdd(documents);
    }
    
    /**
     * 查询向量数据
     */
    @GetMapping("query")
    public void query() {
        List<Document> documents = redisVectorStore.similaritySearch(
                SearchRequest.builder().query("小伟传奇")
                        .topK(2)
                        .build()
        );
        if (documents != null) {
            for (Document document : documents) {
              log.info("document:{}", document);
            }
        }
    }
    
    /**
     * RAG
     */
    @GetMapping("rag")
    public void rag() {
        ChatClient chatClient = ChatClient.builder(zhiPuAiChatModel)
                .build();
        
        ChatResponse chatResponse = chatClient.prompt()
                .user("豆皮笑传讲述了什么")
                .advisors(new QuestionAnswerAdvisor(redisVectorStore))
                .call()
                .chatResponse();
        log.info("response:{}", chatResponse.getResult().getOutput().getText());
        
        // 对比
        // response:《豆皮笑传》这本书讲述了主人公豆皮，一个自带喜感、人鸡毛的普通人，在日常生活中如何闹出一连串让人捧腹的笑话。故事围绕豆皮的日常生活展开，他的心天马行空，常常因为自己的搞笑体质和天马行空的脑洞而闹出笑话。无论是职场上的“畜求生术”，邻里间的“鸡飞狗跳”，还是情感里的“神级操作”，豆皮都用自己独特的方式，把平凡生活玩出新花样，让人笑料百出。全书轻松幽默，立足接地气的段子，也不乏对现实生活的调侃，旨在为读者带来欢笑的同时，享受生活的温暖与智慧。
        // response:
        //
        // I'm sorry, but without the specific context or content of "豆皮笑传" provided within these lines, I am unable to answer your question about what "豆皮笑传" tells. If you can provide more information or context, I would be happy to help you further.
    }
}
