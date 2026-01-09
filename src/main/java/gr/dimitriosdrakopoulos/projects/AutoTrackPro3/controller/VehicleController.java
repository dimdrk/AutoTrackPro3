package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectAlreadyExists;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotFoundException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service.VehicleService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    
    private final VehicleService vehicleService;

    @GetMapping
    public String listVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<VehicleReadOnlyDTO> vehicles = vehicleService.getPaginatedVehicles(page, size);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "vehicles/list";
    }

    @GetMapping("/add")
    public String addVehicleForm(Model model) {
        model.addAttribute("vehicleInsertDTO", new VehicleInsertDTO());
        return "vehicles/add";
    }

    @PostMapping("/save")
    public String saveVehicle(VehicleInsertDTO vehicleInsertDTO, Model model) {
        try {
            vehicleService.saveVehicle(vehicleInsertDTO);
            return "redirect:/vehicles";
        } catch (AppObjectAlreadyExists e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("vehicleInsertDTO", vehicleInsertDTO);
            return "vehicles/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editVehicleForm(@PathVariable Long id, Model model) {
        try {
            VehicleReadOnlyDTO vehicle = vehicleService.getVehicleById(id);
            model.addAttribute("vehicle", vehicle);
            return "vehicles/edit";
        } catch (AppObjectNotFoundException e) {
            model.addAttribute("error", "Vehicle not found");
            return "redirect:/vehicles";
        }
    }

    @PostMapping("/update/{id}")
    public String updateVehicle(@PathVariable Long id, VehicleInsertDTO vehicleInsertDTO, Model model) {
        try {
            vehicleService.updateVehicle(id, vehicleInsertDTO);
            return "redirect:/vehicles";
        } catch (AppObjectNotFoundException e) {
            model.addAttribute("error", "Vehicle not found");
            return "redirect:/vehicles";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return "redirect:/vehicles";
        } catch (AppObjectNotFoundException e) {
            return "redirect:/vehicles";
        }
    }
}
