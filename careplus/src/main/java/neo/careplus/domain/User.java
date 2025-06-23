package neo.careplus.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
    private List<Email> emailList = new ArrayList<>();

    private String password;

    private String name;

}
