package com.tzh.database_module.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("Test")
data class TestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null, val name: String
)