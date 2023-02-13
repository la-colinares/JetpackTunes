package com.lacolinares.jetpacktunes.view.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lacolinares.domain.common.Constants
import com.lacolinares.domain.model.Song
import com.lacolinares.jetpacktunes.R
import com.lacolinares.jetpacktunes.model.SongListState
import com.lacolinares.jetpacktunes.view.components.HorizontalSpace
import com.lacolinares.jetpacktunes.view.components.VerticalSpace
import com.lacolinares.jetpacktunes.view.theme.*
import com.lacolinares.jetpacktunes.viewmodel.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

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
                when {
                    bottomSheetScaffoldState.bottomSheetState.isCollapsed -> {
                        CollapsedMusicPlayer(
                            onExpand = {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }
                        )
                    }
                    bottomSheetScaffoldState.bottomSheetState.isExpanded -> {
                        ExpandedMusicPlayer(
                            onCollapse = {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        )
                    }
                }
            }
            BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
            }
        },
        sheetPeekHeight = Constants.BOTTOM_SHEET_PEAK.dp,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        backgroundColor = VampireBlack,
        sheetBackgroundColor = VampireBlack
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
                modifier = Modifier.padding(top = 24.dp)
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
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun ExpandedMusicPlayer(
    onCollapse: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(VampireBlack),
        state = listState,
        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "Header") {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "NOW PLAYING",
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.Center),
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Icon(
                    painter = painterResource(id = R.drawable.round_arrow_down_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onCollapse.invoke() }
                        .align(Alignment.CenterEnd),
                    tint = White
                )
            }
        }
        item(key = "Song") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                VerticalSpace(height = 24)
                Text(
                    text = "Flowers",
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately
                        ),
                    color = White,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    maxLines = 1
                )
                VerticalSpace(height = 8)
                Text(
                    text = "Miley Cyrus",
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately
                        ),
                    color = OldSilver,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        item(key = "Controller") {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "0:10",
                        modifier = Modifier.weight(1f),
                        color = OldSilver,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start
                    )

                    Slider(
                        value = 10f,
                        onValueChange = {},
                        modifier = Modifier.weight(8f),
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = DarkTangerine,
                            activeTrackColor = DarkTangerine
                        ),
                        thumb = {
                            SliderDefaults.Thumb(
                                interactionSource = MutableInteractionSource(),
                                thumbSize = DpSize(18.dp, 18.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = DarkTangerine,
                                    activeTrackColor = DarkTangerine
                                )
                            )
                        }
                    )

                    Text(
                        text = "3:20",
                        modifier = Modifier.weight(1f),
                        color = OldSilver,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End
                    )
                }
                VerticalSpace(height = 24)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_loop_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .weight(1f)
                            .rotate(90f)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = rememberRipple(bounded = false, color = MetallicYellow)
                            ) {

                            },
                        tint = White
                    )
                    Row(
                        modifier = Modifier.weight(8f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_skip_previous_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { },
                            tint = White
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.round_pause_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clickable { },
                            tint = White
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_skip_next_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { },
                            tint = White
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.round_shuffle_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .weight(1f)
                            .clickable { },
                        tint = White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun CollapsedMusicPlayer(
    onExpand: () -> Unit = {}
) {
    val brush = Brush.verticalGradient(listOf(DarkTangerine, DarkTangerine, MetallicYellow))
    Row(
        modifier = Modifier
            .height(Constants.BOTTOM_SHEET_PEAK.dp)
            .fillMaxWidth()
            .background(brush),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.round_skip_previous_24),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false, color = VampireBlack)
                    ) {

                    },
                tint = White
            )
            Icon(
                painter = painterResource(id = R.drawable.round_pause_24),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false, color = VampireBlack)
                    ) {

                    },
                tint = White
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_skip_next_24),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false, color = VampireBlack)
                    ) {

                    },
                tint = White
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.placeholder_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            HorizontalSpace(width = 8)
            Column(
                modifier = Modifier.widthIn(30.dp, 60.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sample Title",
                    modifier = Modifier
                        .wrapContentWidth()
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately
                        ),
                    color = White,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "Artistaaaaaaaaaaaaaaaa",
                    modifier = Modifier
                        .wrapContentWidth()
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately
                        ),
                    color = White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        Text(
            text = "00:00 / 00:00",
            modifier = Modifier.wrapContentWidth(),
            color = White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Icon(
            painter = painterResource(id = R.drawable.round_arrow_up_24),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(bounded = false, color = VampireBlack)
                ) {
                    onExpand.invoke()
                },
            tint = White
        )
    }
}

@Composable
private fun ScrollableList(
    suggestedSong: Song,
    allTracks: List<SongListState.SongList>,
    viewModel: MainViewModel,
) {

    val uriHandler = LocalUriHandler.current

    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        if (suggestedSong.title.isNotEmpty()) {
            item {
                SuggestedSongItem(
                    suggestedSong = suggestedSong,
                    onPlay = { viewModel.onPlaySong(suggestedSong) },
                    onFollow = { uriHandler.openUri(suggestedSong.followUrl) }
                )
                VerticalSpace(24)
            }
        }
        if (allTracks.isNotEmpty()) {
            items(allTracks, key = { it.category }) {
                MusicList(
                    category = it.category,
                    songs = it.songs,
                    onSelectMusic = { song ->
                        viewModel.onPlaySong(song)
                    }
                )
            }
        }
    }
}

@Composable
private fun MusicList(
    category: String = "",
    songs: List<Song> = emptyList(),
    onSelectMusic: (Song) -> Unit
) {
    val rowState = rememberLazyListState()
    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            text = category,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
        ) {
            items(songs, key = { it.key }) { song ->
                MusicCard(
                    imageUrl = song.coverArt,
                    title = song.title,
                    subtitle = song.subtitle,
                    onClick = { onSelectMusic.invoke(song) }
                )
            }
        }
    }
}

@Composable
private fun MusicCard(
    imageUrl: String = "",
    title: String = "",
    subtitle: String = "",
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick.invoke() }
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
private fun SuggestedSongItem(
    suggestedSong: Song = Song(),
    onPlay: () -> Unit = {},
    onFollow: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = suggestedSong.title,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    animationMode = MarqueeAnimationMode.Immediately
                ),
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
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    animationMode = MarqueeAnimationMode.Immediately
                ),
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
                onClick = { onPlay.invoke() },
                modifier = Modifier.defaultMinSize(minWidth = 115.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MetallicYellow,
                    contentColor = EerieBlack
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
                onClick = { onFollow.invoke() },
                modifier = Modifier.defaultMinSize(minWidth = 115.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = EerieBlack
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
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