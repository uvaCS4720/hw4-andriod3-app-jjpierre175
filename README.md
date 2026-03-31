Campus Maps 
This app displays campus locations on a Google Map and allows users to filter locations by tags.
Location data is retrieved from a remote API, stored locally using a Room database, and displayed with map markers using Google Maps Compose

Features
- displays campus locations on an interactive Google Map
- gets location data from a remote api using retrofit
- stores location data locally using room database
- filters locations by tag using a dropdown menu
- dynamically updates map marker when the selected tag changes

Architecture
- API Layer: uses retrofit placemark data from a remote json endpoint
- Repository: handles api requests and db operations, converts api data into db entities
- Database: implemented using room, stores location information including coordinates and tags
- ViewModel: manages ui state using stateflow, filters locations based on the selected tag
- UI: displays a google map with markers for each location, includes a dropdown menu to filter markers by tag

Filtering System
1. the selected tag updates in the viewmodel
2. locations are filtered to include only those containing the selected tag
3. the map markers update automatically 