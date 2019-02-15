package com.cache.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jujun chen
 * @description:
 * @date: 2019/2/14
 */
@Configuration
public class MessageConfig {

    @Autowired
    @Qualifier(value = "customRedisTemplate")
    private RedisTemplate redisTemplate;

    @Value("${spring.cache.redis.topic:cache}")
    String topicName;

    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        List<Topic> topicList = new ArrayList<>();
        topicList.add(new PatternTopic(topicName));
        container.addMessageListener(listenerAdapter, topicList);
        return container;
    }

    /**
     * 消息侦听器适配器,能将消息委托给目标侦听器方法
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(final TwoLevelCacheManager cacheManager) {
        return new MessageListenerAdapter(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                RedisSerializer<?> valueSerializer = redisTemplate.getValueSerializer();
                //获取到的body就是缓存名称
                String body = (String) valueSerializer.deserialize(message.getBody());
                cacheManager.receiver(body);
            }
        });
    }

}
