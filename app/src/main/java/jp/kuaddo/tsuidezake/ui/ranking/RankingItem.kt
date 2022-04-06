package jp.kuaddo.tsuidezake.ui.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.material.composethemeadapter.MdcTheme
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail

@Composable
fun RankingItem(rankingContent: Ranking.Content, onClickItem: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RankingIcon(rank = rankingContent.rank)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(rankingContent.sakeDetail.imageUri)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(68.dp),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Text(
            text = rankingContent.sakeDetail.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            color = colorResource(id = R.color.textPrimaryColor),
            fontSize = 16.sp
        )
    }
}

@Composable
private fun RankingIcon(rank: Int) {
    val density = LocalDensity.current
    val isCrown = rank <= 3

    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
        if (isCrown) {
            Image(
                painter = painterResource(id = R.drawable.ic_crown),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(colorResource(id = R.color.colorAccent))
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.ranking_oval_padding))
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.colorAccent))
            )
        }
        Text(
            text = rank.toString(),
            modifier = Modifier.padding(
                top = dimensionResource(
                    id = if (isCrown) R.dimen.ranking_crown_top_margin
                    else R.dimen.ranking_oval_top_margin
                )
            ),
            color = colorResource(id = R.color.white),
            fontSize = with(density) { 16.dp.toSp() }
        )
    }
}

@Preview
@Composable
fun PreviewRankingItem() {
    val rankingContent = Ranking.Content(
        rank = 3,
        sakeDetail = SakeDetail(
            id = 1,
            name = "獺祭",
            description = null,
            region = "",
            brewer = null,
            imageUri = null,
            tags = emptyList(),
            suitableTemperatures = emptySet(),
            goodFoodCategories = emptySet()
        )
    )
    MdcTheme {
        RankingItem(rankingContent) {}
    }
}

@Preview(name = "Crown ranking icon")
@Composable
fun PreviewCrownRankingIcon() {
    MdcTheme {
        RankingIcon(1)
    }
}

@Preview(name = "Oval ranking icon")
@Composable
fun PreviewOvalRankingIcon() {
    MdcTheme {
        RankingIcon(5)
    }
}
