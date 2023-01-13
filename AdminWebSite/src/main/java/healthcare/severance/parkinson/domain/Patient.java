package healthcare.severance.parkinson.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "patients")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder @Getter
public class Patient {

    @Id
    @Column(name = "patient_num")
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

    @Column(name = "fcm_registration_token")
    private String fcmRegistrationToken;

    public void EditPatient(User user, String name, LocalTime sleepStartTime, LocalTime sleepEndTime) {
        this.user = user;
        this.name = name;
        this.sleepStartTime = sleepStartTime;
        this.sleepEndTime = sleepEndTime;
    }
}
