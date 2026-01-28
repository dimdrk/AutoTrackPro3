package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto;

import java.time.LocalDate;
import java.util.List;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Color;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Fuel;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gearbox;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VehicleReadOnlyDTO {

    private Long id;
    private String vin;
    private String licencePlate;
    private String make;
    private String model;
    private VehicleType type;
    private Color color;
    private LocalDate productionDate;
    private Fuel fuel;
    private Gearbox gearbox;
    private Long odometer;
    private List<String> ownerUsernames;
    private List<String> driverUsernames;
}
