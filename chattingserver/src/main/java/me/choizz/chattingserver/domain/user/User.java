package me.choizz.chattingserver.domain.user;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.choizz.chattingserver.domain.BaseJpaEntity;
import me.choizz.chattingserver.domain.chattingroom.ChattingRoom;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false)
    private String email;

    private String password;

    private String nickname;

    @Enumerated(STRING)
    private UserRole roles;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChattingRoom> hostChattingRooms = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChattingRoom> clientChattingRooms = new ArrayList<>();

    @Builder
    public User(
        final String email,
        final String password,
        final String nickname,
        final UserRole roles
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    public void savePassword(final String password) {
        this.password = password;
    }
}
