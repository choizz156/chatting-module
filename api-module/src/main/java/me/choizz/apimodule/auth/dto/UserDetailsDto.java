package me.choizz.apimodule.auth.dto;

import java.io.Serializable;
import lombok.Builder;

@Builder
public record UserDetailsDto(String email, String password, String role) implements Serializable {

}
