package josianvanefferen.tech2_addressgeocoding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import josianvanefferen.tech2_addressgeocoding.ui.theme.Tech2AddressGeocodingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tech2AddressGeocodingTheme {
                GeocodingApp()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GeocodingApp() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppTitle()
            AddressInputFields()
        }
    }
    
    @Composable
    fun AppTitle() {
        Text(
            text = "Josian's Geocoding App",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
    }

    @Composable
    fun AddressInputFields() {
        val apiKey = BuildConfig.apiKey
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Street Name:") },
                modifier = Modifier.padding(10.dp)
            )

            TextField(
                value = "",
                onValueChange = {},
                label = { Text("House Number:") },
                modifier = Modifier.padding(10.dp)
            )

            TextField(
                value = "",
                onValueChange = {},
                label = { Text("City:") },
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

