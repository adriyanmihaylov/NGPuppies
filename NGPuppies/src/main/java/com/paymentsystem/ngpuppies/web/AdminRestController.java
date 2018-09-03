package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.datatransferobjects.AdminDto;
import com.paymentsystem.ngpuppies.models.datatransferobjects.BillingRecordDto;
import com.paymentsystem.ngpuppies.models.datatransferobjects.ClientDto;
import com.paymentsystem.ngpuppies.models.datatransferobjects.SubscriberDto;
import com.paymentsystem.ngpuppies.models.users.*;
import com.paymentsystem.ngpuppies.services.base.*;
import com.paymentsystem.ngpuppies.viewModels.AdminViewModel;
import com.paymentsystem.ngpuppies.viewModels.ClientViewModel;
import com.paymentsystem.ngpuppies.viewModels.SubscriberViewModel;
import com.paymentsystem.ngpuppies.viewModels.UserViewModel;
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
    private BillingService billingService;
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
    public SubscriberViewModel getByNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return SubscriberViewModel.fromModel(subscriberService.getByNumber(phoneNumber));
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
    public List<SubscriberViewModel> getAllSubscribers() {
        return subscriberService.getAll().stream().map(SubscriberViewModel::fromModel).
                collect(Collectors.toList());
    }

    @GetMapping("/account")
    public AdminViewModel getAccount(Authentication authentication) {
        return AdminViewModel.fromModel(adminService.loadByUsername(authentication.getName()));
    }

    @PutMapping("/account/update")
    public ResponseEntity updateAccount(@Valid @RequestBody AdminDto adminDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return ResponseEntity.badRequest().body(message);
        }
        try {
            Admin admin = (Admin) authentication.getPrincipal();
            admin.setUsername(adminDto.getUsername());
            admin.setEmail(adminDto.getEmail());

            if (adminDto.getPassword() != null) {
                admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
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
    public ResponseEntity updateSubscriber(@RequestParam() String phoneNumber, @Valid @RequestBody SubscriberDto subscriberDto, BindingResult bindingResult) {
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
            subscriber.setPhone(subscriberDto.getPhone());
            subscriber.setFirstName(subscriberDto.getFirstName());
            subscriber.setLastName(subscriberDto.getLastName());

            if (subscriberDto.getAddress() != null && subscriber.getAddress() == null) {
                Address address = subscriberDto.getAddress();
                addressService.create(subscriberDto.getAddress());
                subscriber.setAddress(address);
            } else if (subscriberDto.getAddress() != null && subscriber.getAddress() != null) {
                subscriberDto.getAddress().setId(subscriber.getAddress().getId());
                addressService.update(subscriberDto.getAddress());
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
    public ResponseEntity updateClient(@RequestParam("username") String username, @Valid @RequestBody ClientDto clientDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return ResponseEntity.badRequest().body(message);
        }
        ClientDetail details = null;
        try {
            Client client = clientService.loadByUsername(username);
            if (client == null) {
                return ResponseEntity.badRequest().body("Client was not found!");
            }
            client.setUsername(clientDto.getUsername());
            client.setEik(clientDto.getEik());
            if (clientDto.getPassword() != null) {
                if (clientDto.getPassword().length() < 6) {
                    return ResponseEntity.badRequest().body("Password must be at least 6 characters");
                } else if (clientDto.getPassword().length() > 100) {
                    return ResponseEntity.badRequest().body("Password must be less than 100 characters");
                }
                client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
            }
            if (clientDto.getDetails() != null && client.getDetails() == null) {
                details = clientDto.getDetails();
                if (!clientDetailService.create(details)) {
                    return ResponseEntity.badRequest().body("Some error happened when saving details!");
                }

                client.setDetails(details);
            } else if (clientDto.getDetails() != null && client.getDetails() != null) {
                clientDto.getDetails().setId(client.getDetails().getId());
                clientDetailService.update(clientDto.getDetails());
            }

            if (!clientService.update(client)) {
                if (details != null) {
                    clientDetailService.delete(details);
                }
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }

        return ResponseEntity.ok("Subscriber updated successfully!");
    }

    @GetMapping("/register/admin")
    public AdminDto registerAdmin() {
        return new AdminDto();
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminDto adminDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        if (adminDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("Password can not be empty!");
        }
        try {
            Admin admin = new Admin();
            admin.setUsername(adminDto.getUsername());
            admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            admin.setEmail(adminDto.getEmail());
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
    public ClientDto registerClient() {
        return new ClientDto();
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientDto clientDto, BindingResult bindingResult) {
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
    public SubscriberDto createSubscriber() {
        SubscriberDto subscriberDto = new SubscriberDto();
        subscriberDto.setAddress(new Address());

        return subscriberDto;
    }

    @PostMapping("/create/subscriber")
    public ResponseEntity<?> createSubscriber(@Valid @RequestBody SubscriberDto subscriberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        try {
            Subscriber subscriber = new Subscriber();
            subscriber.setFirstName(subscriberDto.getFirstName());
            subscriber.setLastName(subscriberDto.getLastName());
            subscriber.setPhone(subscriberDto.getPhone());
            subscriber.setEgn(subscriberDto.getEgn());

            if (subscriberDto.getAddress() != null) {
                Address address = subscriberDto.getAddress();
                addressService.create(subscriberDto.getAddress());
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

    @GetMapping("/generate/bill")
    public BillingRecordDto createBill() {
        return new BillingRecordDto();
    }

    @PostMapping("/generate/bill")
    public ResponseEntity<?> generateBill(@Valid @RequestBody BillingRecordDto billingRecordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        try {

            Subscriber subscriber = subscriberService.getByNumber(billingRecordDto.getSubscriberPhone());
            if (subscriber == null) {
                return ResponseEntity.badRequest().body("Subscriber phone not found!");
            }

            OfferedServices offeredServices = offeredServicesService.getByName(billingRecordDto.getService().toUpperCase());
            if(offeredServices == null) {
                return ResponseEntity.badRequest().body("Not a valid service!");
            }

            Currency currency = currencyService.getByName(billingRecordDto.getCurrency().toUpperCase());
            if (currency == null) {
                return ResponseEntity.badRequest().body("Currency not found!");
            }

            BillingRecord billingRecord = new BillingRecord();
            billingRecord.setSubscriber(subscriber);
            billingRecord.setCurrency(currency);
            Date startDate = dateFormat.parse(billingRecordDto.getStartDate());
            Date endDate = dateFormat.parse(billingRecordDto.getEndDate());
            billingRecord.setStartDate(startDate);
            billingRecord.setEndDate(endDate);
            billingRecord.setAmount(Double.parseDouble(billingRecordDto.getAmount()));
            billingRecord.setOfferedServices(offeredServices);

            billingService.create(billingRecord);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
        }
        return ResponseEntity.ok("Bill successfully added!");
    }
}