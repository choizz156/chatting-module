package me.choizz.chattingmongomodule.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginUserRepository extends MongoRepository<LoginUser, Long> {

}
