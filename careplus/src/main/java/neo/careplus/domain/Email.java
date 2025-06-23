package neo.careplus.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Email {

    @Id @GeneratedValue
    @Column(name = "email_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String email;

    private int verificationCode;

    private boolean verified;

    private LocalDateTime create_at;

    private LocalDateTime expires_at;

}
