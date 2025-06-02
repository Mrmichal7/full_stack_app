package com.example.pasir_jurczak_michal.controller;

import com.example.pasir_jurczak_michal.dto.GroupResponseDTO;
import com.example.pasir_jurczak_michal.dto.MembershipDTO;
import com.example.pasir_jurczak_michal.dto.MembershipResponseDTO;
import com.example.pasir_jurczak_michal.model.Group;
import com.example.pasir_jurczak_michal.model.Membership;
import com.example.pasir_jurczak_michal.model.User;
import com.example.pasir_jurczak_michal.repository.GroupRepository;
import com.example.pasir_jurczak_michal.repository.MembershipRepository;
import com.example.pasir_jurczak_michal.service.MembershipService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class MembershipGraphQLController {

    private final MembershipService membershipService;
    private final MembershipRepository membershipRepository;
    private final GroupRepository groupRepository;

    public MembershipGraphQLController(MembershipService membershipService, MembershipRepository membershipRepository, GroupRepository groupRepository) {
        this.membershipService = membershipService;
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
    }

    @QueryMapping
    public List<MembershipResponseDTO> groupMembers(@Argument Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("nie znaleziono grupy o ID: " + groupId));

        return membershipRepository.findByGroupId(group.getId()).stream()
                .map(membership -> new MembershipResponseDTO(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGroup().getId(),
                membership.getUser().getEmail() //  Upload uploading email
        ))
    .toList();
    }

    @MutationMapping
    public MembershipResponseDTO addMember(@Valid @Argument MembershipDTO membershipDTO) {
        Membership membership = membershipService.addMember(membershipDTO);
        return new MembershipResponseDTO(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGroup().getId(),
                membership.getUser().getEmail()
        );
    }

    @MutationMapping
    public Boolean removeMember(@Argument Long membershipId) {
        membershipService.removeMember(membershipId);
        return true;
    }

    @QueryMapping
    public List<GroupResponseDTO> myGroups() {
        User user = membershipService.getCurrentUser();
        return groupRepository.findByMemberships_User(user).stream()
    .map(group -> new GroupResponseDTO(
                group.getId(),
                group.getName(),
                group.getOwner().getId()
                ))
    .toList();
    }
}
