package com.example.pasir_jurczak_michal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "'groups'") // 'group' to slowo kluczowe w SQL!
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Nazwa grupy

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // Wtasciciel grupy (mode zapraszać, usuwać)

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships; // Lista członkostw w grupie
}

