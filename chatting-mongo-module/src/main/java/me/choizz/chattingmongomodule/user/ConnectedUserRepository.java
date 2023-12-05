package me.choizz.chattingmongomodule.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConnectedUserRepository extends MongoRepository<ConnectedUser, Long> {

    void deleteByUserId(Long userId);
}
