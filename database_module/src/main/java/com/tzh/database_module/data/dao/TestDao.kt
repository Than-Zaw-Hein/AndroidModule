package com.tzh.database_module.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tzh.database_module.data.entity.TestEntity

@Dao
interface TestDao {

    @Query(
        """
        SELECT * FROM test
    """
    )
    suspend fun getAllTestData(): List<TestEntity>

    @Insert
    suspend fun insertData(testData: TestEntity)
}