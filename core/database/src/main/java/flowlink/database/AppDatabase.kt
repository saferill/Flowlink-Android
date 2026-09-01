package FlowLink.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import FlowLink.database.dao.DeviceDao
import FlowLink.database.dao.NetworkDao
import FlowLink.database.model.LocalDeviceEntity
import FlowLink.database.model.NetworkEntity
import FlowLink.database.model.PairedDeviceEntity

interface AppDatabase {
    fun devicesDao(): DeviceDao
    fun networkDao(): NetworkDao

    /**
     * Execute the whole database calls as an atomic operation
     */
    suspend fun <T> transaction(block: suspend () -> T): T

    companion object {
        private const val DATABASE_NAME = "FlowLink.db"
        
        fun createRoom(context: Context): AppDatabase = Room
            .databaseBuilder(context, AppRoomDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration(false)
            .build()
    }
}

@Database(
    entities = [
        PairedDeviceEntity::class,
        NetworkEntity::class,
        LocalDeviceEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class AppRoomDatabase : RoomDatabase(), AppDatabase {
    abstract override fun devicesDao(): DeviceDao
    abstract override fun networkDao(): NetworkDao

    override suspend fun <T> transaction(block: suspend () -> T): T = withTransaction(block)
}