package josianvanefferen.tech2_addressgeocoding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import josianvanefferen.tech2_addressgeocoding.ui.theme.Tech2AddressGeocodingTheme
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

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
    fun GeocodingApp(
        mainViewModel: MainViewModel = viewModel()
    ) {
        val mainUiState by mainViewModel.uiState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppTitle()
            AddressInputFields(mainViewModel = mainViewModel)
            GoogleMapCompose(mainUiState.currentCoordinates)
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
    fun AddressInputFields(mainViewModel: MainViewModel) {
        var streetNameInput by remember {mutableStateOf("")}
        var houseNumberInput by remember {mutableStateOf("")}
        var cityInput by remember {mutableStateOf("")}
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = streetNameInput,
                onValueChange = {
                    streetNameInput = it
                                },
                label = { Text("Street Name") },
                modifier = Modifier.padding(10.dp)
            )

            TextField(
                value = houseNumberInput,
                onValueChange = {
                    houseNumberInput = it
                                },
                label = { Text("House Number") },
                modifier = Modifier.padding(10.dp)
            )

            TextField(
                value = cityInput,
                onValueChange = {
                    cityInput = it
                                },
                label = { Text("City") },
                modifier = Modifier.padding(10.dp)
            )
            
            Button(onClick = { mainViewModel.requestGeocodingData(streetNameInput, houseNumberInput, cityInput) }) {
                Text(text = "Search Address")
            }
        }
    }

    @Composable
    fun GoogleMapCompose(currentCoordinates: LatLng) {
        GoogleMap(
            cameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(currentCoordinates, 16f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Marker(
                state = MarkerState(position = currentCoordinates),
                title = "Your Entered Location"
            )
        }

    }
}

