package com.creativehub.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapElement(latitude: Double, longitude: Double, positionName: String) {
	val location = LatLng(latitude, longitude)
	val cameraPositionState = rememberCameraPositionState {
		position = CameraPosition.fromLatLngZoom(location, 14f)
	}
	var uiSettings by remember { mutableStateOf(MapUiSettings()) }
	val markerState = rememberMarkerState(null, location)


	Box(Modifier.height(250.dp)
			.fillMaxWidth()) {

		GoogleMap(
			modifier = Modifier.fillMaxSize(),
			cameraPositionState = cameraPositionState,
			uiSettings = uiSettings
		) {

			Marker(
				state = markerState,
				title = positionName,
				snippet = "Location of event is here!",
				visible = true
			)
		}
		Switch(checked = uiSettings.zoomControlsEnabled,
			   onCheckedChange = {
				   uiSettings = uiSettings.copy(zoomControlsEnabled = it)
			   })
	}
}