package com.simon.simonssecureapi.controller;

import com.simon.simonssecureapi.dto.AddressDto;
import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.repository.AdminRepo;
import com.simon.simonssecureapi.service.AdminService;
import com.simon.simonssecureapi.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.simon.simonssecureapi.util.ApiUtil.adminRepo;

@RestController
@RequestMapping("/test")
public class TestController {

    AdminService adminService;
    AdminRepo adminRepo;
    ApiUtil apiUtil;

    public TestController(AdminService adminService, AdminRepo adminRepo, ApiUtil apiUtil) {
        this.adminService = adminService;
        this.adminRepo   = adminRepo;
        this.apiUtil     = apiUtil;
    }

    @GetMapping("/rawdogg/{id}")
    public AppUser getAllRawDogg(@PathVariable Long id) {
        return adminService.getRawAppUserById(id).get();
    }

    @GetMapping("/addresses")
    public List<AddressDto> getAllAddresses() {

        List<Address> uniqueAddresses = adminRepo.findAllByMemberIsNotNull()
                .stream()
                .map(appUser -> appUser.getMember().getAddress())
                .distinct()
                .toList();

        List<AddressDto> addressDtos = new ArrayList<>();

        for (Address address : uniqueAddresses)
            addressDtos.add(new AddressDto( address.getId(), address.getStreet(), address.getPostalCode(), address.getCity()));

        return addressDtos;
    }
}
