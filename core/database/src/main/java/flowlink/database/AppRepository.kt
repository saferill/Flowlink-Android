package FlowLink.database

import android.content.Context
import FlowLink.database.dao.NetworkDao
import FlowLink.database.dao.DeviceDao
import FlowLink.database.model.LocalDeviceEntity
import FlowLink.database.model.NetworkEntity
import FlowLink.database.model.PairedDeviceEntity
import FlowLink.database.model.toDomain
import FlowLink.domain.model.AddressEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val context: Context,
    private val deviceDao: DeviceDao,
    private val networkDao: NetworkDao
){
    fun getAllDevicesFlow() = deviceDao.getAllDevicesFlow()
    suspend fun addDevice(device: PairedDeviceEntity) = deviceDao.addDevice(device)
    suspend fun removeDevice(deviceId: String) = deviceDao.removeDevice(deviceId)
    suspend fun updateDevice(device: PairedDeviceEntity) = deviceDao.updateDevice(device)
    suspend fun updateDeviceAddresses(deviceId: String, ipAddresses: List<AddressEntry>) = deviceDao.updateDeviceAddresses(deviceId, ipAddresses)

    fun getRemoteDevice(deviceId: String) = deviceDao.getRemoteDevice(deviceId)

    suspend fun addLocalDevice(device: LocalDeviceEntity) = deviceDao.addLocalDevice(device)
    suspend fun updateLocalDeviceName(deviceId: String, deviceName: String) = deviceDao.updateLocalDeviceName(deviceId, deviceName)
    fun getLocalDevice() = deviceDao.getLocalDevice()
    fun getLocalDeviceFlow() = deviceDao.getLocalDeviceFlow()

    fun getAllNetworksFlow() = networkDao.getAllNetworksFlow()
    suspend fun addNetwork(network: NetworkEntity) = networkDao.addNetwork(network)
    fun getNetwork(ssid: String) = networkDao.getNetwork(ssid)

    suspend fun deleteNetwork(network: NetworkEntity) = networkDao.deleteNetwork(network)
    suspend fun updateNetwork(network: NetworkEntity) = networkDao.updateNetwork(network)
}