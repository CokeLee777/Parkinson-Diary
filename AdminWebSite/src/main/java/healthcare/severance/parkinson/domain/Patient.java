package healthcare.severance.parkinson.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

//@DynamicInsert
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue
    @Column(name = "patient_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "patient_name",nullable = false)
    private String name;

//    @ColumnDefault("22:00:00")
    @Column(name = "sleep_start_time")
    private LocalDateTime sleepStartTime;

//    @ColumnDefault("08:00:00")
    @Column(name = "sleep_end_time")
    private LocalDateTime sleepEndTime;
}
