package com.example.pasir_jurczak_michal.service;

import com.example.pasir_jurczak_michal.dto.MembershipDTO;
import com.example.pasir_jurczak_michal.model.Group;
import com.example.pasir_jurczak_michal.model.Membership;
import com.example.pasir_jurczak_michal.model.User;
import com.example.pasir_jurczak_michal.repository.GroupRepository;
import com.example.pasir_jurczak_michal.repository.MembershipRepository;
import com.example.pasir_jurczak_michal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public MembershipService(MembershipRepository membershipRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<Membership> getGroupMembers(long groupId) {
        return membershipRepository.findByGroupId(groupId);
    }

    public Membership addMember(MembershipDTO membershipDTO) {
        User user = userRepository.findByEmail(membershipDTO.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("nie znaleziano uzytikownika o emailu: " + membershipDTO.getUserEmail()));
        Group group = groupRepository.findById(membershipDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("nie znaleziano group o ID: " + membershipDTO.getGroupId()));

        boolean alreadyMember = membershipRepository.findByGroupId(group.getId()).stream()
            .anyMatch(membership -> membership.getUser().getId().equals(user.getId()));

        if (alreadyMember) {
            throw new IllegalStateException("Uzytikomnik jest juz członkiem tej group: ");
        }

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        return membershipRepository.save(membership);
    }

    public void removeMember(long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Czlonkostwo nie istnieje"));

        User currentUser = getCurrentUser(); // kto probuje usuwad
        User groupOwner = membership.getGroup().getOwner(); // kto jest właścicielen grupy

        if (!currentUser.getId().equals(groupOwner.getId())){
            throw new SecurityException("Iyiko właściciel grupy może usuwad cztonków.");
        }

        membershipRepository.deleteById(membershipId);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("nie znaleziono użytkownika: " + email));
    }
}
