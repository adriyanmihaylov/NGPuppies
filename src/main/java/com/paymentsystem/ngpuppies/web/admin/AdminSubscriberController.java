package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.dto.SubscriberDTO;
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
@RequestMapping("/api")
public class AdminSubscriberController {
    @Autowired
    private SubscriberService subscriberService;

    @GetMapping()
    public ResponseEntity<SubscriberViewModel> getSubscriberByPhoneNumber(@RequestParam("phone") @ValidPhone String phoneNumber) {
        Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);

        if (subscriber != null) {
            SubscriberViewModel viewModel = SubscriberViewModel.fromModel(subscriber);

            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/subscriber/all")
    public ResponseEntity<List<SubscriberViewModel>> getAllSubscribers() {
        List<SubscriberViewModel> viewModels = subscriberService.getAll().stream()
                .map(SubscriberViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/subscriber/create")
    public SubscriberDTO createSubscriber() {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setAddress(new Address());

        return subscriberDTO;
    }

    @PostMapping("/subscriber/create")
    public ResponseEntity<ResponseMessage> createSubscriber(@RequestBody @Valid SubscriberDTO subscriberDTO,
                                                            BindingResult bindingResult) {
        try {
            if (subscriberService.create(subscriberDTO)) {
                return new ResponseEntity<>(new ResponseMessage("Subscriber created!"), HttpStatus.OK);
            }
        } catch (IllegalArgumentException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/subscriber/{phone}/update")
    public ResponseEntity<ResponseMessage> updateSubscriber(@PathVariable("phone") @ValidPhone String phoneNumber,
                                                            @RequestBody @Valid SubscriberDTO subscriberDTO,
                                                            BindingResult bindingResult) {
        try {
            if (subscriberService.update(phoneNumber, subscriberDTO)) {
                return new ResponseEntity<>(new ResponseMessage("Subscriber updated!"), HttpStatus.OK);
            }
        } catch (IllegalArgumentException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/subscriber/delete")
    public ResponseEntity<ResponseMessage> deleteByNumber(@RequestParam("phone") @ValidPhone String phoneNumber) {
        try {
            if (subscriberService.deleteSubscriberByNumber(phoneNumber)) {
                return new ResponseEntity<>(new ResponseMessage(phoneNumber + " deleted!"), HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/subscriber/{phone}/service")
    public ResponseEntity<ResponseMessage> addNewServiceToSubscriber(@PathVariable("phone") @ValidPhone String subscriberPhone,
                                                                     @RequestParam("name") @ValidServiceName String serviceName) {
        try {
            if (subscriberService.addServiceToSubscriber(subscriberPhone, serviceName)) {
                return new ResponseEntity<>(new ResponseMessage(serviceName + " service added to subscriber!"),HttpStatus.OK);
            }
        } catch (IllegalArgumentException | AlreadyBoundException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{service}/all")
    public ResponseEntity<List<SubscriberSimpleViewModel>> getAllSubscribersOfService(@PathVariable("service") @ValidServiceName String serviceName) {
        try {
            List<Subscriber> subscribers = subscriberService.getAllSubscribersUsingServiceByServiceName(serviceName);
            if (subscribers != null) {
                List<SubscriberSimpleViewModel> viewModels = subscribers
                        .stream()
                        .map(SubscriberSimpleViewModel::fromModel)
                        .collect(Collectors.toList());

                return new ResponseEntity<>(viewModels, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}