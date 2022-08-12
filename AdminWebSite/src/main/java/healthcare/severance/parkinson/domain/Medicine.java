package healthcare.severance.parkinson.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@DynamicInsert
@Entity
public class Medicine {

    @Id @GeneratedValue
    @Column(name = "medicine_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "take_time",nullable = false)
    private LocalDateTime takeTime;

    @ColumnDefault("false")
    @Column(name = "is_take",nullable = false)
    private Boolean isTake;
}
