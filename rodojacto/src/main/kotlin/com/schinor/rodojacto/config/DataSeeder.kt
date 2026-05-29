package com.schinor.rodojacto.config

import com.schinor.rodojacto.models.AccessLevel
import com.schinor.rodojacto.models.Collaborator
import com.schinor.rodojacto.models.Device
import com.schinor.rodojacto.models.Organization
import com.schinor.rodojacto.repositories.CollaboratorRepository
import com.schinor.rodojacto.repositories.DeviceRepository
import com.schinor.rodojacto.repositories.OrganizationRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val orgRepo: OrganizationRepository,
    private val collabRepo: CollaboratorRepository,
    private val deviceRepo: DeviceRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (orgRepo.count() == 0L) {
            
            // 1. Organizações
            val matriz = orgRepo.save(Organization(corporateName = "Matriz Rodojacto", registrationCode = "CNPJ-0001"))
            val sul = orgRepo.save(Organization(corporateName = "Filial Sul", registrationCode = "CNPJ-0002"))
            val norte = orgRepo.save(Organization(corporateName = "Filial Norte", registrationCode = "CNPJ-0003"))
            val oeste = orgRepo.save(Organization(corporateName = "Filial Oeste", registrationCode = "CNPJ-0004"))
            val leste = orgRepo.save(Organization(corporateName = "Filial Leste", registrationCode = "CNPJ-0005"))

            // 2. Colaboradores
            val commonPass = passwordEncoder.encode("123456")!!

            // Managers
            collabRepo.save(Collaborator("Admin Gerente", "admin@rodojacto.com.br", commonPass, AccessLevel.MANAGER, matriz))
            collabRepo.save(Collaborator("Carlos Gerente", "carlos@rodojacto.com.br", commonPass, AccessLevel.MANAGER, sul))

            // Operadores (Matriz) - 5 colaboradores
            repeat(5) { i ->
                collabRepo.save(Collaborator("Op Matriz $i", "op.matriz$i@rodojacto.com.br", commonPass, AccessLevel.OPERATOR, matriz))
            }

            // Operadores (Filial Sul) - 3 colaboradores
            repeat(3) { i ->
                collabRepo.save(Collaborator("Op Sul $i", "op.sul$i@rodojacto.com.br", commonPass, AccessLevel.OPERATOR, sul))
            }

            // Operadores (Filial Norte) - 8 colaboradores (Será Top 1)
            repeat(8) { i ->
                collabRepo.save(Collaborator("Op Norte $i", "op.norte$i@rodojacto.com.br", commonPass, AccessLevel.OPERATOR, norte))
            }

            // 3. Dispositivos
            // Matriz - 2 dispositivos
            deviceRepo.save(Device("Coletor Zebra TC21", "Z-001", matriz))
            deviceRepo.save(Device("Coletor Zebra TC21", "Z-002", matriz))

            // Sul - 10 dispositivos (Será Top 1)
            repeat(10) { i ->
                deviceRepo.save(Device("Tablet Samsung Tab A", "S-01$i", sul))
            }

            // Oeste - 6 dispositivos
            repeat(6) { i ->
                deviceRepo.save(Device("Leitor Honeywell", "H-02$i", oeste))
            }

            // Leste - 4 dispositivos
            repeat(4) { i ->
                deviceRepo.save(Device("Coletor CipherLab", "C-03$i", leste))
            }
            
            println("✅ Seed da base de dados expandido com sucesso!")
        }
    }
}
