package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Color;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Fuel;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gearbox;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.VehicleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.RoleType;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectInvalidArgumentException;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
public class Vehicle extends AbstractEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceRecord> serviceRecords = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "vehicle_owners",
        joinColumns = @JoinColumn(name = "vehicle_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> owners = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "vehicle_drivers",
        joinColumns = @JoinColumn(name = "vehicle_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> drivers = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void validateVehicleUserRelationships() throws AppObjectInvalidArgumentException {
        // Ensure at least one owner
        if (owners == null || owners.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Vehicle", "Vehicle must have at least one owner");
        }

        // Validate all owners have OWNER role
        for (User owner : owners) {
            if (owner.getRoleType() != RoleType.OWNER) {
                throw new AppObjectInvalidArgumentException("Vehicle", "User with username '" + owner.getUsername() + "' is not an OWNER");
            }
        }

        // Validate all drivers have DRIVER role
        if (drivers != null) {
            for (User driver : drivers) {
                if (driver.getRoleType() != RoleType.DRIVER) {
                    throw new AppObjectInvalidArgumentException("Vehicle", "User with username '" + driver.getUsername() + "' is not a DRIVER");
                }
            }
        }

        // Validate no SUPER_ADMIN in owners or drivers
        if (owners != null) {
            for (User owner : owners) {
                if (owner.getRoleType() == RoleType.SUPER_ADMIN) {
                    throw new AppObjectInvalidArgumentException("Vehicle", "SUPER_ADMIN users cannot be vehicle owners");
                }
            }
        }
        if (drivers != null) {
            for (User driver : drivers) {
                if (driver.getRoleType() == RoleType.SUPER_ADMIN) {
                    throw new AppObjectInvalidArgumentException("Vehicle", "SUPER_ADMIN users cannot be vehicle drivers");
                }
            }
        }
    }

    // Helper methods for managing relationships
    public void addOwner(User user) throws AppObjectInvalidArgumentException {
        if (user.getRoleType() != RoleType.OWNER) {
            throw new AppObjectInvalidArgumentException("Vehicle", "User must have OWNER role");
        }
        if (user.getRoleType() == RoleType.SUPER_ADMIN) {
            throw new AppObjectInvalidArgumentException("Vehicle", "SUPER_ADMIN users cannot be vehicle owners");
        }
        if (!this.owners.contains(user)) {
            this.owners.add(user);
            user.getOwnedVehicles().add(this);
        }
    }

    public void removeOwner(User user) throws AppObjectInvalidArgumentException {
        if (this.owners.size() <= 1) {
            throw new AppObjectInvalidArgumentException("Vehicle", "Cannot remove the last owner. Vehicle must have at least one owner");
        }
        this.owners.remove(user);
        user.getOwnedVehicles().remove(this);
    }

    public void addDriver(User user) throws AppObjectInvalidArgumentException {
        if (user.getRoleType() != RoleType.DRIVER) {
            throw new AppObjectInvalidArgumentException("Vehicle", "User must have DRIVER role");
        }
        if (user.getRoleType() == RoleType.SUPER_ADMIN) {
            throw new AppObjectInvalidArgumentException("Vehicle", "SUPER_ADMIN users cannot be vehicle drivers");
        }
        if (!this.drivers.contains(user)) {
            this.drivers.add(user);
            user.getDrivenVehicles().add(this);
        }
    }

    public void removeDriver(User user) {
        this.drivers.remove(user);
        user.getDrivenVehicles().remove(this);
    }
}
