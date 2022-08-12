package healthcare.severance.parkinson.domain;

import javax.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id @GeneratedValue
    @Column(name = "DOCTOR_ID")
    private Long id;

    @Column(nullable = false)
    private String doctorName,doctorAccountPassword, doctorEmail, doctorAccountName;;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

}
