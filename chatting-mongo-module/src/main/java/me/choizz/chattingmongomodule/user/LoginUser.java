package me.choizz.chattingmongomodule.user;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
public class LoginUser {
//todo: dto 만들기
   @Id
   private String id;
   private Long userId;
   private String email;
   private String nickname;
}
