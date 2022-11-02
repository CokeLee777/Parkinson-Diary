package healthcare.severance.parkinson.domain;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "sensor")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder @Getter
public class Sensor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private final Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_num", nullable = false)
    private final Patient patient;

    @Column(name = "sensor_time", nullable = false)
    private final LocalDateTime sensorTime;

    @Column(name = "acc_sensor_x", nullable = false)
    private final double accSensorX;

    @Column(name = "acc_sensor_y", nullable = false)
    private final double accSensorY;

    @Column(name = "acc_sensor_z", nullable = false)
    private final double accSensorZ;
}
