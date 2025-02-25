package com.example.gdgk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gdgk.ui.theme.GDGKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GDGKTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "shoppingApp"
                ) {
                    composable("shoppingApp") {
                        ShoppingApp(navController)
                    }
                    composable("productDetail/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                        val product = products.find { it.id == productId }
                        if (product != null) {
                            ProductDetailScreen(product, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingApp(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(navController) },
        bottomBar = { BottomNavigationBar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ProductList(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text("New Arrivals") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { /* Open filter */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter), // You'll need to add this resource
                    contentDescription = "Filter"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFF0E6FF)
        )
    )
}

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false,
            onClick = { /* Navigate to home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            selected = false,
            onClick = { /* Navigate to cart */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            selected = false,
            onClick = { /* Navigate to profile */ }
        )
    }
}

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val rating: Float,
    val imageResId: Int
)

val products = listOf(
    Product(1, "Áo thun tím", "95% Cotton, 5% Spandex, Đặc điểm: Thường ngày, Tay áo ngắn, In chữ, Cổ chữ V, Áo thun thời trang. Chất vải mềm mại và có độ co giãn một chút.", 12.99, 4.5f, R.drawable.purple_tshirt),
    Product(2, "Áo thun đỏ", "100% Polyester, Giặt máy, 100% polyester cation khóa liên động, Giặt máy & Thu nhỏ trước để vừa vặn", 7.95, 0.0f, R.drawable.red_tshirt),
    Product(3, "Áo thun trắng", "95% RAYON 5% SPANDEX, Sản xuất tại Mỹ hoặc nhập khẩu, Không tẩy trắng, Vải nhẹ có độ co giãn tuyệt vời tạo cảm giác thoải mái", 9.85, 0.0f, R.drawable.white_tshirt),
    Product(4, "Áo khoác đi mưa", "Nhẹ hoàn hảo cho chuyến đi hoặc trang phục thường ngày. Tay áo dài có mũ trùm đầu, thiết kế dây rút điều chỉnh ở eo. Nút và dây kéo", 39.99, 0.0f, R.drawable.rain_jacket),
    Product(5, "Áo da bò", "100% POLYURETHANE(vỏ) 100% POLYESTER(lớp lót) 75% POLYESTER 25% COTTON (Áo len), Chất liệu giả da", 29.95, 4.6f, R.drawable.leather_jacket)
)

@Composable
fun ProductList(modifier: Modifier = Modifier, navController: NavController) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ProductCard(product, navController)
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            navController.navigate("productDetail/${product.id}")
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Ảnh sp
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .width(100.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            // Cột thông tin sp
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                // Tên sp
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // Mô tả sp
                Text(
                    text = product.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )

                // Đánh
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${product.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Giá và nút thêm sp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    IconButton(
                        onClick = { /* Add to cart */ },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Thêm vào giỏ hàng"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(product: Product, navController: NavController) {
    Scaffold(
        topBar = { ProductDetailTopBar(navController) },
        bottomBar = { ProductDetailBottomBar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ProductDetailContent(
            modifier = Modifier.padding(innerPadding),
            product = product
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text("Chi tiết sản phẩm") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFF0E6FF)
        )
    )
}

@Composable
fun ProductDetailBottomBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
            selected = false,
            onClick = { /* Navigate to home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng") },
            selected = false,
            onClick = { /* Navigate to cart */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Cá nhân") },
            selected = false,
            onClick = { /* Navigate to profile */ }
        )
    }
}

@Composable
fun ProductDetailContent(modifier: Modifier = Modifier, product: Product) {
    val reviews = listOf(
        Review(
            id = 1,
            userName = "Thu Hien",
            rating = 5f,
            comment = "Sản phẩm rất tốt! Gửi hàng nhanh, áo đúng như mô tả. 100% mua lại.",
            date = "5/10/2023"
        ),
        Review(
            id = 2,
            userName = "Anh Quan",
            comment = "Chất liệu vải khá dày, phù hợp với giá tiền.",
            rating = 4f,
            date = "7/09/2023"
        ),
        Review(
            id = 3,
            userName = "Ahmed Khan",
            comment = "Mình đã mua cho vợ, cô ấy rất thích.",
            rating = 5f,
            date = "12/08/2023"
        )
    )

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color(0xFFF8F8F8))
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }

            // Product Title and Description
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(rating = product.rating)
                    Text(
                        text = "(${product.rating})",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* Add to cart */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    )
                ) {
                    Text(
                        text = "THÊM VÀO GIỎ HÀNG",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Divider()

            // Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Chi tiết",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                DetailItem("Ngày giao", "5/10/2023")
                DetailItem("Loại sản phẩm", "Áo thun")
                DetailItem("Chất liệu", "95% Cotton, 5% Spandex")
                DetailItem("Kích cỡ", "S, M, L, XL")
            }

            Divider()

            // Reviews Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Đánh giá",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Review items
        items(reviews.size) { index ->
            ReviewItem(review = reviews[index])
            if (index < reviews.size - 1) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

data class Review(
    val id: Int,
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String
)

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = review.userName.first().toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = review.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(rating = review.rating)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = review.date,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.comment,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun RatingBar(rating: Float, maxRating: Int = 5) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingAppPreview() {
    GDGKTheme {
        val navController = rememberNavController()
        ShoppingApp(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    GDGKTheme {
        val navController = rememberNavController()
        ProductDetailScreen(
            product = Product(
                id = 1,
                name = "DANVOUY Womens T Shirt Casual Cotton Short",
                description = "95% Cotton, 5% Spandex, Features: Casual, Short Sleeve, Letter Print, V-Neck, Fashion Tees. The fabric is soft and has some stretch.",
                price = 12.99,
                rating = 4.5f,
                imageResId = R.drawable.purple_tshirt
            ),
            navController = navController
        )
    }
}