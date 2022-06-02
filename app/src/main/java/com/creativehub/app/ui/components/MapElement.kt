package com.creativehub.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creativehub.app.model.Event
import com.creativehub.app.util.getPreviewEvent
import com.creativehub.app.util.toLatLng
import com.creativehub.app.util.toLatLngBounds
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapElement(events: List<Event>?) {
	if (events == null) {
		Box(
			modifier = Modifier
				.height(250.dp)
				.fillMaxWidth()
				.placeholder(true, highlight = PlaceholderHighlight.shimmer()),
		)
		return
	}
	if (events.isEmpty()) return
	val scope = rememberCoroutineScope()
	val latLngBounds = events.map { it.coordinates.toLatLng() }.toLatLngBounds()
	val cameraPositionState = rememberCameraPositionState {
		position = CameraPosition.fromLatLngZoom(latLngBounds.center, 14f)
	}
	val resetPosition = {
		scope.launch {
			val cameraUpdate = when (events.size) {
				1 -> CameraUpdateFactory.newLatLngZoom(events.single().coordinates.toLatLng(), 14f)
				else -> CameraUpdateFactory.newLatLngBounds(latLngBounds, 16)
			}
			cameraPositionState.animate(cameraUpdate)
		}
	}
	Box(
		modifier = Modifier
			.height(250.dp)
			.fillMaxWidth(),
		contentAlignment = Alignment.TopEnd
	) {
		GoogleMap(
			modifier = Modifier.fillMaxSize(),
			cameraPositionState = cameraPositionState,
			onMapLoaded = { resetPosition() }
		) {
			for (event in events) {
				Marker(
					position = event.coordinates.toLatLng(),
					title = event.name,
					snippet = event.locationName,
					visible = true
				)
			}
		}
		Button(
			modifier = Modifier
				.padding(14.dp)
				.size(40.dp),
			contentPadding = PaddingValues(8.dp),
			onClick = { resetPosition() },
		) {
			Icon(
				imageVector = Icons.Default.MyLocation,
				contentDescription = "reset location"
			)
		}
	}
}

@Preview
@Composable
fun MapElementPreview() {
	MapElement(events = listOf(getPreviewEvent().publication))
}