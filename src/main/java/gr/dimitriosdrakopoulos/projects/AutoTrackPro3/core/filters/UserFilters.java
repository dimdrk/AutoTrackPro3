package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserFilters extends GenericFilters {

    @Nullable
    private String uuid;

    @Nullable
    private String driverLicence;

    @Nullable
    private String identityNumber;

    @Nullable
    private String username;

    @Nullable
    private String email;

    @Nullable
    private Boolean active;
}
