package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.creativehub.app.viewmodel.CreatorState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.rememberCreatorState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun CreatorScreen(id: String) {
	val userState = LocalUserState.current
	val user = userState.user
	val creatorState = rememberCreatorState(id, user)
	val swipeRefreshState = rememberSwipeRefreshState(creatorState.isLoading)
	SwipeRefresh(
		state = swipeRefreshState,
		onRefresh = { creatorState.refresh() }
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			CreatorPageHeader(creatorState)
			CreatorTabs(creatorState)
		}
	}
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CreatorTabs(creatorState: CreatorState) {
	val pagerState = rememberPagerState()
	val scope = rememberCoroutineScope()
	val pages = listOf(
		TabInfo("About", Icons.Default.Info) { AboutCreatorTab(creatorState) },
		TabInfo("Portfolio", Icons.Default.List) { PortfolioCreatorTab(creatorState) },
		TabInfo("Events", Icons.Default.Event) { EventsCreatorTab(creatorState) },
		TabInfo("Collab", Icons.Default.People) { CollabsCreatorTab(creatorState) },
	)
	TabRow(
		selectedTabIndex = pagerState.currentPage,
		indicator = { tabPositions ->
			TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
		}
	) {
		pages.forEachIndexed { index, tabInfo ->
			Tab(
				text = { Text(tabInfo.title) },
				selected = pagerState.currentPage == index,
				onClick = {
					scope.launch {
						pagerState.animateScrollToPage(index)
					}
				},
			)
		}
	}
	HorizontalPager(
		count = pages.size,
		state = pagerState
	) { page ->
		pages[page].content()
	}
}

data class TabInfo(
	val title: String,
	val icon: ImageVector,
	val content: @Composable () -> Unit,
)

@Composable
fun CreatorPageHeader(creatorState: CreatorState) {
	Text(text = creatorState.creator?.nickname ?: "?")
	//TODO
}

@Composable
fun AboutCreatorTab(creatorState: CreatorState) {
	//TODO
}

@Composable
fun PortfolioCreatorTab(creatorState: CreatorState) {
	//TODO
}

@Composable
fun EventsCreatorTab(creatorState: CreatorState) {
	//TODO
}

@Composable
fun CollabsCreatorTab(creatorState: CreatorState) {
	//TODO
}
