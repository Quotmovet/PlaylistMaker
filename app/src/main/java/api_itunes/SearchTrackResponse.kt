package api_itunes

import com.example.playlistmaker.activites.for_search.TrackDataClass

class SearchTrackResponse (val resultCount: Int,
                           val results: List<TrackDataClass>)