package FlowLink.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import FlowLink.domain.model.AddressEntry

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromAddressEntryList(value: String): List<AddressEntry> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun toAddressEntryList(list: List<AddressEntry>): String {
        return json.encodeToString(list)
    }
}