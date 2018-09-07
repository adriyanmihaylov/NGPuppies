package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.dto.TelecomServiceDTO;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import com.paymentsystem.ngpuppies.models.viewModels.TelecomServSimpleViewModel;
import com.paymentsystem.ngpuppies.models.viewModels.TelecomServViewModel;
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
    @Autowired
    private TelecomServService telecomServService;

    @GetMapping("/all")
    public List<TelecomServSimpleViewModel> getAllServices() {
        return telecomServService.getAll().stream()
                .map(TelecomServSimpleViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/create")
    public TelecomServiceDTO createServiceTemplate() {
        return new TelecomServiceDTO();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createNewService(@Valid @RequestBody TelecomServiceDTO serviceDTO,
                                                            BindingResult bindingResult) {
        if (serviceDTO.getName() == null) {
            return new ResponseEntity<>(new ResponseMessage("Please enter service name"), HttpStatus.BAD_REQUEST);
        }
        try {
            TelecomServ newService = new TelecomServ(serviceDTO.getName().toUpperCase());
            telecomServService.create(newService);
        } catch (SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        } catch (InternalError e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseMessage("Service added successfully"), HttpStatus.OK);
    }

    @GetMapping("/{service}/subscribers")
    public ResponseEntity<TelecomServViewModel> getAllSubscribersOfService(@PathVariable("service") String serviceName) {
        TelecomServViewModel viewModel = TelecomServViewModel.fromModel(telecomServService.getByName(serviceName));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}