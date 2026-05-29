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
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory

@Component
class DataSeeder(
    private val orgRepo: OrganizationRepository,
    private val collabRepo: CollaboratorRepository,
    private val deviceRepo: DeviceRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(DataSeeder::class.java)

    @Transactional
    override fun run(vararg args: String) {
        logger.info("Verificando necessidade de seed na base de dados...")
        if (orgRepo.count() == 0L) {
            logger.info("Iniciando seed da base de dados...")
            
            // 1. Organizações
            val matriz = orgRepo.save(Organization(corporateName = "Matriz Rodojacto", registrationCode = "CNPJ-0001"))
            val sul = orgRepo.save(Organization(corporateName = "Filial Sul", registrationCode = "CNPJ-0002"))
            val norte = orgRepo.save(Organization(corporateName = "Filial Norte", registrationCode = "CNPJ-0003"))
            val oeste = orgRepo.save(Organization(corporateName = "Filial Oeste", registrationCode = "CNPJ-0004"))
            val leste = orgRepo.save(Organization(corporateName = "Filial Leste", registrationCode = "CNPJ-0005"))

            // 2. Colaboradores
            val commonPass = passwordEncoder.encode("123456")!!

            // Managers
            collabRepo.save(Collaborator(fullName = "Admin Gerente", email = "admin@rodojacto.com.br", password = commonPass, accessLevel = AccessLevel.MANAGER, organization = matriz))
            collabRepo.save(Collaborator(fullName = "Carlos Gerente", email = "carlos@rodojacto.com.br", password = commonPass, accessLevel = AccessLevel.MANAGER, organization = sul))

            // Operadores (Matriz) - 5 colaboradores
            repeat(5) { i ->
                collabRepo.save(Collaborator(fullName = "Op Matriz $i", email = "op.matriz$i@rodojacto.com.br", password = commonPass, accessLevel = AccessLevel.OPERATOR, organization = matriz))
            }

            // Operadores (Filial Sul) - 3 colaboradores
            repeat(3) { i ->
                collabRepo.save(Collaborator(fullName = "Op Sul $i", email = "op.sul$i@rodojacto.com.br", password = commonPass, accessLevel = AccessLevel.OPERATOR, organization = sul))
            }

            // Operadores (Filial Norte) - 8 colaboradores (Será Top 1)
            repeat(8) { i ->
                collabRepo.save(Collaborator(fullName = "Op Norte $i", email = "op.norte$i@rodojacto.com.br", password = commonPass, accessLevel = AccessLevel.OPERATOR, organization = norte))
            }

            // 3. Dispositivos
            // Matriz - 2 dispositivos
            deviceRepo.save(Device(model = "Coletor Zebra TC21", assetTag = "Z-001", organization = matriz))
            deviceRepo.save(Device(model = "Coletor Zebra TC21", assetTag = "Z-002", organization = matriz))

            // Sul - 10 dispositivos (Será Top 1)
            repeat(10) { i ->
                deviceRepo.save(Device(model = "Tablet Samsung Tab A", assetTag = "S-01$i", organization = sul))
            }

            // Oeste - 6 dispositivos
            repeat(6) { i ->
                deviceRepo.save(Device(model = "Leitor Honeywell", assetTag = "H-02$i", organization = oeste))
            }

            // Leste - 4 dispositivos
            repeat(4) { i ->
                deviceRepo.save(Device(model = "Coletor CipherLab", assetTag = "C-03$i", organization = leste))
            }
            
            println("✅ Seed da base de dados expandido com sucesso!")
        }
    }
}
