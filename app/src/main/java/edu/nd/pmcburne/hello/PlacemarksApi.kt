package edu.nd.pmcburne.hello
import retrofit2.http.GET

data class VisualCenter(val latitude: Double, val longitude: Double)
data class PlacemarkDto(
    val id: Int,
    val name: String,
    val description: String,
    val tag_list: List<String>, // list of tags associated with the placemark
    val visual_center: VisualCenter // geographic coords used to display the location on the map
)

interface PlacemarksApi{
    @GET("placemarks.json") // sends a get request to get placemark data
    suspend fun getPlacemarks(): List<PlacemarkDto> // returns a list of placemarkdto objects
}