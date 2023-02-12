package com.lacolinares.jetpacktunes.view.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lacolinares.jetpacktunes.R
import com.lacolinares.jetpacktunes.view.components.Space
import com.lacolinares.jetpacktunes.view.theme.*
import com.lacolinares.jetpacktunes.viewmodel.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterialApi::class)
@Destination(start = true)
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: MainViewModel) {

    val suggestedSong = viewModel.suggestedSongState.collectAsState().value

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
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
        }, sheetPeekHeight = 0.dp
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
                    vertical = 24.dp,
                    horizontal = 20.dp
                )
            ) {
                HeaderItem()
                Space(24)
                SearchItem()
                Space(24)
                ScrollableList()
            }
        }
    }
}

@Composable
private fun BottomMusicPlayer() {

}

@Composable
private fun ScrollableList(){

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


@Preview(showBackground = true, showSystemUi = true)
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