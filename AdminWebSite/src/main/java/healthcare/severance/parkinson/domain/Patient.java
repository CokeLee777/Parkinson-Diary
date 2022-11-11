package healthcare.severance.parkinson.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

//@DynamicInsert
@Entity
@Table(name = "patients")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder @Getter
public class Patient {

    @Id
    @Column(name = "patient_num", nullable = false, unique = true)
    private Long patientNum;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "patient_name",nullable = false)
    private String name;

    @Column(name = "sleep_start_time")
    private LocalTime sleepStartTime;

    @Column(name = "sleep_end_time")
    private LocalTime sleepEndTime;

    public void EditPatient(Long patientNum, User user, String name, LocalTime sleepStartTime, LocalTime sleepEndTime) {
        this.patientNum = patientNum;
        this.user = user;
        this.name = name;
        this.sleepStartTime = sleepStartTime;
        this.sleepEndTime = sleepEndTime;
    }
}
