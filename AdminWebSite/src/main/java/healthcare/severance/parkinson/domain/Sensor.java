package healthcare.severance.parkinson.domain;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Sensor {

    @Id @GeneratedValue
    @Column(name = "sensor_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "sensor_time", nullable = false)
    private LocalDateTime sensorTime;

    @Column(name = "acc_sensor_x", nullable = false)
    private double accSensorX;

    @Column(name = "acc_sensor_y", nullable = false)
    private double accSensorY;

    @Column(name = "acc_sensor_z", nullable = false)
    private double accSensorZ;
}
