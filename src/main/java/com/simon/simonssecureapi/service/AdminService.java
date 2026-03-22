package com.simon.simonssecureapi.service;

import com.simon.simonssecureapi.exception.MemberNotFoundException;
import com.simon.simonssecureapi.Role;
import com.simon.simonssecureapi.dto.AppUserDto;
import com.simon.simonssecureapi.dto.AppUserPutDto;
import com.simon.simonssecureapi.dto.AppUserRegistrationDto;
import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.entity.Member;
import com.simon.simonssecureapi.exception.ResourceAlreadyExistsException;
import com.simon.simonssecureapi.mapper.AppUserMapper;
import com.simon.simonssecureapi.repository.AdminRepo;
import com.simon.simonssecureapi.repository.MemberRepo;
import com.simon.simonssecureapi.util.ApiUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepo adminRepo;
    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepo adminRepo, MemberRepo memberRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo       = adminRepo;
        this.memberRepo      = memberRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUserDto registrateUser(AppUserRegistrationDto dto) {

        if(memberRepo.existsByDateOfBirth(dto.dateOfBirth()))
            throw new ResourceAlreadyExistsException("A member with that date of birth already exists");

        if(adminRepo.existsByUsername(dto.username()))
            throw new ResourceAlreadyExistsException("A user with that username already exists");

        Address updatedAddress = ApiUtil.toCompleteAddress(dto.street(), dto.postalCode(), dto.city());

        Member member = new Member();

        Address existingAddress =  ApiUtil.getAddressWithFields(updatedAddress);

        if(existingAddress != null)
            member.setAddress(existingAddress);

        else
            member.setAddress(updatedAddress);

        member.setFirstName(dto.firstName());
        member.setLastName(dto.lastName());
        member.setEmail(dto.email());
        member.setPhone(dto.phone());
        member.setDateOfBirth(dto.dateOfBirth());


        AppUser user = new AppUser();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());
        user.setMember(member);

        AppUser savedUser = adminRepo.save(user);

        return AppUserMapper.toDto(savedUser);
    }

    public List<AppUserDto> getAllAppUsersThatAreMembers() {
        return adminRepo.findAll()
                        .stream()
                        .filter( appUser -> appUser.getMember() != null)
                        .map(AppUserMapper::toDto)
                        .toList();
    }

    public Optional<AppUserDto> getAppUserById(Long id){

        AppUser appUserWithId = adminRepo.findById(id).orElseThrow(() -> new MemberNotFoundException(id));

        if(appUserWithId.getMember() == null)
            return Optional.empty();

        return Optional.of( AppUserMapper.toDto(appUserWithId));
    }


    public AppUserDto putAppUser(Long id, AppUserPutDto dto) {

        AppUser appUser = adminRepo.findById(id).get();

        if(memberRepo.existsByDateOfBirthAndIdNot(dto.dateOfBirth(), appUser.getMember().getId()))
            throw new ResourceAlreadyExistsException("A member with that date of birth already exists");


        if(adminRepo.existsByUsernameAndIdNot(dto.username(), id ))
            throw new ResourceAlreadyExistsException("A user with that username already exists");

        Address updatedAddress = ApiUtil.toCompleteAddress(dto.street(), dto.postalCode(), dto.city());
        Address existingAddress =  ApiUtil.getAddressWithFields(updatedAddress);

        if(existingAddress != null)
            appUser.getMember().setAddress(existingAddress);

        else
            appUser.getMember().setAddress(updatedAddress);

        appUser.getMember().setFirstName(dto.firstName());
        appUser.getMember().setLastName(dto.lastName());
        appUser.getMember().setEmail(dto.email());
        appUser.getMember().setPhone(dto.phone());
        appUser.getMember().setDateOfBirth(dto.dateOfBirth());

        appUser.setUsername(dto.username());
        appUser.setPassword(passwordEncoder.encode(dto.password()));
        appUser.setRole(dto.role());

        adminRepo.save(appUser);
        return AppUserMapper.toDto(appUser);
    }

    @Transactional
    public Optional<AppUserDto> patchAppUser(Long id, Map<String, Object> updates) {

        if(memberRepo.existsByDateOfBirthAndIdNot((String) updates.get("dateOfBirth"), id))
            throw new ResourceAlreadyExistsException("A member with date of birth " + updates.get("dateOfBirth") + " already exists");

        if(adminRepo.existsByUsernameAndIdNot((String) updates.get("username"), id))
            throw new ResourceAlreadyExistsException("A user with that username already exists");

        AppUser appUser = adminRepo.findById(id).get();

        if(appUser.getMember() != null) {

            if(appUser.getMember().getAddress() != null) {

                String street     = updates.containsKey("street") ? updates.get("street").toString() : appUser.getMember().getAddress().getStreet();
                String postalCode = updates.containsKey("postalCode") ? updates.get("postalCode").toString() : appUser.getMember().getAddress().getPostalCode();
                String city       = updates.containsKey("city") ? updates.get("city").toString() : appUser.getMember().getAddress().getCity();

                System.out.println(street + " " + postalCode + " " + city);

                Address addressFromUpdates = ApiUtil.toCompleteAddress(
                        street,
                        postalCode,
                        city
                );

                if(ApiUtil.getAddressWithFields(addressFromUpdates) != null) {
                    appUser.getMember().setAddress(ApiUtil.getAddressWithFields(addressFromUpdates));
                    System.out.println("not null");
                }
                else {
                    this.updateAppUserAddressInCaseNewAddress(appUser, updates);
                    System.out.println("null");
                }
            }

            if(updates.get("firstName") != null)
                appUser.getMember().setFirstName((String) updates.get("firstName"));

            if(updates.get("lastName") != null)
                appUser.getMember().setLastName((String) updates.get("lastName"));

            if(updates.get("email") != null)
                appUser.getMember().setEmail((String) updates.get("email"));

            if(updates.get("dateOfBirth") != null)
                appUser.getMember().setDateOfBirth((String) updates.get("dateOfBirth"));

            if(updates.containsKey("phone"))
                appUser.getMember().setPhone((String) updates.get("phone"));
        }

        if(updates.get("username") != null)
            appUser.setUsername((String) updates.get("username"));

        if(updates.get("password") != null)
            appUser.setPassword( passwordEncoder.encode((String) updates.get("password")));

        if(updates.get("role") != null)
            appUser.setRole(Role.valueOf((String) updates.get("role")));


        return Optional.of(AppUserMapper.toDto(appUser));
    }

    public void deleteAppUserById(Long id){
        adminRepo.deleteById(id);
    }

    public void updateAppUserAddressInCaseNewAddress(AppUser appUser, Map<String, Object> updates) {
        String street = (String) updates.get("street");
        String postalCode = (String) updates.get("postalCode");
        String city = (String) updates.get("city");

        if(street != null || postalCode != null || city != null) {

            Address newAddress = new Address();

            if (updates.get("street") != null)
                newAddress.setStreet((String) updates.get("street"));

            else
                newAddress.setStreet(appUser.getMember().getAddress().getStreet());

            if (updates.get("postalCode") != null)
                newAddress.setPostalCode((String) updates.get("postalCode"));

            else
                newAddress.setPostalCode(appUser.getMember().getAddress().getPostalCode());

            if (updates.get("city") != null)
                newAddress.setCity((String) updates.get("city"));

            else
                newAddress.setCity(appUser.getMember().getAddress().getCity());

            appUser.getMember().setAddress(newAddress);
        }
    }

    public Optional<AppUser> getRawAppUserById(Long id) {
        return adminRepo.findById(id);
    }
}
