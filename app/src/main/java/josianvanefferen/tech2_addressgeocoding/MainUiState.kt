package josianvanefferen.tech2_addressgeocoding

import com.google.android.gms.maps.model.LatLng

data class MainUiState(
    val currentCoordinates: LatLng = LatLng(0.0, 0.0)
)