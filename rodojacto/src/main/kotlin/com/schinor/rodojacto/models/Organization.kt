package com.schinor.rodojacto.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "organization")
@SQLDelete(sql = "UPDATE organization SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
class Organization(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var corporateName: String,

    @Column(nullable = false, unique = true)
    var registrationCode: String,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null,

    @Column(nullable = false)
    var isDeleted: Boolean = false,

    @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL])
    val collaborators: MutableList<Collaborator> = mutableListOf(),

    @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL])
    val devices: MutableList<Device> = mutableListOf()
)