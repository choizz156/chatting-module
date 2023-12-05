package me.choizz.apimodule.auth.dto;

import java.io.Serializable;
import lombok.Builder;

@Builder
public record LoginUser(Long userId, String email, String nickname, String roles) implements Serializable {
}
