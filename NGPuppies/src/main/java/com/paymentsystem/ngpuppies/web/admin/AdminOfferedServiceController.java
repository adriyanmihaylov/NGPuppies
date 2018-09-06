package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.dto.Response;
import com.paymentsystem.ngpuppies.models.dto.OfferedServiceDTO;
import com.paymentsystem.ngpuppies.services.base.OfferedServicesService;
import com.paymentsystem.ngpuppies.models.viewModels.OfferedServiceSimpleViewModel;
import com.paymentsystem.ngpuppies.models.viewModels.OfferedServiceViewModel;
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
public class AdminOfferedServiceController {
    @Autowired
    private OfferedServicesService offeredServicesService;


    @GetMapping("/all")
    public ResponseEntity<List<OfferedServiceSimpleViewModel>> getAllServices() {
        List<OfferedServiceSimpleViewModel> viewModels = offeredServicesService.getAll().stream()
                .map(OfferedServiceSimpleViewModel::fromModel)
                .collect(Collectors.toList());
        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createNewService(@Valid @RequestBody OfferedServiceDTO serviceDTO,
                                                     BindingResult bindingResult) {
        if (serviceDTO.getName() == null) {
            return new ResponseEntity<>(new Response("Please enter service name"), HttpStatus.BAD_REQUEST);
        }
        try {
            OfferedServices newService = new OfferedServices(serviceDTO.getName().toUpperCase());
            offeredServicesService.create(newService);
        } catch (SQLException e) {
            return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        } catch (InternalError e) {
            return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Response("Service added successfully"), HttpStatus.OK);
    }

    @GetMapping("/{service}/subscribers")
    public ResponseEntity<OfferedServiceViewModel> getAllSubscribersOfService(@PathVariable("service") String serviceName) {
        OfferedServiceViewModel viewModel = OfferedServiceViewModel.fromModel(offeredServicesService.getByName(serviceName));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}