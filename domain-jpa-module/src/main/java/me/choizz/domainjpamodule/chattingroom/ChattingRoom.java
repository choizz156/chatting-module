package me.choizz.domainjpamodule.chattingroom;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.choizz.domainjpamodule.BaseJpaEntity;
import me.choizz.domainjpamodule.user.User;


@Getter
@NoArgsConstructor
@Entity
@Table
public class ChattingRoom extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    public ChattingRoom(final String roomName) {
        this.roomName = roomName;
    }

    public void makeChattingRoom(User host, User client){
        this.host = host;
        this.client = client;
    }

    public String getHostNickName(){
        return this.host.getNickname();
    }

    public String getClientName(){
        return this.client.getNickname();
    }
}
