package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.web.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.web.dto.TelecomServDto;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import com.paymentsystem.ngpuppies.models.viewModels.TelecomServSimpleViewModel;
import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/service")
public class AdminTelecomServController {
    private final TelecomServService telecomServService;

    @Autowired
    public AdminTelecomServController(TelecomServService telecomServService) {
        this.telecomServService = telecomServService;
    }

    @GetMapping("/all")
    public List<TelecomServSimpleViewModel> getAllServices() {
        return telecomServService.getAll().stream()
                .map(TelecomServSimpleViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/create")
    public TelecomServDto createServiceTemplate() {
        return new TelecomServDto();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createNewService(@Valid @RequestBody TelecomServDto serviceDTO,
                                                            BindingResult bindingResult) {
        try {
            if (telecomServService.create(serviceDTO)) {
                return new ResponseEntity<>(new ResponseMessage("Service" + serviceDTO.getName() + " added!"), HttpStatus.OK);
            }
        } catch (IllegalArgumentException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{name}/update")
    public ResponseEntity<ResponseMessage> updateTelecomService(@PathVariable("name") @ValidServiceName String serviceName,
                                                                @Valid @RequestBody TelecomServDto serviceDTO,
                                                                BindingResult bindingResult) {
        try {
            if (telecomServService.update(serviceName, serviceDTO)) {
                return new ResponseEntity<>(new ResponseMessage("Service updated!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("Service not found!"), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}