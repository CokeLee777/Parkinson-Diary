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
    @Column(name = "MEDICINE_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(nullable = false)
    private LocalDateTime takeTime;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean takeOrNot;
}
