package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.web.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.web.dto.ClientDto;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.models.viewModels.ClientViewModel;
import com.paymentsystem.ngpuppies.validation.anotations.ValidUsername;
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
@RequestMapping("${common.basepath}/client")
public class AdminClientsController {
    private final ClientService clientService;

    @Autowired
    public AdminClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public ResponseEntity<ClientViewModel> getClientByUsername(@RequestParam("username") @ValidUsername String username) {
        ClientViewModel viewModel = ClientViewModel.fromModel(clientService.loadByUsername(username));
        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientViewModel>> getAllClients() {

        List<ClientViewModel> viewModels = clientService.getAll().stream()
                .map(ClientViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/register")
    public ClientDto registerClient() {
        return new ClientDto();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerClient(@RequestBody @Valid ClientDto clientDto,
                                                          BindingResult bindingResult) {
        try {
            if (clientService.create(clientDto)) {
                return new ResponseEntity<>(new ResponseMessage("Successful registration!"), HttpStatus.OK);
            }
        } catch (IllegalArgumentException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseMessage> updateClient(@RequestParam("username") @ValidUsername String username,
                                                        @RequestBody @Valid ClientDto clientDto,
                                                        BindingResult bindingResult) {
        try {
            if (clientService.update(username, clientDto)) {
                return new ResponseEntity<>(new ResponseMessage("Successful update!"), HttpStatus.OK);
            }

        } catch (IllegalArgumentException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}