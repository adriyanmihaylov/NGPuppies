package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.dto.ClientDTO;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.base.AuthorityService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.models.viewModels.ClientViewModel;
import com.paymentsystem.ngpuppies.validation.anotations.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/client")
public class AdminClientsController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
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
    public ClientDTO registerClient() {
        return new ClientDTO();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerClient(@RequestBody @Valid ClientDTO clientDTO,
                                                          BindingResult bindingResult) {
        try {
            if (clientService.create(clientDTO)) {
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
                                                        @RequestBody @Valid ClientDTO clientDto,
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