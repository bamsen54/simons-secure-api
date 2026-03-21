package com.simon.simonssecureapi.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simon.simonssecureapi.dto.AppUserDto;
import com.simon.simonssecureapi.dto.AppUserRegistrationDto;
import com.simon.simonssecureapi.dto.AppUserPutDto;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.mapper.AppUserMapper;
import com.simon.simonssecureapi.service.AdminService;
import com.simon.simonssecureapi.util.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/members")
public class AdminController {

    private final AdminService adminService;
    private final ObjectMapper objectMapper;

    public AdminController(AdminService adminService, ObjectMapper objectMapper) {
        this.adminService = adminService;
        this.objectMapper = objectMapper;


    }

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllAppUsers() {
        return ResponseEntity.ok(adminService.getAllAppUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getAppUserById(@PathVariable Long id) {

        AppUserDto appUserDtoWithId = adminService.getAppUserById(id).orElseThrow();

        return ResponseEntity.status(HttpStatus.OK).body(appUserDtoWithId);
    }

    @PostMapping
    public ResponseEntity<AppUserDto> registerAppUser(@RequestBody AppUserRegistrationDto dto) {
        AppUserDto registeredAppUserDto = adminService.registrateUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredAppUserDto);
    }

    @PutMapping("/{id}")
    ResponseEntity<AppUserDto> putAppUser(@PathVariable Long id, @RequestBody AppUserPutDto dto) {
        AppUserDto updatedDto = adminService.putAppUser(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @PatchMapping("/{id}")
    ResponseEntity<AppUserDto> patchAppUser(@PathVariable Long id, @RequestBody Map<String, Object> updates ) throws JsonMappingException {

        //ApiUtil.addressAlreadyExists(null);

        AppUserDto dto = adminService.patchAppUser(id, updates).get();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unregisterAppUser(@PathVariable Long id) {
        adminService.deleteAppUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
