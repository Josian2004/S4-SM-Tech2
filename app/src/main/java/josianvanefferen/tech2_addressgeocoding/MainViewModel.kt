package josianvanefferen.tech2_addressgeocoding

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState>  = _uiState.asStateFlow()

    fun requestGeocodingData (streetName:String, houseNumber:String, city:String) {
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
                        try {
                            val lat = responseJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat") as Double
                            val lng = responseJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng") as Double
                            val currentCoordinates = LatLng(lat, lng)
                            println(currentCoordinates)

                            _uiState.update { currentState ->
                                currentState.copy(
                                    currentCoordinates = currentCoordinates
                                )
                            }
                        }
                        catch (e: JSONException) {
                            println(e)
                        }
                    }
                }
        })
    }
}