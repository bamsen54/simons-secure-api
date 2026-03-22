package com.simon.simonssecureapi.controller;

import com.simon.simonssecureapi.dto.AppUserDto;
import com.simon.simonssecureapi.dto.MemberDto;
import com.simon.simonssecureapi.dto.MemberPutDto;
import com.simon.simonssecureapi.repository.AdminRepo;
import com.simon.simonssecureapi.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypages/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateOwnMember(@PathVariable Long id, @Valid @RequestBody MemberPutDto dto) {
        return ResponseEntity.ok().body(memberService.putOwnMember(id, dto));
    }
}
