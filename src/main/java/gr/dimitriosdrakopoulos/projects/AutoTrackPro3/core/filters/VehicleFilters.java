package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VehicleFilters extends GenericFilters {

    private String id;

    private String vin;

    private String licencePlate;

    private String username;
    
}
