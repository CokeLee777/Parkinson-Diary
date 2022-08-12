package healthcare.severance.parkinson.domain;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Survey {

    @Id @GeneratedValue
    @Column(name = "SURVEY_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(nullable = false)
    private LocalDateTime surveyTime;

    private Double medicinalEffect;
    private Double abnormalMovement;
    private Double userCondition;
}
