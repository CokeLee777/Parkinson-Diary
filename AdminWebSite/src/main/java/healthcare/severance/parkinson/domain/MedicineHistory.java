package healthcare.severance.parkinson.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "medicine_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class MedicineHistory {

    @Id
    @Column(name = "medicine_history_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_num", nullable = false)
    private final Patient patient;

    @Column(name = "reserved_take_time", nullable = false)
    private final LocalDateTime reservedTakeTime;

    @Column(name = "actual_take_time")
    private final LocalDateTime actualTakeTime;

    @ColumnDefault("false")
    @Column(name = "is_take", nullable = false)
    private final Boolean isTake;
}
