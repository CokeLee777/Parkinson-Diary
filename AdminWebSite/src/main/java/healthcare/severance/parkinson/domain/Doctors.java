package healthcare.severance.parkinson.domain;

import javax.persistence.*;

@Entity
public class Doctors {

    @Id @GeneratedValue
    @Column(name = "DOCTOR_ID")
    private Long id;

    @Column(nullable = false)
    private String doctorName, doctorPassword, doctorEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

}
