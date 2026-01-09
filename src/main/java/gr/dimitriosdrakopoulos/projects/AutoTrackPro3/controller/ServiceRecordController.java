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
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service.ServiceRecordService;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service.VehicleService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/service-records")
@RequiredArgsConstructor
public class ServiceRecordController {
    
    private final ServiceRecordService serviceRecordService;
    private final VehicleService vehicleService;

    @GetMapping
    public String listServiceRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ServiceRecordReadOnlyDTO> records = serviceRecordService.getPaginatedServiceRecord(page, size);
        model.addAttribute("serviceRecords", records);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "service-records/list";
    }

    @GetMapping("/add")
    public String addServiceRecordForm(Model model) {
        model.addAttribute("serviceRecordInsertDTO", new ServiceRecordInsertDTO());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "service-records/add";
    }

    @PostMapping("/save")
    public String saveServiceRecord(ServiceRecordInsertDTO serviceRecordInsertDTO, Model model) {
        try {
            serviceRecordService.saveServiceRecord(serviceRecordInsertDTO);
            return "redirect:/service-records";
        } catch (AppObjectAlreadyExists e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("serviceRecordInsertDTO", serviceRecordInsertDTO);
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            return "service-records/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editServiceRecordForm(@PathVariable Long id, Model model) {
        try {
            ServiceRecordReadOnlyDTO record = serviceRecordService.getServiceRecordById(id);
            model.addAttribute("serviceRecord", record);
            model.addAttribute("serviceRecordId", id);
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            return "service-records/edit";
        } catch (AppObjectNotFoundException e) {
            model.addAttribute("error", "Service record not found");
            return "redirect:/service-records";
        }
    }

    @PostMapping("/update/{id}")
    public String updateServiceRecord(@PathVariable Long id, ServiceRecordInsertDTO serviceRecordInsertDTO, Model model) {
        try {
            serviceRecordService.updateServiceRecord(id, serviceRecordInsertDTO);
            return "redirect:/service-records";
        } catch (AppObjectNotFoundException e) {
            model.addAttribute("error", "Service record not found");
            return "redirect:/service-records";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteServiceRecord(@PathVariable Long id) {
        try {
            serviceRecordService.deleteServiceRecord(id);
            return "redirect:/service-records";
        } catch (AppObjectNotFoundException e) {
            return "redirect:/service-records";
        }
    }
}
