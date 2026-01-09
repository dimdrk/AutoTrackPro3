package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model;

import java.time.LocalDate;

import gr.dimitriosdrakopoulos.core.enums.Color;
import gr.dimitriosdrakopoulos.core.enums.Fuel;
import gr.dimitriosdrakopoulos.core.enums.Gearbox;
import gr.dimitriosdrakopoulos.core.enums.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "vehicles")
public class Vehicle extends AbstractEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, unique = true)
    private String vin;

    @Column(unique=true)
    private String licencePlate;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(nullable = false)
    private LocalDate productionDate;

    @Enumerated(EnumType.STRING)
    private Fuel fuel;

    @Enumerated(EnumType.STRING)
    private Gearbox gearbox;

    @Column(nullable = false)
    private String odometer;
}
