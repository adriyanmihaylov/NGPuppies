package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.models.Response;
import com.paymentsystem.ngpuppies.models.dto.ClientDTO;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.base.AuthorityService;
import com.paymentsystem.ngpuppies.services.base.ClientDetailService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.viewModels.ClientViewModel;
import com.paymentsystem.ngpuppies.viewModels.UserViewModel;
import com.paymentsystem.ngpuppies.web.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/client")
public class AdminClientsController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private ClientDetailService clientDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResponseHandler responseHandler;

    @GetMapping("/{username}")
    public ResponseEntity<ClientViewModel> getClientByUsername(@PathVariable("username") String username) {
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
    public ResponseEntity<Response> registerClient(@Valid @RequestBody ClientDTO clientDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseHandler.bindingResultHandler(bindingResult);
        }

        if (clientDto.getPassword() == null) {
            return responseHandler.returnResponse("Missing password", HttpStatus.BAD_REQUEST);
        }
        try {
            Authority authority = authorityService.getByName(AuthorityName.ROLE_CLIENT);
            Client client = new Client(clientDto.getUsername(),
                    clientDto.getPassword(),
                    clientDto.getEik(),
                    authority,
                    clientDto.getDetails());

            if (!clientService.create(client)) {
                return responseHandler.returnResponse("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return responseHandler.returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return responseHandler.returnResponse("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return responseHandler.returnResponse("Successful registration!", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateClient(@RequestParam("username") String username,
                                                 @Valid @RequestBody ClientDTO clientDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseHandler.bindingResultHandler(bindingResult);
        }
        try {
            Client client = clientService.loadByUsername(username);
            if (client == null) {
                return responseHandler.returnResponse("Client not found!", HttpStatus.BAD_REQUEST);
            }

            client.setUsername(clientDto.getUsername());
            client.setEik(clientDto.getEik());

            if (clientDto.getDetails() != null) {
                ClientDetail clientDetail = clientDetailService.getById(clientDto.getDetails().getId());

                if (clientDetail != null) {
                    client.setDetails(clientDetail);
                } else {
                    int id = client.getDetails().getId();
                    client.setDetails(clientDto.getDetails());
                    client.getDetails().setId(id);

                }
            }

            if (clientDto.getPassword() != null) {
                client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
                client.setLastPasswordResetDate(new Date());
            }
            if (!clientService.update(client)) {
                return responseHandler.returnResponse("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException e) {
            return responseHandler.returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return responseHandler.returnResponse("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return responseHandler.returnResponse("Client updated!", HttpStatus.OK);
    }

}
