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
        // Só injeta os dados se a tabela de organizações estiver vazia
        if (orgRepo.count() == 0L) {
            
            val org1 = orgRepo.save(Organization(
                corporateName = "Matriz Rodojacto",
                registrationCode = "CNPJ-12345678000199"
            ))

            val org2 = orgRepo.save(Organization(
                corporateName = "Filial Sul",
                registrationCode = "CNPJ-98765432000111"
            ))

            // Criar um Manager (Acesso Global)
            collabRepo.save(Collaborator(
                fullName = "Admin Gerente",
                email = "admin@rodojacto.com.br",
                password = passwordEncoder.encode("123456")!!,
                accessLevel = AccessLevel.MANAGER,
                organization = org1
            ))

            // Criar um Operator (Acesso Restrito à Matriz)
            collabRepo.save(Collaborator(
                fullName = "João Operador",
                email = "joao@rodojacto.com.br",
                password = passwordEncoder.encode("123456")!!,
                accessLevel = AccessLevel.OPERATOR,
                organization = org1
            ))

            // Criar Dispositivos
            deviceRepo.save(Device(model = "Coletor Zebra TC21", assetTag = "TAG-001", organization = org1))
            deviceRepo.save(Device(model = "Tablet Samsung Active 3", assetTag = "TAG-002", organization = org2))
            
            println("✅ Seed da base de dados concluído com sucesso!")
        }
    }
}
