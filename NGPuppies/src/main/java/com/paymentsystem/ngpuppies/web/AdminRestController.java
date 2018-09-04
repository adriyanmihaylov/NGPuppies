package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.dto.AdminDTO;
import com.paymentsystem.ngpuppies.models.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.models.dto.ClientDTO;
import com.paymentsystem.ngpuppies.models.dto.SubscriberDTO;
import com.paymentsystem.ngpuppies.models.users.*;
import com.paymentsystem.ngpuppies.services.base.*;
import com.paymentsystem.ngpuppies.viewModels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}")
public class AdminRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private ClientDetailService clientDetailService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private OfferedServicesService offeredServicesService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

    @GetMapping("/user")
    public ResponseEntity<UserViewModel> getUserByUsername(@RequestParam("username") String username) {
        UserViewModel viewModel = UserViewModel.fromModel((User) userService.loadUserByUsername(username));
        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin")
    public ResponseEntity<AdminViewModel> getAdminByUsername(@RequestParam("username") String username) {
        AdminViewModel viewModel = AdminViewModel.fromModel(adminService.loadByUsername(username));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/client")
    public ResponseEntity<ClientViewModel> getClientByUsername(@RequestParam("username") String username) {
        ClientViewModel viewModel = ClientViewModel.fromModel(clientService.loadByUsername(username));
        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/subscriber")
    public ResponseEntity<SubscriberSimpleViewModel> getByNumber(@RequestParam("phoneNumber") String phoneNumber) {
        SubscriberSimpleViewModel viewModel = SubscriberSimpleViewModel.fromModel(subscriberService.getByNumber(phoneNumber));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get/users")
    public ResponseEntity<List<UserViewModel>> getAllUsers() {
        List<UserViewModel> viewModels = userService.getAll().stream()
                .map(UserViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get/admins")
    public ResponseEntity<List<AdminViewModel>> getAllAdmins() {
        List<AdminViewModel> viewModels = adminService.getAll().stream()
                .map(AdminViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get/clients")
    public ResponseEntity<List<ClientViewModel>> getAllClients() {
        List<ClientViewModel> viewModels = clientService.getAll().stream()
                .map(ClientViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get/subscribers")
    public ResponseEntity<List<SubscriberSimpleViewModel>> getAllSubscribers() {
        List<SubscriberSimpleViewModel> viewModels = subscriberService.getAll().stream()
                .map(SubscriberSimpleViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/account")
    public ResponseEntity<AdminViewModel> getAccount(Authentication authentication) {
        AdminViewModel viewModel = AdminViewModel.fromModel(adminService.loadByUsername(authentication.getName()));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/account/update")
    public ResponseEntity<String> updateAccount(@Valid @RequestBody AdminDTO adminDTO,
                                                BindingResult bindingResult,
                                                Authentication authentication) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        try {
            Admin admin = (Admin) authentication.getPrincipal();
            admin.setUsername(adminDTO.getUsername());
            admin.setEmail(adminDTO.getEmail());

            if (adminDTO.getPassword() != null) {
                admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
                admin.setLastPasswordResetDate(new Date());
            }

            if (!adminService.update(admin)) {
                return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Please try again later", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Account updated!");
    }

    @GetMapping("/register/admin")
    public AdminDTO registerAdmin() {
        return new AdminDTO();
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody AdminDTO adminDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (adminDTO.getPassword() == null) {
            return new ResponseEntity<>("Missing password!", HttpStatus.BAD_REQUEST);
        }
        try {
            Authority authority = authorityService.getByName(AuthorityName.ROLE_ADMIN);
            Admin admin = new Admin(adminDTO.getUsername(), adminDTO.getPassword(), adminDTO.getEmail(), authority);

            if (!adminService.create(admin)) {
                return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successful registration!", HttpStatus.OK);
    }


    @PutMapping("/update/admin")
    public ResponseEntity<String> updateAdmin(@RequestParam String username,
                                              @Valid @RequestBody AdminDTO adminDTO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        try {
            Admin admin = adminService.loadByUsername(username);
            if (admin == null) {
                return new ResponseEntity<>(adminDTO.getUsername() + " was not found!", HttpStatus.BAD_REQUEST);
            }

            admin.setUsername(adminDTO.getUsername());
            admin.setEmail(adminDTO.getEmail());
            if (adminDTO.getPassword() != null) {
                admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
                admin.setLastPasswordResetDate(new Date());
            }

            if (!adminService.update(admin)) {
                return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successful update!", HttpStatus.OK);
    }

    @GetMapping("/register/client")
    public ClientDTO registerClient() {
        return new ClientDTO();
    }

    @PostMapping("/register/client")
    public ResponseEntity<String> registerClient(@Valid @RequestBody ClientDTO clientDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (clientDto.getPassword() == null) {
            return new ResponseEntity<>("Missing password", HttpStatus.BAD_REQUEST);
        }
        try {
            Authority authority = authorityService.getByName(AuthorityName.ROLE_CLIENT);
            Client client = new Client(clientDto.getUsername(),
                    clientDto.getPassword(),
                    clientDto.getEik(),
                    authority,
                    clientDto.getDetails());

            if (!clientService.create(client)) {
                return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successful registration!", HttpStatus.OK);
    }

    @GetMapping("/create/subscriber")
    public SubscriberDTO createSubscriber() {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setAddress(new Address());

        return subscriberDTO;
    }

    @PostMapping("/create/subscriber")
    public ResponseEntity<String> createSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        try {
            Subscriber subscriber = new Subscriber(subscriberDTO.getFirstName(),
                    subscriberDTO.getLastName(),
                    subscriberDTO.getPhone(),
                    subscriberDTO.getEgn(),
                    subscriberDTO.getAddress());

            if (subscriberDTO.getClient() != null) {
                Client client = clientService.loadByUsername(subscriberDTO.getClient());
                if (client == null) {
                    return new ResponseEntity<>("Client not found", HttpStatus.BAD_REQUEST);
                }

                subscriber.setClient(client);
            }

            if (!subscriberService.create(subscriber)) {
                return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Subscriber created!", HttpStatus.OK);
    }

    @PutMapping("/update/subscriber")
    public ResponseEntity<String> updateSubscriber(@RequestParam() String phoneNumber,
                                                   @Valid @RequestBody SubscriberDTO subscriberDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        try {
            Subscriber subscriber = subscriberService.getByNumber(phoneNumber);
            if (subscriber == null) {
                return new ResponseEntity<>("Subscriber not found!", HttpStatus.BAD_REQUEST);
            }
            subscriber.setPhone(subscriberDTO.getPhone());
            subscriber.setFirstName(subscriberDTO.getFirstName());
            subscriber.setLastName(subscriberDTO.getLastName());

            if (subscriberDTO.getClient() != null) {
                if (subscriber.getClient() != null && !subscriberDTO.getClient().equals(subscriber.getClient().getUsername())) {
                    Client client = clientService.loadByUsername(subscriberDTO.getClient());
                    if (client != null) {
                        subscriber.setClient(client);
                    } else {
                        return new ResponseEntity<>("Client not found!", HttpStatus.BAD_REQUEST);
                    }
                }
            }

            Address address = addressService.getById(subscriberDTO.getAddress().getId());

            if (address != null) {
                subscriber.setAddress(address);
            } else {
                if (subscriber.getAddress() != null) {
                    int id = subscriber.getAddress().getId();
                    subscriber.setAddress(subscriberDTO.getAddress());
                    subscriber.getAddress().setId(id);
                } else {
                    return new ResponseEntity<>("Please create the address first!", HttpStatus.BAD_REQUEST);
                }
            }

            if (!subscriberService.update(subscriber)) {
                return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Subscriber updated!", HttpStatus.OK);
    }


    @PutMapping("/update/client")
    public ResponseEntity<String> updateClient(@RequestParam() String username,
                                               @Valid @RequestBody ClientDTO clientDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        try {
            Client client = clientService.loadByUsername(username);
            if (client == null) {
                return new ResponseEntity<>("Client not found!", HttpStatus.BAD_REQUEST);
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
                return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Client updated!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<String> deleteUserByUsername(@RequestParam() String username) {
        User user = (User) userService.loadUserByUsername(username);
        if (!userService.delete(user)) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>("User " + username + " deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/subscriber")
    public ResponseEntity<String> deleteByNumber(@RequestParam("number") String phoneNumber) {
        Subscriber subscriber = subscriberService.getByNumber(phoneNumber);

        if (subscriberService.delete(subscriber)) {
            return new ResponseEntity<>("Subscriber not found!", HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>("Subscriber deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/generate/invoice")
    public InvoiceDTO createInvoice() {
        return new InvoiceDTO();
    }

    @PostMapping("/generate/invoice")
    public ResponseEntity<String> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        try {

            Subscriber subscriber = subscriberService.getByNumber(invoiceDTO.getSubscriberPhone());
            if (subscriber == null) {
                return new ResponseEntity<>("Subscriber phone not found!", HttpStatus.BAD_REQUEST);
            }

            OfferedServices offeredServices = offeredServicesService.getByName(invoiceDTO.getService().toUpperCase());
            if (offeredServices == null) {
                return new ResponseEntity<>("Offered service not found!", HttpStatus.BAD_REQUEST);
            }

            Currency currency = currencyService.getByName(invoiceDTO.getCurrency().toUpperCase());
            if (currency == null) {
                return new ResponseEntity<>("Currency not found!", HttpStatus.BAD_REQUEST);
            }

            Invoice invoice = new Invoice();
            invoice.setSubscriber(subscriber);
            invoice.setCurrency(currency);
            invoice.setStartDate(dateFormat.parse(invoiceDTO.getStartDate()));
            invoice.setEndDate(dateFormat.parse(invoiceDTO.getEndDate()));
            invoice.setAmount(Double.parseDouble(invoiceDTO.getAmount()));
            invoice.setOfferedServices(offeredServices);

            invoiceService.create(invoice);

        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Invoice successfully added!", HttpStatus.OK);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleAuthenticationException(SQLException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}