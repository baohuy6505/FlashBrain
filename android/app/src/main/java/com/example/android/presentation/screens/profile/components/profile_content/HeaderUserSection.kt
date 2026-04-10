package com.example.android.presentation.screens.profile.components.profile_content

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.android.R
import com.example.android.presentation.ui.theme.*

@Composable
fun HeaderUserSection(
    name: String,
    email: String,
    balance: Double,
    isPro: Boolean,
    avatarUri: Uri?,
    onNameChange: (String) -> Unit,
    onAvatarChange: (Uri?) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showAvatarPreview by remember { mutableStateOf(false) }

    // --- CÁC BIẾN TẠM ĐỂ GIỮ TRẠNG THÁI TRONG DIALOG ---
    var editedName by remember { mutableStateOf(name) }
    var tempAvatarUri by remember { mutableStateOf(avatarUri) }

    // Cập nhật lại biến tạm mỗi khi Dialog mở ra để đồng bộ với dữ liệu gốc
    LaunchedEffect(showEditDialog) {
        if (showEditDialog) {
            editedName = name
            tempAvatarUri = avatarUri
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            // Chỉ cập nhật vào biến tạm, KHÔNG gọi onAvatarChange ở đây
            if (uri != null) tempAvatarUri = uri
        }
    )

    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- BOX AVATAR ---
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.size(110.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .clip(CircleShape)
                        .clickable { showAvatarPreview = true }
                        .border(3.dp, if (isPro) ProGold else Color.Transparent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatarUri ?: R.drawable.avt)
                            .crossfade(true)
                            .build(),
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(100.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.avt),
                        error = painterResource(id = R.drawable.avt)
                    )
                }

                if (isPro) {
                    Surface(
                        color = ProGold, shape = CircleShape, shadowElevation = 4.dp,
                        modifier = Modifier.offset(x = 4.dp, y = 2.dp)
                    ) {
                        Text(
                            text = "PRO", color = Color.White, fontSize = 12.sp,
                            fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- PHẦN TÊN & NÚT SỬA ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp)
            ) {
                Text(text = name, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextBlack)
                IconButton(onClick = { showEditDialog = true }) {
                    Icon(Icons.Default.Edit, "Edit", tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                }
            }

            Text(text = email, fontSize = 14.sp, color = TextGray)

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = BgGray, thickness = 2.dp)
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "TOTAL BALANCE", fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.SemiBold)
            Text(text = "$$balance", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
        }
    }

    // --- 1. DIALOG XEM ẢNH TO ---
    if (showAvatarPreview) {
        Dialog(
            onDismissRequest = { showAvatarPreview = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { showAvatarPreview = false },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = avatarUri ?: R.drawable.avt,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.9f).aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    // --- 2. DIALOG SỬA THÔNG TIN (Tên & Ảnh) ---
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Chỉnh sửa thông tin", fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Hiển thị ảnh đang chọn thử (Preview ngay trong Dialog)
                    AsyncImage(
                        model = tempAvatarUri ?: R.drawable.avt,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp).clip(CircleShape).border(1.dp, BgGray, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PhotoCamera, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Thay đổi ảnh đại diện")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Tên hiển thị") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    // CHỈ CẬP NHẬT RA NGOÀI KHI NHẤN LƯU
                    onNameChange(editedName)
                    onAvatarChange(tempAvatarUri)
                    showEditDialog = false
                }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text("Lưu thay đổi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Hủy", color = TextGray)
                }
            }
        )
    }
}