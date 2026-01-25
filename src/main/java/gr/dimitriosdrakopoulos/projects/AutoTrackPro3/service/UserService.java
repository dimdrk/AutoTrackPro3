package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectAlreadyExists;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectInvalidArgumentException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotFoundException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.UserFilters;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.Paginated;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications.UserSpecification;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper.UserMapper;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional(rollbackOn = Exception.class)
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws AppObjectAlreadyExists, AppObjectInvalidArgumentException {

        if (userRepository.findByUsername(userInsertDTO.getUsername()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with username: " + userInsertDTO.getUsername() + " already exist.");
        }

        if (userRepository.findByEmail(userInsertDTO.getEmail()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with email: " + userInsertDTO.getEmail() + " already exist.");
        }

        if (userRepository.findByDriverLicence(userInsertDTO.getDriverLicence()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with driver licence: " + userInsertDTO.getDriverLicence() + " already exist.");
        }

        if (userRepository.findByIdentityNumber(userInsertDTO.getIdentityNumber()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with identity number: " + userInsertDTO.getIdentityNumber() + " already exist.");
        }

        // if (userInsertDTO.getRole().toString() != "OWNER") {
        //     throw new AppObjectInvalidArgumentException("User", "User with role: " + userInsertDTO.getRole() + " is valid.");
        // }

        User user = userMapper.mapToUserEntity(userInsertDTO);
        User savedUser = userRepository.save(user);
        return userMapper.mapToUserReadOnlyDTO(savedUser);
    }

    // ToDo
    @Transactional(rollbackOn = Exception.class)
    public UserReadOnlyDTO updateUser(Long id, UserInsertDTO userUpdateDTO) throws AppObjectNotFoundException {
        
        if (userRepository.findById(id).isEmpty()) {
            throw new AppObjectNotFoundException("User", "User with id: " + id + " not found.");
        }
        
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userUpdateDTO.getUsername());
        user.setPassword(userUpdateDTO.getPassword());
        user.setFirstname(userUpdateDTO.getFirstname());
        user.setLastname(userUpdateDTO.getLastname());
        user.setEmail(userUpdateDTO.getEmail());
        user.setGender(userUpdateDTO.getGender());
        user.setDriverLicence(userUpdateDTO.getDriverLicence());
        user.setLicenceExpiration(userUpdateDTO.getLicenceExpiration());
        user.setLicenceCategory(userUpdateDTO.getLicenceCategory());
        user.setIdentityNumber(userUpdateDTO.getIdentityNumber());
        user.setCity(userUpdateDTO.getCity());
        user.setIsActive(userUpdateDTO.getIsActive());

        User updatedUser = userRepository.save(user);
        return userMapper.mapToUserReadOnlyDTO(updatedUser);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(Long id) throws AppObjectNotFoundException {

        if (userRepository.findById(id).isEmpty()) {
            throw new AppObjectNotFoundException("User", "User with id: " + id + " not found.");
        }

        userRepository.deleteById(id);
    }

    // Usefull for delete
    public UserReadOnlyDTO getUserById(Long id) throws AppObjectNotFoundException {

        if (userRepository.findById(id).isEmpty()) {
            throw new AppObjectNotFoundException("User", "User with id: " + id + " not found.");
        }

        User user = userRepository.findById(id).get();
        UserReadOnlyDTO userToReturn = userMapper.mapToUserReadOnlyDTO(user);
        return userToReturn;
    }

    public Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) {

        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        return userRepository.findAll(pageable).map(userMapper::mapToUserReadOnlyDTO);
    }

    public Page<UserReadOnlyDTO> getPaginatedSortedUsers(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository.findAll(pageable).map(userMapper::mapToUserReadOnlyDTO);
    }

    @org.springframework.transaction.annotation.Transactional
    public Paginated<UserReadOnlyDTO> getUsersFilteredPaginated(UserFilters filters) {
        var filtered = userRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(userMapper::mapToUserReadOnlyDTO));
    }

    @org.springframework.transaction.annotation.Transactional
    public List<UserReadOnlyDTO> getUsersFiltered(UserFilters filters) {
        return userRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(userMapper::mapToUserReadOnlyDTO).toList();
    }

    private Specification<User> getSpecsFromFilters(UserFilters filters) {
        return Specification
                .where(UserSpecification.userStringFieldLike("uuid", filters.getUuid()))
                .and(UserSpecification.userUsernameIs(filters.getUsername()))
                .and(UserSpecification.userEmailIs(filters.getEmail()))
                .and(UserSpecification.userIsActive(filters.getActive()));
    }

}
