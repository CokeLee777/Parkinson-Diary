package healthcare.severance.parkinson.domain;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Survey {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private Users users;

    @Column(nullable = false)
    private LocalDateTime surveyTime;

    private double medicinalEffect;
    private double abnormalMovement;
    private double condition;
}
