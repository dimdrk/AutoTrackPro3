package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters;

import java.time.LocalDate;

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
public class ServiceRecordFilters extends GenericFilters {

    private String id;

    private LocalDate dateOfService;

    private String licencePlate;
    
}
