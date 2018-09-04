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
    public UserViewModel getUserByUsername(@RequestParam("username") String username) {
        return UserViewModel.fromModel((User) userService.loadUserByUsername(username));
    }

    @GetMapping("/admin")
    public AdminViewModel getAdminByUsername(@RequestParam("username") String username) {
        return AdminViewModel.fromModel(adminService.loadByUsername(username));
    }

    @GetMapping("/client")
    public ClientViewModel getClientByUsername(@RequestParam("username") String username) {
        return ClientViewModel.fromModel(clientService.loadByUsername(username));
    }

    @GetMapping("/subscriber")
    public SubscriberSimpleViewModel getByNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return SubscriberSimpleViewModel.fromModel(subscriberService.getByNumber(phoneNumber));
    }

    @GetMapping("/get/users")
    public List<UserViewModel> getAllUsers() {
        return userService.getAll().stream()
                .map(UserViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/get/admins")
    public List<AdminViewModel> getAllAdmins() {
        return adminService.getAll().stream()
                .map(AdminViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/get/clients")
    public List<ClientViewModel> getAllClients() {
        return clientService.getAll().stream()
                .map(ClientViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/get/subscribers")
    public List<SubscriberSimpleViewModel> getAllSubscribers() {
        return subscriberService.getAll().stream().map(SubscriberSimpleViewModel::fromModel).
                collect(Collectors.toList());
    }

    @GetMapping("/account")
    public AdminViewModel getAccount(Authentication authentication) {
        return AdminViewModel.fromModel(adminService.loadByUsername(authentication.getName()));
    }

    @PutMapping("/account/update")
    public ResponseEntity updateAccount(@Valid @RequestBody AdminDTO adminDTO, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return ResponseEntity.badRequest().body(message);
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
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }
        return ResponseEntity.ok("Account updated!");
    }

    @PutMapping("/update/subscriber")
    public ResponseEntity updateSubscriber(@RequestParam() String phoneNumber, @Valid @RequestBody SubscriberDTO subscriberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return ResponseEntity.badRequest().body(message);
        }
        try {
            Subscriber subscriber = subscriberService.getByNumber(phoneNumber);
            if (subscriber == null) {
                return ResponseEntity.badRequest().body("Subscriber was not found!");
            }
            subscriber.setPhone(subscriberDTO.getPhone());
            subscriber.setFirstName(subscriberDTO.getFirstName());
            subscriber.setLastName(subscriberDTO.getLastName());

            if (subscriberDTO.getAddress() != null && subscriber.getAddress() == null) {
                Address address = subscriberDTO.getAddress();
                addressService.create(subscriberDTO.getAddress());
                subscriber.setAddress(address);
            } else if (subscriberDTO.getAddress() != null && subscriber.getAddress() != null) {
                subscriberDTO.getAddress().setId(subscriber.getAddress().getId());
                addressService.update(subscriberDTO.getAddress());
            }
            if (!subscriberService.update(subscriber)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong! Please try again later!");
        }

        return ResponseEntity.ok("Subscriber updated successfully!");
    }

    @PutMapping("/update/client")
    public ResponseEntity updateClient(@RequestParam("username") String username, @Valid @RequestBody ClientDTO clientDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return ResponseEntity.badRequest().body(message);
        }
        try {
            Client client = clientService.loadByUsername(username);
            if (client == null) {
                return ResponseEntity.badRequest().body("Client was not found!");
            }

            client.setUsername(clientDto.getUsername());
            client.setEik(clientDto.getEik());

            if (clientDto.getPassword() != null) {
                client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
                client.setLastPasswordResetDate(new Date());
            }

            if (!clientService.update(client)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            } else {
                if (updateOrCreateClientDetails(clientDto.getDetails(), client)) {
                    clientService.update(client);
                }
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }

        return ResponseEntity.ok("Client updated successfully!");
    }

    @GetMapping("/register/admin")
    public AdminDTO registerAdmin() {
        return new AdminDTO();
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminDTO adminDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        if (adminDTO.getPassword() == null) {
            return ResponseEntity.badRequest().body("Password can not be empty!");
        }
        try {
            Admin admin = new Admin();
            admin.setUsername(adminDTO.getUsername());
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
            admin.setEmail(adminDTO.getEmail());
            Authority authority = authorityService.getByName(AuthorityName.ROLE_ADMIN);
            admin.setAuthority(authority);

            if (!adminService.create(admin)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }

        return ResponseEntity.ok("Successful registration!");
    }

    @GetMapping("/register/client")
    public ClientDTO registerClient() {
        return new ClientDTO();
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientDTO clientDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        if (clientDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("Password can not be empty!");
        }
        try {
            Client client = new Client();
            client.setUsername(clientDto.getUsername());
            client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
            client.setEik(clientDto.getEik());
            Authority authority = authorityService.getByName(AuthorityName.ROLE_CLIENT);
            client.setAuthority(authority);

            if (!clientService.create(client)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }

        return ResponseEntity.ok("Successful registration!");
    }

    @GetMapping("/create/subscriber")
    public SubscriberDTO createSubscriber() {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setAddress(new Address());

        return subscriberDTO;
    }

    @PostMapping("/create/subscriber")
    public ResponseEntity<?> createSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        try {
            Subscriber subscriber = new Subscriber();
            subscriber.setFirstName(subscriberDTO.getFirstName());
            subscriber.setLastName(subscriberDTO.getLastName());
            subscriber.setPhone(subscriberDTO.getPhone());
            subscriber.setEgn(subscriberDTO.getEgn());

            if (subscriberDTO.getAddress() != null) {
                Address address = subscriberDTO.getAddress();
                addressService.create(subscriberDTO.getAddress());
                subscriber.setAddress(address);
            }

            if (!subscriberService.create(subscriber)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }

        return ResponseEntity.ok("Subscriber created!");
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<?> deleteUserByUsername(@RequestParam() String username) {
        User user = (User) userService.loadUserByUsername(username);
        if (userService.delete(user)) {
            return ResponseEntity.ok("User deleted!");
        }

        return ResponseEntity.badRequest().body("User not found!");
    }

    @DeleteMapping("/delete/subscriber")
    public ResponseEntity<?> deleteByNumber(@RequestParam("number") String phoneNumber) {
        Subscriber subscriber = subscriberService.getByNumber(phoneNumber);

        if (subscriberService.delete(subscriber)) {
            return ResponseEntity.ok("Subscriber was successfully deleted");
        }

        return ResponseEntity.badRequest().body("Subscriber not found!");
    }

    @GetMapping("/generate/invoice")
    public InvoiceDTO createInvoice() {
        return new InvoiceDTO();
    }

    @PostMapping("/generate/invoice")
    public ResponseEntity<?> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        try {

            Subscriber subscriber = subscriberService.getByNumber(invoiceDTO.getSubscriberPhone());
            if (subscriber == null) {
                return ResponseEntity.badRequest().body("Subscriber phone not found!");
            }

            OfferedServices offeredServices = offeredServicesService.getByName(invoiceDTO.getService().toUpperCase());
            if (offeredServices == null) {
                return ResponseEntity.badRequest().body("Not a valid service!");
            }

            Currency currency = currencyService.getByName(invoiceDTO.getCurrency().toUpperCase());
            if (currency == null) {
                return ResponseEntity.badRequest().body("Currency not found!");
            }

            Invoice invoice = new Invoice();
            invoice.setSubscriber(subscriber);
            invoice.setCurrency(currency);
            Date startDate = dateFormat.parse(invoiceDTO.getStartDate());
            Date endDate = dateFormat.parse(invoiceDTO.getEndDate());
            invoice.setStartDate(startDate);
            invoice.setEndDate(endDate);
            invoice.setAmount(Double.parseDouble(invoiceDTO.getAmount()));
            invoice.setOfferedServices(offeredServices);

            invoiceService.create(invoice);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
        }
        return ResponseEntity.ok("Invoice successfully added!");
    }

    private boolean updateOrCreateClientDetails(ClientDetail newDetails, Client client) {
        if (newDetails != null && client.getDetails() != null) {
            int id =client.getDetails().getId();
            client.setDetails(newDetails);
            client.getDetails().setId(id);
            return clientDetailService.update(client.getDetails());
        } else if (newDetails != null) {
            client.setDetails(newDetails);
            return clientDetailService.create(client.getDetails());
        }

        return false;
    }
}