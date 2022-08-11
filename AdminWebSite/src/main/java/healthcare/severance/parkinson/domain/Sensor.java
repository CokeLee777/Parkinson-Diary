package healthcare.severance.parkinson.domain;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Sensor {

    @Id @GeneratedValue
    @Column(name = "SENSOR_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private LocalDateTime sensorTime;
    private double accSensorX;
    private double accSensorY;
    private double accSensorZ;
}
