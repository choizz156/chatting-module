package me.choizz.domainjpamodule.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.choizz.domainjpamodule.BaseJpaEntity;
import org.hibernate.proxy.HibernateProxy;

@ToString(of = "email")
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole roles;


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

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass = o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer()
            .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        final User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass().hashCode() : getClass().hashCode();
    }
}
