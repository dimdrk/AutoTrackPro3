package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service.UserService;
import jakarta.validation.Valid;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectAlreadyExists;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectInvalidArgumentException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppServerException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserReadOnlyDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterRestController {

    private final UserService userService;
    
    @PostMapping("/save")
    public ResponseEntity<UserReadOnlyDTO> saveUser(
            @Valid @RequestBody UserInsertDTO userInsertDTO) throws AppObjectInvalidArgumentException, AppObjectAlreadyExists, AppServerException {

        UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }
}
