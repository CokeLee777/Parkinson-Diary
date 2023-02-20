package healthcare.severance.parkinson.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder @Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private final Long id;

    @Column(name = "identifier", nullable = false, unique = true)
    private final String identifier;

    @Column(name = "password", nullable = false)
    private final String password;

    @Column(name = "username", nullable = false)
    private final String username;

    @Column(name = "email", nullable = false, unique = true,
            columnDefinition = "VARCHAR(255) CHECK (email LIKE '%@%.%')")
    private final String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role;

    public void signUser() {
        this.role = RoleType.DOCTOR;
    }
    public void unsignUser() {
        this.role = RoleType.UNSIGNED;
    }
}
