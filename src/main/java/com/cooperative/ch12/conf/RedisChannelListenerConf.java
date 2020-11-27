package com.cooperative.ch12.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/27 23:07
 */
@Configuration
public class RedisChannelListenerConf {

    /**
     * 对消息进行序列化工作
     *
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter() {
//        return new MessageListenerAdapter(new MyRedisChannelListener());
        MessageListenerAdapter adapter =new MessageListenerAdapter(new MyRedisChannelListener());
        /**
         * 默认序列化策略是StringRedisSerializer
         *改成JDK序列化机制
         */
        adapter.setSerializer(new JdkSerializationRedisSerializer());
        return adapter;
    }

    /**
     * 客户端接收消息后，通过PatternTopic派发消息到对应的消息监听者，并构造一个线程池任务来调用MessageListener
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅所有news.* 频道内容
        container.addMessageListener(listenerAdapter, new PatternTopic("news.*"));
        return container;
    }

    class MyRedisChannelListener implements MessageListener {
        @Override
        public void onMessage(Message message, byte[] pattern) {
            byte[] channel = message.getChannel();
            byte[] bs = message.getBody();
            String content = new String(bs, StandardCharsets.UTF_8);
            String p = new String(channel, StandardCharsets.UTF_8);
            System.out.println("get " + content + " from " + p);
        }
    }
}
