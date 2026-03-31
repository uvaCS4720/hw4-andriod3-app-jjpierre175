package edu.nd.pmcburne.hello

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MainViewModel) {
    val tags by viewModel.tags.collectAsState() // list of tags from viewmodel
    val selectedTag by viewModel.selectedTag.collectAsState() // current selected tag from viewmodel
    val locations by viewModel.filtered.collectAsState() // filtered list of locations to display

    var dropdownExpanded by remember { mutableStateOf(false) }

    val uvaCenter = LatLng(38.0336, -78.5080) // start center point of the map
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uvaCenter, 15f)
    }

    Scaffold( // top app bar
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "UVA Campus Map",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF232D4B)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // pushes content below the top bar
        ) {

            // dropdown
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedTag,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter by Tag") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            text = { Text(tag) },
                            onClick = {
                                viewModel.selectTag(tag)
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraState
            ) {
                locations.forEach { location ->
                    Marker(
                        state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                        title = location.name,
                        snippet = location.description
                    )
                }
            }
        }
    }
}