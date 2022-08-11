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
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private Users users;

    @Column(nullable = false)
    private LocalDateTime takeTime;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean takeOrNot;
}
