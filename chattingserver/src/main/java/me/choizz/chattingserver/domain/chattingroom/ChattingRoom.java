package me.choizz.chattingserver.domain.chattingroom;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.choizz.chattingserver.domain.BaseJpaEntity;
import me.choizz.chattingserver.domain.user.User;


@Getter
@NoArgsConstructor
@Entity
@Table
public class ChattingRoom extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    public void makeChattingRoom(User host, User client){
        if(this.host != null){
            this.host.getHostChattingRooms().remove(this);
        }

        if(this.client != null){
            this.client.getClientChattingRooms().remove(this);
        }

        this.host = host;
        this.client = client;

        this.host.getHostChattingRooms().add(this);
        this.client.getClientChattingRooms().add(this);
    }



    public ChattingRoom(final String roomName) {
        this.roomName = roomName;
    }
}
