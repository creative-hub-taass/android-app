package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.components.*
import com.creativehub.app.viewmodel.CreatorState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
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
			CreatorScreenTabs(creatorState)
		}
	}
}

data class Page(
	val title: String,
	val icon: ImageVector,
	val content: @Composable () -> Unit,
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CreatorScreenTabs(creatorState: CreatorState) {
	val pagerState = rememberPagerState()
	val scope = rememberCoroutineScope()
	val pages = listOf(
		Page("About", Icons.Default.Info) { AboutCreatorTab(creatorState) },
		Page("Portfolio", Icons.Default.Dashboard) { PortfolioCreatorTab(creatorState) },
		Page("Events", Icons.Default.Event) { EventsCreatorTab(creatorState) },
		Page("Collabs", Icons.Default.People) { CollabsCreatorTab(creatorState) },
		Page("Shop", Icons.Default.Shop) { ShopCreatorTab(creatorState) },
	)
	ScrollableTabRow(
		modifier = Modifier.fillMaxWidth(),
		selectedTabIndex = pagerState.currentPage,
		indicator = { tabPositions ->
			TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
		}
	) {
		pages.forEachIndexed { index, tabInfo ->
			Tab(
				text = { Text(tabInfo.title) },
				icon = { Icon(tabInfo.icon, contentDescription = null) },
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
		modifier = Modifier.fillMaxWidth(),
		count = pages.size,
		state = pagerState
	) { page ->
		pages[page].content()
	}
}

@Preview
@Composable
fun CreatorScreenPreview() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		CreatorScreen("")
	}
}