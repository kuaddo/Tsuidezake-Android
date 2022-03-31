package jp.kuaddo.tsuidezake.ui.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import jp.kuaddo.tsuidezake.R


@Composable
fun RankingItem() {
    TODO()
}

@Composable
private fun RankingIcon(rank: Int) {
    val density = LocalDensity.current
    val isCrown = rank <= 3
    val rankTopMarginRes =
        if (isCrown) R.dimen.ranking_crown_top_margin
        else R.dimen.ranking_oval_top_margin

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
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.colorAccent))
            )
        }
        Text(
            text = rank.toString(),
            modifier = Modifier.padding(top = dimensionResource(id = rankTopMarginRes)),
            color = colorResource(id = R.color.white),
            fontSize = with(density) { 16.dp.toSp() }
        )
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
