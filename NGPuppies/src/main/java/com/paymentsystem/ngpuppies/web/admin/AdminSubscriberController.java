package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.dto.Response;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.dto.SubscriberDTO;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.models.viewModels.SubscriberSimpleViewModel;
import com.paymentsystem.ngpuppies.services.base.AddressService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.services.base.OfferedServicesService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.models.viewModels.SubscriberViewModel;
import com.paymentsystem.ngpuppies.validator.base.ValidPhone;
import com.paymentsystem.ngpuppies.validator.base.ValidServiceName;
import com.paymentsystem.ngpuppies.web.ResponseHandler;
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

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/subscriber")
@Validated()
public class AdminSubscriberController {
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private OfferedServicesService offeredServicesService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ResponseHandler responseHandler;

    @GetMapping("")
    public ResponseEntity<SubscriberViewModel> getByNumber(@RequestParam("phone") @ValidPhone String phoneNumber) {
        Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);

        if (subscriber != null) {
            SubscriberViewModel viewModel = SubscriberViewModel.fromModel(subscriber);

            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubscriberViewModel>> getAllSubscribers() {
        List<SubscriberViewModel> viewModels = subscriberService.getAll().stream()
                .map(SubscriberViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{phone}/service/")
    public ResponseEntity<Response> addNewServiceToSubscriber(@PathVariable("phone") @ValidPhone  String subscriberPhone,
                                                              @RequestParam() @ValidServiceName String serviceName) {
        try {

            Subscriber subscriber = subscriberService.getSubscriberByPhone(subscriberPhone);
            if (subscriber == null) {
                return responseHandler.returnResponse("Subscriber not found", HttpStatus.BAD_REQUEST);
            }
            OfferedServices offeredServices = offeredServicesService.getByName(serviceName);
            if (offeredServices == null) {
                return responseHandler.returnResponse("Service not found", HttpStatus.BAD_REQUEST);
            }
            subscriber.getSubscriberServices().add(offeredServices);
            if (subscriberService.update(subscriber)) {
                return responseHandler.returnResponse("Service successfully added", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseHandler.returnResponse("The subscriber already has the selected service!", HttpStatus.NOT_ACCEPTABLE);
        }

        return responseHandler.returnResponse("Something went wrong!Please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/create")
    public SubscriberDTO createSubscriber() {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setAddress(new Address());

        return subscriberDTO;
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createSubscriber(@RequestBody @Valid SubscriberDTO subscriberDTO,
                                                     BindingResult bindingResult) {
        try {
            Subscriber subscriber = new Subscriber(subscriberDTO.getFirstName(),
                    subscriberDTO.getLastName(),
                    subscriberDTO.getPhone(),
                    subscriberDTO.getEgn(),
                    subscriberDTO.getAddress());

            if (subscriberDTO.getClient() != null) {
                Client client = clientService.loadByUsername(subscriberDTO.getClient());
                if (client == null) {
                    return responseHandler.returnResponse("Client not found", HttpStatus.BAD_REQUEST);
                }

                subscriber.setClient(client);
            }

            if (!subscriberService.create(subscriber)) {
                return responseHandler.returnResponse("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return responseHandler.returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return responseHandler.returnResponse("Please try again later", HttpStatus.BAD_REQUEST);
        }

        return responseHandler.returnResponse("Subscriber created!", HttpStatus.OK);
    }

    @PutMapping("/{phone}/update")
    public ResponseEntity<Response> updateSubscriber(@PathVariable("phone") @ValidPhone String phoneNumber,
                                                     @RequestBody @Valid SubscriberDTO subscriberDTO,
                                                     BindingResult bindingResult) {
        try {
            Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);
            if (subscriber == null) {
                return responseHandler.returnResponse("Subscriber not found!", HttpStatus.BAD_REQUEST);
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
                        return responseHandler.returnResponse("Client not found!", HttpStatus.BAD_REQUEST);
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
                    return responseHandler.returnResponse("Please create the address first!", HttpStatus.BAD_REQUEST);
                }
            }

            if (!subscriberService.update(subscriber)) {
                return responseHandler.returnResponse("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return responseHandler.returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return responseHandler.returnResponse("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseHandler.returnResponse("Subscriber updated!", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteByNumber(@RequestParam("number") @ValidPhone String phoneNumber) {
        Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);

        if (subscriber != null) {
            if (subscriberService.delete(subscriber)) {
                return responseHandler.returnResponse("Subscriber deleted successfully", HttpStatus.OK);

            }
            return responseHandler.returnResponse("Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseHandler.returnResponse("Subscriber not found!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{service}/all")
    public ResponseEntity<List<SubscriberSimpleViewModel>> getAllSubscribersOfService(@PathVariable("service") @ValidServiceName String serviceName) {
        OfferedServices offeredServices = offeredServicesService.getByName(serviceName);
        if(offeredServices != null) {
            List<Subscriber> subscribers = subscriberService.getSubscribersByService(offeredServices.getId());

            if (subscribers != null) {
                List<SubscriberSimpleViewModel> viewModels = subscribers
                        .stream()
                        .map(SubscriberSimpleViewModel::fromModel)
                        .collect(Collectors.toList());

                return new ResponseEntity<>(viewModels, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}