package me.choizz.apimodule.auth.dto;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class LoginDto implements Serializable {

    private String email;
    private String password;
}
