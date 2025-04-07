package cn.linux95.springaiexample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@RestController
@RequestMapping("/zhipuai/chat")
public class ZhipuAiChatExampleController {
    private static final Logger log = LoggerFactory.getLogger(ZhipuAiChatExampleController.class);
    
    private final InMemoryChatMemory chatMemory = new InMemoryChatMemory();
    
    private ChatClient chatClient;
    private ZhiPuAiChatModel zhiPuAiChatModel;
    
    private static final int MY_CHAT_MEMORY_RETRIEVE_SIZE_KEY = 10;
    
    public ZhipuAiChatExampleController(ZhiPuAiChatModel zhiPuAiChatModel){
         this.chatClient = ChatClient.builder(zhiPuAiChatModel)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
         this.zhiPuAiChatModel = zhiPuAiChatModel;
    }

    
    /**
     * 简单聊天
     */
    @GetMapping("/simpleChat")
    public void simpleChat() {
        ChatResponse response = zhiPuAiChatModel.call(new Prompt("你好"));
        log.info("response:{}", response.getResult().getOutput().getText());
        
        String called = zhiPuAiChatModel.call(new UserMessage("你好"));
        String 你好 = zhiPuAiChatModel.call("你好");
    }
    
    
    /**
     * 带聊天记忆
     */
    @GetMapping("/chatWithMemory")
    public void chatWithMemory(String userMsg) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(userMsg)
                .advisors(
                        a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, 1)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, MY_CHAT_MEMORY_RETRIEVE_SIZE_KEY)
                )
                .call()
                .chatResponse();
        log.info("response:{}", chatResponse.getResult().getOutput().getText());
        
    }
    
    /**
     * 带聊天记忆 测试
     */
    @GetMapping("/chatWithMemoryTest")
    public String chatWithMemoryTest(String userMsg) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(userMsg)
                .advisors(
                        a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, 1)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, MY_CHAT_MEMORY_RETRIEVE_SIZE_KEY)
                )
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }
    
    /**
     * 流式输出
     */
    @GetMapping("/streamChat")
    public Flux<String> streamChat() {
        Flux<String> responseFlux = chatClient.prompt()
                .user("你好")
                .advisors(
                        a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, 1)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, MY_CHAT_MEMORY_RETRIEVE_SIZE_KEY)
                )
                .stream()
                .content();
        log.info("responseFlux:{}", responseFlux);
        return responseFlux;
    }
    
}
