package com.lacolinares.jetpacktunes.view.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lacolinares.domain.model.Song
import com.lacolinares.jetpacktunes.R
import com.lacolinares.jetpacktunes.model.SongListState
import com.lacolinares.jetpacktunes.view.components.HorizontalSpace
import com.lacolinares.jetpacktunes.view.components.VerticalSpace
import com.lacolinares.jetpacktunes.view.theme.*
import com.lacolinares.jetpacktunes.viewmodel.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterialApi::class)
@Destination(start = true)
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: MainViewModel) {

    val suggestedSong = viewModel.suggestedSongState.collectAsState().value
    val allTracks = viewModel.songListState.collectAsState().value

    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Hello World", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        backgroundColor = VampireBlack,

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(VampireBlack)
        ) {
            if (suggestedSong.loading.not() && suggestedSong.song.backgroundImage.isNotEmpty()) {
                BackgroundImage(imageUrl = suggestedSong.song.backgroundImage)
            }
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 20.dp,
                    end = 20.dp
                )
            ) {
                HeaderItem()
                VerticalSpace(24)
                SearchItem()
                VerticalSpace(24)
                if (suggestedSong.loading || allTracks.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MetallicYellow)
                    }
                } else {
                    ScrollableList(
                        suggestedSong = suggestedSong.song,
                        allTracks = allTracks.songs,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomMusicPlayer() {

}

@Composable
private fun ScrollableList(
    suggestedSong: Song,
    allTracks: List<SongListState.SongList>,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        if (suggestedSong.title.isNotEmpty()) {
            item {
                SuggestedSongItem(suggestedSong)
                VerticalSpace(24)
            }
        }
        if (allTracks.isNotEmpty()) {
            items(allTracks, key = { it.category }) {
                MusicList(category = it.category, songs = it.songs)
            }
        }
    }
}

@Composable
private fun MusicList(
    category: String = "",
    songs: List<Song> = emptyList(),
) {
    val rowState = rememberLazyListState()
    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            text = category,
            modifier = Modifier.fillMaxWidth(),
            color = White,
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        VerticalSpace(height = 8)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = rowState,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(songs, key = { it.key }) {
                MusicCard(imageUrl = it.coverArt, title = it.title, subtitle = it.subtitle)
            }
        }
    }
}

@Composable
private fun MusicCard(
    imageUrl: String = "",
    title: String = "",
    subtitle: String = "",
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { }
    ) {
        AsyncImage(
            model = imageUrl.ifEmpty { R.drawable.placeholder_icon },
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
        VerticalSpace(4)
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            color = White,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = subtitle,
            modifier = Modifier.fillMaxWidth(),
            color = OldSilver,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SuggestedSongItem(suggestedSong: Song = Song()) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = suggestedSong.title,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            color = White,
            fontSize = 32.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            maxLines = 1
        )
        Text(
            text = suggestedSong.subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            color = OldSilver,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { },
                modifier = Modifier.defaultMinSize(minWidth = 115.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MetallicYellow,
                    contentColor = OldSilver
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.play_now),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(bottom = 2.dp),
                    color = VampireBlack,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalSpace(width = 8)
            Button(
                onClick = { },
                modifier = Modifier.defaultMinSize(minWidth = 115.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = OldSilver
                ),
                border = BorderStroke(1.dp, MetallicYellow)
            ) {
                Text(
                    text = stringResource(id = R.string.follow),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(bottom = 2.dp),
                    color = MetallicYellow,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BackgroundImage(imageUrl: String) {
    val largeRadialGradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(EerieBlack, Black),
                center = size.center,
                radius = biggerDimension / 2f,
                colorStops = listOf(0f, 0.95f)
            )
        }
    }

    AsyncImage(
        model = imageUrl,
        contentDescription = "background",
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.9f)
            .background(
                brush = largeRadialGradient
            )
    )

}

@Composable
private fun HeaderItem() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.fillMaxWidth(),
            color = MetallicYellow,
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.app_description),
            modifier = Modifier.fillMaxWidth(),
            color = OldSilver,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchItem() {
    Button(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = EerieBlack,
            contentColor = OldSilver
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.search_music),
                tint = OldSilver
            )
            Text(
                text = stringResource(id = R.string.search_music),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(bottom = 2.dp),
                color = OldSilver,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}