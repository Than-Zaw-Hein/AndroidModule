package com.tzh.android_module

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.tzh.android_module.ui.theme.AndroidModuleTheme
import com.tzh.database_module.AppDataBase
import com.tzh.database_module.data.entity.TestEntity
import com.tzh.datastore_module.SharePreferenceHelper
import com.tzh.retrofit_module.ApiClient
import com.tzh.retrofit_module.RetrofitConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var appDataBase: AppDataBase

    @Inject
    lateinit var userClass: UserClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseApiInterface = ApiClient.createApiInterface(RetrofitConstant.BASE_URL)
        appDataBase = AppDataBase.getInstance(this)
        val sharePreferenceHelper = SharePreferenceHelper(this)

        lifecycleScope.launch {
            try {
                val response = baseApiInterface.getHealthCheck()
                sharePreferenceHelper.apiUrl = "ok"
                val myDao = appDataBase.appDao()
                val data = TestEntity(name = "TEST")
                myDao.insertData(data)
                Log.e("DATA", myDao.getAllTestData().toString())
                Log.e("TEST API URL", sharePreferenceHelper.apiUrl.toString())
                Log.e("TEST API URL", userClass.baseData)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }

        setContent {
            AndroidModuleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidModuleTheme {
        Greeting("Android")
    }
}