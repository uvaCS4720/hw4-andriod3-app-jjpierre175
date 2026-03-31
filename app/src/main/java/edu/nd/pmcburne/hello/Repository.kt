package edu.nd.pmcburne.hello
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(private val dao: LocationDao) {
    private val api = Retrofit.Builder() // retrofit instance used to make network requests
        .baseUrl("https://www.cs.virginia.edu/~wxt4gm/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PlacemarksApi::class.java)

    suspend fun syncFromApi() { // call on start which gets api & sync to db
        val dtos = api.getPlacemarks() // gets placemark data from
        val entities = dtos.map { dto -> // converts api dto objects into db location entities
            Location(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                latitude = dto.visual_center.latitude,
                longitude = dto.visual_center.longitude,
                tags = dto.tag_list.joinToString(",") // convert list of tags into comma separated string
            )
        }
        dao.insertAll(entities) // replace strategy handles duplicates
    }

    suspend fun getAllLocations(): List<Location> = dao.getAllLocations() // get all saved locations from the db

    suspend fun getAllTags(): List<String> { // parse comma separated tags back into unique sorted list
        return dao.getAllLocations() // get all locations
            .flatMap { it.tags.split(",") }
            .map {it.trim()}
            .filter{it.isNotEmpty()}
            .distinct()
            .sorted()
    }

}