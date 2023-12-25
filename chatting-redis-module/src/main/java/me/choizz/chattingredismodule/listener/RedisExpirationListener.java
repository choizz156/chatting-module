package me.choizz.chattingredismodule.listener;

import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingredismodule.session.LoginUsers;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisExpirationListener extends KeyExpirationEventMessageListener {
    private final LoginUsers loginUsers;

    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    public RedisExpirationListener(
        final RedisMessageListenerContainer listenerContainer,
        final LoginUsers loginUsers
    ) {
        super(listenerContainer);
        this.loginUsers = loginUsers;
    }

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        String key = message.toString();
        loginUsers.removeValue(key);
    }
}
