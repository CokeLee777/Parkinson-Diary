package healthcare.severance.parkinson.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private final Long id;

    @Column(name = "identifier", nullable = false)
    private final String identifier;

    @Column(name = "password", nullable = false)
    private final String password;

    @Column(name = "username", nullable = false)
    private final String username;

    @Column(name = "email", nullable = false)
    private final String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private final RoleType role;

}
