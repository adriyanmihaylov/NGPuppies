package com.paymentsystem.ngpuppies.web.RestControllers;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.services.base.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressRestController {
    @Autowired
    private AddressService addressService;
    @PostMapping("/create")
    public void create(@RequestParam(name = "city", required = false) String city,
                       @RequestParam(name = "street", required = false) String street,
                       @RequestParam(name = "State", required = false) String state,
                       @RequestParam(name = "postCode", required = false) String postCode,
                       @RequestParam(name = "country", required = false) String country){
        Address addressToCreate = new Address();
        if (city != null){
            addressToCreate.setCity(city);
        }if(street != null){
            addressToCreate.setStreet(street);
        }if (state != null){
           addressToCreate.setState(state);
        }if (postCode != null){
            addressToCreate.setPostCode(postCode);
        }if (country != null){
            addressToCreate.setCountry(country);
        }
        addressService.create(addressToCreate);
    }
    @DeleteMapping("/delete")
    public void deleteById(@RequestParam(name = "id") String id ){
        addressService.deleteById(Integer.parseInt(id));
    }
}
