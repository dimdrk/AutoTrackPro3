package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_records")
public class ServiceRecord extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate dateOfService;

    @Column(nullable = false)
    private String serviceType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String odometer;

    @Column(nullable = false)
    private String parts;

    @Column(nullable = false)
    private String cost;

    @Column(nullable = false)
    private LocalDate nextService;

    @Column(nullable = false)
    private String recommentdations;

    @Column(nullable = false)
    private String warranty;
    
}
