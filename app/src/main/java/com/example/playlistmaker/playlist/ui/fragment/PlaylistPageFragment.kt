package com.example.playlistmaker.playlist.ui.fragment

import Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.creatingPlaylist.ui.fragment.EditPlaylistFragment
import com.example.playlistmaker.databinding.FragmentPlaylistPageBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.playlist.ui.adapter.PlaylistPageAdapter
import com.example.playlistmaker.playlist.ui.viewmodel.PlaylistPageViewModel
import com.example.playlistmaker.root.ui.RootActivity
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.Constatn.KEY_FOR_PLAYER
import com.example.playlistmaker.util.mapper.TrackMapper
import com.example.playlistmaker.util.other.StringsUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistPageFragment : Fragment() {

    companion object {
        private const val EXTRA_PLAYLIST = "EXTRA_PLAYLIST"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(EXTRA_PLAYLIST to playlistId)
    }

    private val viewModel by viewModel<PlaylistPageViewModel>()
    private lateinit var binding: FragmentPlaylistPageBinding

    private var playlistId: Int = 0
    private var playlist: PlaylistDataClass? = null
    private val tracks = arrayListOf<TrackDataClass>()

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var tracksBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var adapter: PlaylistPageAdapter

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
        setupListeners()
        setupBottomSheets()
        setupRecyclerView()
    }

    private fun setupUI() {
        (activity as? RootActivity)?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
        playlistId = requireArguments().getInt(EXTRA_PLAYLIST)
        viewModel.getPlaylistById(playlistId)
    }

    private fun setupObservers() {
        // Получение данных о плейлисте
        viewModel.getPlaylistPageLiveData().observe(viewLifecycleOwner) {
            playlist = it
            if (playlist != null) bind(playlist!!)
        }

        // Получение списка треков плеийлиста
        viewModel.getPlaylistTracksLiveData().observe(viewLifecycleOwner) { tracklist ->
            updateTrackInfo(tracklist)
            showTracks(reverseTrackList(tracklist))
        }
    }

    private fun setupListeners() {
        binding.mainBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.shareButton.setOnClickListener {
            sendPlaylist()
        }

        binding.menuButton.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            tracksBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        setupMenuListeners()
    }

    private fun setupMenuListeners() {
        binding.share.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sendPlaylist()
        }

        binding.editInformation.setOnClickListener {
            findNavController().navigate(R.id.action_playlistPageFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlist!!))
        }

        binding.deletePlaylist.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylist()
        }
    }

    private fun setupBottomSheets() {
        setupTracksBottomSheet()
        setupMenuBottomSheet()
    }

    private fun setupTracksBottomSheet() {
        val tracksBottomSheetContainer = binding.standardBottomSheetOfTracks
        val overlay = binding.overlay
        tracksBottomSheetBehavior = BottomSheetBehavior.from(tracksBottomSheetContainer)

        val menuButton = binding.menuButton

        // Устанавливаем минимальную высоту BottomSheet
        tracksBottomSheetContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tracksBottomSheetContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val parent = tracksBottomSheetContainer.parent as View
                val minHeight = parent.height - menuButton.bottom - resources.getDimensionPixelSize(R.dimen.s24)
                tracksBottomSheetBehavior.peekHeight = minHeight
                tracksBottomSheetBehavior.isFitToContents = false
            }
        })

        tracksBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> overlay.isVisible = false
                    else -> overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset

                // Ограничиваем нижнее положение BottomSheet
                val parent = bottomSheet.parent as View
                val minTop = parent.height - tracksBottomSheetBehavior.peekHeight
                if (bottomSheet.top > minTop) {
                    bottomSheet.y = minTop.toFloat()
                }
            }
        })
    }

    private fun setupMenuBottomSheet() {
        val menuBottomSheetContainer = binding.standardBottomSheetOfPlaylist
        val overlay = binding.overlay
        menuBottomSheetBehavior = BottomSheetBehavior.from(menuBottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        menuBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                overlay.visibility = View.VISIBLE
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> overlay.isVisible = false
                    else -> overlay.isVisible = true
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = PlaylistPageAdapter(
            { track -> if (clickDebounce()) navigateToAudioPlayer(track) },
            { track -> showDeleteTrackDialog(track) }
        )

        adapter.tracks = tracks
        binding.recyclerViewOfTracks.adapter = adapter
        binding.recyclerViewOfTracks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun showDeleteTrackDialog(track: TrackDataClass): Boolean {
        Dialog(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setMessage(getString(R.string.want_to_delete_track))
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteTrack(track)
            }
            .show()
        return true
    }

    private fun deleteTrack(track: TrackDataClass) {
        playlist?.let {
            tracks.remove(track)
            viewModel.updatePlaylist(it, track)
        }
        viewModel.deleteTrack(track.trackId)
        playlist?.tracksListId?.let { viewModel.getPlaylistTrackList(it) }
    }

    private fun updateTrackInfo(tracklist: List<TrackDataClass>) {
        binding.playlistTracksNumber.text = StringsUtil.countTracks(tracklist.size)
        binding.numbersOfTracks.text = StringsUtil.countTracks(tracklist.size)
        binding.playlistDuration.text = playlistDuration(tracklist)
    }

    private fun bind(playlist: PlaylistDataClass) {
        bindMainScreen(playlist)
        bindMenu(playlist)
    }

    private fun bindMainScreen(playlist: PlaylistDataClass) {
        Glide.with(this)
            .load(playlist.uriOfImage)
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .into(binding.playlistTitle)
        binding.playlistName.text = playlist.playlistName
        binding.playlistDescription.text = playlist.descriptionPlaylist
    }

    private fun bindMenu(playlist: PlaylistDataClass) {
        Glide.with(this)
            .load(playlist.uriOfImage)
            .placeholder(R.drawable.placeholder_of_track)
            .centerCrop()
            .into(binding.playlistTitleInElement)
        binding.playlistNameInElement.text = playlist.playlistName
    }

    private fun playlistDuration(tracklist: List<TrackDataClass>): String {
        var duration = 0.0f
        for (track: TrackDataClass in tracklist) {
            val srtList = track.trackTimeMillis.split(":")
            val min = srtList[0].toInt()
            val sec = srtList[1].toInt()
            duration += min + sec / 60.0f
        }
        val durationToInt = duration.toInt()
        return StringsUtil.countMinutes(durationToInt)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onStop() {
        super.onStop()
        isClickAllowed = true
    }

    override fun onStart() {
        super.onStart()
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showTracks(tracklist: List<TrackDataClass>) {
        if (tracklist.isEmpty()) {
            binding.recyclerViewOfTracks.isVisible = false
            binding.whenNoTracks.isVisible = true
        } else {
            binding.whenNoTracks.isVisible = false
            binding.recyclerViewOfTracks.isVisible = true
            tracks.clear()
            tracks.addAll(tracklist)
            adapter.tracks = tracks
            adapter.notifyDataSetChanged()
        }
    }

    private fun navigateToAudioPlayer(track: TrackDataClass) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        val trackForPlayer = TrackMapper.mapTrackDomainToUi(track).copy(
            trackTimeMillis = convertTimeToMillis(track.trackTimeMillis).toString()
        )
        intent.putExtra(KEY_FOR_PLAYER, trackForPlayer)
        startActivity(intent)
    }

    private fun convertTimeToMillis(timeString: String): Long {
        val parts = timeString.split(":")
        val minutes = parts[0].toLong()
        val seconds = parts[1].toLong()
        return (minutes * 60 + seconds) * 1000
    }

    private fun reverseTrackList(tracks: List<TrackDataClass>): List<TrackDataClass> {
        return tracks.asReversed()
    }

    private fun deletePlaylist() {
        Dialog(requireContext())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(getString(R.string.want_to_delete_playlist) + " \"" + playlist?.playlistName + "\"?")
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val idTracks = mutableListOf<Int>()
                idTracks.addAll(playlist!!.tracksListId)
                viewModel.deletePlaylistById(playlistId, idTracks)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .show()
    }

    private fun sendPlaylist() {
        if (tracks.isEmpty()) {
            showEmptyPlaylistDialog()
        } else {
            val strToSend = buildPlaylistShareString()
            sharePlaylist(strToSend)
        }
    }

    private fun showEmptyPlaylistDialog() {
        Dialog(requireContext())
            .setTitle(getString(R.string.attention))
            .setMessage(getString(R.string.nothing_to_share))
            .setNegativeButton(getString(R.string.ok)) { _, _ -> }
            .show()
    }

    // построение строки для отправки плейлиста
    private fun buildPlaylistShareString(): String {
        var strToSend = "${playlist?.playlistName}\n${playlist?.descriptionPlaylist ?: ""}\n${StringsUtil.countTracks(tracks.size)}\n"
        for ((index, track) in tracks.withIndex()) {
            strToSend += "\n${index + 1}. ${track.artistName} '${track.trackName}' ${track.trackTimeMillis}"
        }
        return strToSend
    }

    private fun sharePlaylist(strToSend: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, strToSend)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.email))
        startActivity(chooserIntent)
    }
}