package me.choizz.chattingserver.websocket;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class EmbeddedAutoConfig extends EmbeddedMongoAutoConfiguration {

}
