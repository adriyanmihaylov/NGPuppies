package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Response;
import com.paymentsystem.ngpuppies.models.dto.OfferedServiceDTO;
import com.paymentsystem.ngpuppies.services.base.OfferedServicesService;
import com.paymentsystem.ngpuppies.viewModels.OfferedServiceSimpleViewModel;
import com.paymentsystem.ngpuppies.viewModels.OfferedServiceViewModel;
import com.paymentsystem.ngpuppies.web.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/service")
public class AdminOfferedServiceController {
    @Autowired
    private OfferedServicesService offeredServicesService;

    private ResponseHandler responseHandler;

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
    public ResponseEntity<Response> createNewService(@Valid @RequestBody OfferedServiceDTO serviceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseHandler.bindingResultHandler(bindingResult);
        }
        if (serviceDTO.getName() == null) {
            return responseHandler.returnResponse("Please enter service name", HttpStatus.BAD_REQUEST);
        }
        try {
            OfferedServices newService = new OfferedServices(serviceDTO.getName().toUpperCase());
            offeredServicesService.create(newService);
        } catch (SQLException e) {
            return responseHandler.returnResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (InternalError e) {
            return responseHandler.returnResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseHandler.returnResponse("Service added successfully", HttpStatus.OK);
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