package healthcare.severance.parkinson.domain;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder @Getter
public class Survey {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private final Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_num", nullable = false)
    private final Patient patient;

    @Column(name = "survey_time", nullable = false)
    private final LocalDateTime surveyTime;

    @Column(name = "medicinal_effect", nullable = false)
    private final Boolean medicinalEffect;

    @Column(name = "abnormal_movement", nullable = false)
    private final Boolean abnormalMovement;

    @Column(name = "patient_condition", nullable = false)
    private final Double patientCondition;
}
