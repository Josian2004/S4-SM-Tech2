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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import josianvanefferen.tech2_addressgeocoding.ui.theme.Tech2AddressGeocodingTheme
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {
    private var streetName:String = ""
    private var houseNumber:String = ""
    private var city:String = ""
    private var currentCoords:LatLng = LatLng(0.0,0.0)
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
            GoogleMapCompose()
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
        var streetNameInput by remember {mutableStateOf(streetName)}
        var houseNumberInput by remember {mutableStateOf(houseNumber)}
        var cityInput by remember {mutableStateOf(city)}
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
                    streetName = it
                                },
                label = { Text("Street Name") },
                modifier = Modifier.padding(10.dp)
            )

            TextField(
                value = houseNumberInput,
                onValueChange = {
                    houseNumberInput = it
                    houseNumber = it
                                },
                label = { Text("House Number") },
                modifier = Modifier.padding(10.dp)
            )

            TextField(
                value = cityInput,
                onValueChange = {
                    cityInput = it
                    city = it
                                },
                label = { Text("City") },
                modifier = Modifier.padding(10.dp)
            )
            
            Button(onClick = { requestGeocodingData() }) {
                Text(text = "Search Address")
            }
        }
    }

    @Composable
    fun GoogleMapCompose() {
        var coords by remember { mutableStateOf(currentCoords) }
        val onValueChange = { currentCoords:LatLng -> coords = currentCoords}
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(coords, 10f)
        }
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxWidth()
        ) {
            Marker(
                state = MarkerState(position = coords),
                title = "Your Entered Location"
            )
        }

    }

    private fun requestGeocodingData () {
        val client = OkHttpClient()
        val apiKey = BuildConfig.MAPS_API_KEY
        streetName.replace("\\s+".toRegex(), "+")
        println("https://maps.googleapis.com/maps/api/geocode/json?address=${houseNumber}+${streetName},+${city}&key=INSERT_KEY")
        val request = Request.Builder()
            .url("https://maps.googleapis.com/maps/api/geocode/json?address=${houseNumber}+${streetName},+${city}&key=${apiKey}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) =
                run {
                    val responseString = response.body()?.string()
                    println(responseString)
                    if (!responseString.isNullOrEmpty()) {
                        val responseJson = JSONObject(responseString)
                        val lat = responseJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat") as Double
                        val lng = responseJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng") as Double
                        currentCoords = LatLng(lat, lng)
                        println(currentCoords)
                    }
                }
        })
    }


}

