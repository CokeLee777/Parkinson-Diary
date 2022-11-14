package healthcare.severance.parkinson.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "medicine")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder @Getter
public class Medicine {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private final Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_num", nullable = false)
    private final Patient patient;

    @Column(name = "take_time",nullable = false)
    private final LocalDateTime takeTime = LocalDateTime.now();

    @ColumnDefault("false")
    @Column(name = "is_take",nullable = false)
    private final Boolean isTake;
}
