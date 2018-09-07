package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.dto.SubscriberDTO;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.models.viewModels.SubscriberSimpleViewModel;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.models.viewModels.SubscriberViewModel;
import com.paymentsystem.ngpuppies.validation.anotations.ValidPhone;
import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.rmi.AlreadyBoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/subscriber")
public class AdminSubscriberController {
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private TelecomServService telecomServService;
    @Autowired
    private ClientService clientService;

    @GetMapping("")
    public ResponseEntity<SubscriberViewModel> getSubscriberByPhoneNumber(@RequestParam("phone") @ValidPhone String phoneNumber) {
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

    @GetMapping("/create")
    public SubscriberDTO createSubscriber() {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setAddress(new Address());

        return subscriberDTO;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createSubscriber(@RequestBody @Valid SubscriberDTO subscriberDTO,
                                                            BindingResult bindingResult) {
        try {
            Subscriber subscriber = new Subscriber(subscriberDTO.getFirstName(),
                    subscriberDTO.getLastName(),
                    subscriberDTO.getPhone(),
                    subscriberDTO.getEgn(),
                    subscriberDTO.getAddress(),
                    0D);

            if (subscriberDTO.getClient() != null) {
                Client client = clientService.loadByUsername(subscriberDTO.getClient());
                if (client == null) {
                    return new ResponseEntity<>(new ResponseMessage("Client not found"), HttpStatus.BAD_REQUEST);
                }

                subscriber.setClient(client);
            }

            if (!subscriberService.create(subscriber)) {
                return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Please try again later"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseMessage("Subscriber created!"), HttpStatus.OK);
    }

    @PutMapping("/{phone}/update")
    public ResponseEntity<ResponseMessage> updateSubscriber(@PathVariable("phone") @ValidPhone String phoneNumber,
                                                            @RequestBody @Valid SubscriberDTO subscriberDTO,
                                                            BindingResult bindingResult) {
        try {
            Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);
            if (subscriber == null) {
                return new ResponseEntity<>(new ResponseMessage("Subscriber not found!"), HttpStatus.BAD_REQUEST);
            }

            subscriber.setPhone(subscriberDTO.getPhone());
            subscriber.setFirstName(subscriberDTO.getFirstName());
            subscriber.setLastName(subscriberDTO.getLastName());

            if (subscriberDTO.getClient() != null) {
                Client client = clientService.loadByUsername(subscriberDTO.getClient());
                if (client != null) {
                    subscriber.setClient(client);
                } else {
                    return new ResponseEntity<>(new ResponseMessage("Client not found!"), HttpStatus.BAD_REQUEST);
                }
            }

            if (subscriberDTO.getAddress() != null) {
                int id = subscriber.getAddress().getId();
                subscriber.setAddress(subscriberDTO.getAddress());
                subscriber.getAddress().setId(id);
            }

            if (!subscriberService.update(subscriber)) {
                return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseMessage("Subscriber updated!"), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteByNumber(@RequestParam("number") @ValidPhone String phoneNumber) {
        Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);

        if (subscriber != null) {
            if (subscriberService.delete(subscriber)) {
                return new ResponseEntity<>(new ResponseMessage("Subscriber deleted successfully"), HttpStatus.OK);

            }
            return new ResponseEntity<>(new ResponseMessage("Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseMessage("Subscriber not found!"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{phone}/service")
    public ResponseEntity<ResponseMessage> addNewServiceToSubscriber(@PathVariable("phone") @ValidPhone String subscriberPhone,
                                                                     @RequestParam("name") @ValidServiceName String serviceName) {
        try {
            Subscriber subscriber = subscriberService.getSubscriberByPhone(subscriberPhone);
            if (subscriber == null) {
                return new ResponseEntity<>(new ResponseMessage("Subscriber not found"), HttpStatus.BAD_REQUEST);
            }
            TelecomServ currentService = telecomServService.getByName(serviceName);
            if (currentService == null) {
                return new ResponseEntity<>(new ResponseMessage("Service not found"), HttpStatus.BAD_REQUEST);
            }
            if (subscriberService.addServiceToSubscriber(subscriber, currentService)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (AlreadyBoundException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{service}/all")
    public ResponseEntity<List<SubscriberSimpleViewModel>> getAllSubscribersOfService(@PathVariable("service") @ValidServiceName String serviceName) {
        TelecomServ telecomServ = telecomServService.getByName(serviceName);
        if (telecomServ != null) {
            List<Subscriber> subscribers = subscriberService.getSubscribersByService(telecomServ.getId());

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