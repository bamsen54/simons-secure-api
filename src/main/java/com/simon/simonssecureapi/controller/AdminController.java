package com.simon.simonssecureapi.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simon.simonssecureapi.exception.MemberNotFoundException;
import com.simon.simonssecureapi.dto.AppUserDto;
import com.simon.simonssecureapi.dto.AppUserPutDto;
import com.simon.simonssecureapi.dto.AppUserRegistrationDto;
import com.simon.simonssecureapi.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/members")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService, ObjectMapper objectMapper) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllAppUsers() {
        return ResponseEntity.ok(adminService.getAllAppUsersThatAreMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getAppUserById(@PathVariable Long id) {

        AppUserDto appUserDtoWithId = adminService.getAppUserById(id).orElseThrow(()-> new MemberNotFoundException(id));

        return ResponseEntity.status(HttpStatus.OK).body(appUserDtoWithId);
    }

    @PostMapping
    public ResponseEntity<AppUserDto> registerAppUser(@Valid @RequestBody AppUserRegistrationDto dto) {
        AppUserDto registeredAppUserDto = adminService.registrateUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredAppUserDto);
    }

    @PutMapping("/{id}")
    ResponseEntity<AppUserDto> putAppUser(@PathVariable Long id, @Valid @RequestBody AppUserPutDto dto) {
        AppUserDto updatedDto = adminService.putAppUser(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @PatchMapping("/{id}")
    ResponseEntity<AppUserDto> patchAppUser(@PathVariable Long id, @Valid @RequestBody Map<String, Object> updates) {

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
