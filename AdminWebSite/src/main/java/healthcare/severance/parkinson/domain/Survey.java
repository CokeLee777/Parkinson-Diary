package healthcare.severance.parkinson.domain;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Survey {

    @Id @GeneratedValue
    @Column(name = "survey_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "survery_time", nullable = false)
    private LocalDateTime surveyTime;

    @Column(name = "medicinal_effect", nullable = false)
    private Double medicinalEffect;

    @Column(name = "abnormal_movement", nullable = false)
    private Double abnormalMovement;

    @Column(name = "patient_condition", nullable = false)
    private Double patientCondition;
}
