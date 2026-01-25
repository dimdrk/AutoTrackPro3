package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.rest;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.*;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.Paginated;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.UserFilters;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;


    @PostMapping("/users/all")
    public ResponseEntity<List<UserReadOnlyDTO>> getUsers(@Nullable @RequestBody UserFilters filters, Principal principal)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = UserFilters.builder().build();
            return ResponseEntity.ok(userService.getUsersFiltered(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get users.", e);
            throw e;
        }
    }
    
    @PostMapping("/users/all/paginated")
    public ResponseEntity<Paginated<UserReadOnlyDTO>> getUsersPaginated(@Nullable @RequestBody UserFilters filters, Principal principal)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = UserFilters.builder().build();
            return ResponseEntity.ok(userService.getUsersFilteredPaginated(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get users.", e);
            throw e;
        }
    }

    @PatchMapping("/user/update")
    public ResponseEntity<UserReadOnlyDTO> updateUser(
            @RequestParam(name = "id") Long id,
            @Valid @RequestBody UserInsertDTO userUpdateDTO ) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, AppServerException {        

        UserReadOnlyDTO userReadOnlyDTO = userService.updateUser(id, userUpdateDTO);
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<UserReadOnlyDTO> deleteUser(
                @RequestParam(name = "id") Long id) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, AppServerException {
        
                    UserReadOnlyDTO userReadOnlyDTO = userService.getUserById(id);
                    userService.deleteUser(id);
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }
}
