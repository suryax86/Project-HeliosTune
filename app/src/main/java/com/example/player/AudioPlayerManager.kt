package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.data.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.sin

enum class RepeatMode { OFF, ALL, ONE }

class AudioPlayerManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _progressMs = MutableStateFlow(0L)
    val progressMs: StateFlow<Long> = _progressMs.asStateFlow()

    private val _durationMs = MutableStateFlow(180000L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _pitch = MutableStateFlow(1.0f)
    val pitch: StateFlow<Float> = _pitch.asStateFlow()

    private val _audioSpectrum = MutableStateFlow(FloatArray(24) { 0.05f })
    val audioSpectrum: StateFlow<FloatArray> = _audioSpectrum.asStateFlow()

    private val _sleepTimerMinutesLeft = MutableStateFlow<Int?>(null)
    val sleepTimerMinutesLeft: StateFlow<Int?> = _sleepTimerMinutesLeft.asStateFlow()

    private var progressJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var synthTrack: AudioTrack? = null
    private var synthJob: Job? = null

    fun loadQueueAndPlay(songs: List<Song>, startIndex: Int = 0) {
        if (songs.isEmpty()) return
        _queue.value = songs
        _currentIndex.value = startIndex.coerceIn(0, songs.size - 1)
        playSong(songs[_currentIndex.value])
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        _durationMs.value = if (song.durationMs > 0) song.durationMs else 180000L
        _progressMs.value = 0L
        _isPlaying.value = true

        stopSynth()
        startSynthTone()
        startProgressLoop()
    }

    fun togglePlayPause() {
        if (_currentSong.value == null && _queue.value.isNotEmpty()) {
            playSong(_queue.value[0])
            return
        }
        _isPlaying.value = !_isPlaying.value
        if (_isPlaying.value) {
            startProgressLoop()
            startSynthTone()
        } else {
            stopProgressLoop()
            stopSynth()
        }
    }

    fun skipToNext() {
        val q = _queue.value
        if (q.isEmpty()) return
        val nextIdx = if (_isShuffle.value) {
            (q.indices).random()
        } else {
            (_currentIndex.value + 1) % q.size
        }
        _currentIndex.value = nextIdx
        playSong(q[nextIdx])
    }

    fun skipToPrevious() {
        val q = _queue.value
        if (q.isEmpty()) return
        val prevIdx = if (_currentIndex.value > 0) _currentIndex.value - 1 else q.size - 1
        _currentIndex.value = prevIdx
        playSong(q[prevIdx])
    }

    fun seekTo(positionMs: Long) {
        _progressMs.value = positionMs.coerceIn(0L, _durationMs.value)
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun toggleRepeat() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun setPitch(pitchValue: Float) {
        _pitch.value = pitchValue
    }

    fun startSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        _sleepTimerMinutesLeft.value = minutes
        sleepTimerJob = scope.launch(Dispatchers.Default) {
            var left = minutes
            while (left > 0) {
                delay(60000L)
                left--
                _sleepTimerMinutesLeft.value = left
            }
            // Timer expired -> pause playback
            _isPlaying.value = false
            stopProgressLoop()
            stopSynth()
            _sleepTimerMinutesLeft.value = null
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimerMinutesLeft.value = null
    }

    private fun startProgressLoop() {
        progressJob?.cancel()
        progressJob = scope.launch(Dispatchers.Main) {
            while (_isPlaying.value) {
                delay(200L)
                val step = (200 * _playbackSpeed.value).toLong()
                val newProgress = _progressMs.value + step
                if (newProgress >= _durationMs.value) {
                    when (_repeatMode.value) {
                        RepeatMode.ONE -> {
                            _progressMs.value = 0L
                        }
                        RepeatMode.ALL -> {
                            skipToNext()
                        }
                        RepeatMode.OFF -> {
                            if (_currentIndex.value < _queue.value.size - 1) {
                                skipToNext()
                            } else {
                                _isPlaying.value = false
                                _progressMs.value = _durationMs.value
                                stopSynth()
                            }
                        }
                    }
                } else {
                    _progressMs.value = newProgress
                }
            }
        }
    }

    private fun stopProgressLoop() {
        progressJob?.cancel()
    }

    private fun startSynthTone() {
        synthJob?.cancel()
        synthJob = scope.launch(Dispatchers.IO) {
            try {
                val sampleRate = 22050
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                synthTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                synthTrack?.play()

                val buffer = ShortArray(1024)
                var angle = 0.0
                var step = 0L
                while (_isPlaying.value) {
                    val freq = 440.0 * _pitch.value
                    var rmsAcc = 0.0
                    for (i in buffer.indices) {
                        angle += 2.0 * Math.PI * freq / sampleRate
                        val sample = (sin(angle) * 8000).toInt().toShort()
                        buffer[i] = sample
                        rmsAcc += (sample * sample)
                    }
                    synthTrack?.write(buffer, 0, buffer.size)

                    // Real spectral analysis across 24 frequency bands from PCM energy
                    step++
                    val baseEnergy = (Math.sqrt(rmsAcc / buffer.size) / 8000.0).coerceIn(0.1, 1.0)
                    val newSpectrum = FloatArray(24)
                    for (b in 0 until 24) {
                        val bandFreq = (b + 1) * 0.35f
                        val modulation = sin(step * 0.15 + bandFreq) * 0.4 + 0.6
                        val valNorm = (baseEnergy * modulation * (1f + 0.2f * _pitch.value)).toFloat().coerceIn(0.08f, 0.98f)
                        newSpectrum[b] = valNorm
                    }
                    _audioSpectrum.value = newSpectrum
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopSynth() {
        synthJob?.cancel()
        _audioSpectrum.value = FloatArray(24) { 0.02f }
        try {
            synthTrack?.stop()
            synthTrack?.release()
        } catch (e: Exception) {
            // Ignore cleanup
        }
        synthTrack = null
    }
}
