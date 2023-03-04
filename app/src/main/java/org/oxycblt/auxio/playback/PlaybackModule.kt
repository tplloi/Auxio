/*
 * Copyright (c) 2023 Auxio Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
 
package org.oxycblt.auxio.playback

import android.content.Context
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.flac.FlacExtractor
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor
import com.google.android.exoplayer2.extractor.ogg.OggExtractor
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor
import com.google.android.exoplayer2.extractor.wav.WavExtractor
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.PlaybackStateManagerImpl

@Module
@InstallIn(SingletonComponent::class)
interface PlaybackModule {
    @Singleton
    @Binds
    fun stateManager(playbackManager: PlaybackStateManagerImpl): PlaybackStateManager
    @Binds fun settings(playbackSettings: PlaybackSettingsImpl): PlaybackSettings
}

@Module
@InstallIn(SingletonComponent::class)
class ExoPlayerModule {
    @Provides
    fun mediaSourceFactory(
        @ApplicationContext context: Context,
        extractorsFactory: ExtractorsFactory
    ): MediaSource.Factory = DefaultMediaSourceFactory(context, extractorsFactory)

    @Provides
    fun extractorsFactory() = ExtractorsFactory {
        arrayOf(
            FlacExtractor(),
            WavExtractor(),
            FragmentedMp4Extractor(),
            Mp4Extractor(),
            OggExtractor(),
            MatroskaExtractor(),
            // Enable constant bitrate seeking so that certain MP3s/AACs are seekable
            AdtsExtractor(AdtsExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING),
            Mp3Extractor(Mp3Extractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING))
    }
}
