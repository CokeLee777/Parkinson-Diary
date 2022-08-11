package healthcare.severance.parkinson.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@DynamicInsert
@Entity
public class Users {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DOCTOR_ID")
    private Doctors doctors;

    @Column(nullable = false)
    private String userName;

    @ColumnDefault("22:00:00")
    private LocalDateTime userSleepStartTime;

    @ColumnDefault("08:00:00")
    private LocalDateTime userSleepEndTime;
}
