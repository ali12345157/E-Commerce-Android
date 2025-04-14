import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_commerce_android.CategoriesApi
import com.example.e_commerce_android.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
data class PromoItem(
    val discount: String,
    val description: String,
    val imageRes: Int,
    val backgroundColor: Color
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PromoCarousel() {
    val pagerState = rememberPagerState()
    val items = listOf(
        PromoItem("25%", "For all Headphones & AirPods", R.drawable.headphone, Color(0xFFFFD93D)),
        PromoItem("30%", "For all Makeup & Skincare", R.drawable.makeup, Color(0xFF1C1C1E)),
        PromoItem("20%", "For Laptops & Mobiles", R.drawable.lap, Color(0xFFEFEFEF)),
    )

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            count = items.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 8.dp)
        ) { page ->
            PromoCard(item = items[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Color(0xFF004AAD),
            inactiveColor = Color.LightGray,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun PromoCard(item: PromoItem) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(item.backgroundColor)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Text("UP TO", color = Color.White, fontSize = 14.sp)
            Text("${item.discount} OFF", color = Color(0xFF004AAD), fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(item.description, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Shop Now", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryGrid(viewModel: CategoriesApi, navController: NavController) {
    if (viewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(viewModel.categories.size) { index ->
                    val category = viewModel.categories[index]

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(120.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White)

                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(category.image)
                                    .allowHardware(false)
                                    .build(),
                                contentDescription = category.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = category.name ?: "Unknown",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.smallroute),
            contentDescription = "Small Route",
            modifier = Modifier
                .size(70.dp)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                containerColor = Color.White
            ),
            placeholder = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "What do you search for?",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.shopping),
                        contentDescription = "Shopping",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 8.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        PromoCarousel()

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                color = Color(0xFF06004F),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "View all",
                color = Color(0xFF06004F),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                }
            )
        }

        val viewModel: CategoriesApi = viewModel()

        CategoryGrid(
            viewModel = viewModel,
            navController = navController
        )
    }
}

