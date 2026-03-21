package com.simon.simonssecureapi.util;

import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.repository.AdminRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiUtil {

    public static AdminRepo adminRepo;

    public ApiUtil(AdminRepo adminRepo) {
        ApiUtil.adminRepo = adminRepo;
    }

    public static Address getAddressWithFields(Address address) {

        List<Address> uniqueAddresses = adminRepo.findAllByMemberIsNotNull()
                                                 .stream()
                                                 .map(appUser -> appUser.getMember().getAddress())
                                                 .distinct()
                                                 .toList();

        for(Address a : uniqueAddresses){
            if(a.equals(address))
                return a;

        }

        return null;
    }

    public static Address toCompleteAddress(String street, String postalCode, String city) {

        Address address = new Address();

        address.setStreet(        street == null ? "" : street );
        address.setPostalCode(postalCode == null ? "" : postalCode );
        address.setCity(            city == null ? "" : city);

        return address;
    }

    public static Address findAddressById(Long id) {

        List<Address> uniqueAddresses = adminRepo.findAllByMemberIsNotNull()
                .stream()
                .map(appUser -> appUser.getMember().getAddress())
                .distinct()
                .toList();

        for(Address a : uniqueAddresses){
            if(a.getId().equals(id))
                return a;

        }

        return null;
    }
}
