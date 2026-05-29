package com.schinor.rodojacto.repositories

import com.schinor.rodojacto.models.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : JpaRepository<Device, Long> {
    fun findByAssetTag(assetTag: String): Device?
    fun findByOrganizationId(organizationId: Long): List<Device>
}
