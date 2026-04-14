package com.example.android.presentation.screens.profile.components.profile_content

import android.content.Context
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.android.presentation.ui.theme.*
import java.io.File
import java.io.FileOutputStream

@Composable
fun HeaderUserSection(
    name: String,
    email: String,
    balance: Double,
    isPro: Boolean,
    avatarUri: String?,
    onUpdateClick: (String, String?) -> Unit
) {
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    var showAvatarPreview by remember { mutableStateOf(false) }

    var editedName by remember { mutableStateOf(name) }
    var tempAvatarUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(showEditDialog) {
        if (showEditDialog) {
            editedName = name
            tempAvatarUri = null
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) tempAvatarUri = uri }
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
            // --- AVATAR CHÍNH ---
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(110.dp)) {
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .clip(CircleShape)
                        // Chỉ cho phép bấm xem ảnh nếu đã có ảnh từ server
                        .clickable { if (!avatarUri.isNullOrEmpty()) showAvatarPreview = true }
                        .background(BgGray)
                        .border(3.dp, if (isPro) ProGold else Color.Transparent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUri.isNullOrEmpty()) {
                        // Hiện nền xám + Icon nếu không có ảnh
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    } else {
                        // Chỉ load AsyncImage nếu có URL
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(avatarUri)
                                .crossfade(true).build(),
                            contentDescription = "User Avatar",
                            modifier = Modifier.size(100.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
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

    // --- 1. DIALOG XEM ẢNH PHÓNG TO ---
    if (showAvatarPreview && !avatarUri.isNullOrEmpty()) {
        Dialog(onDismissRequest = { showAvatarPreview = false }) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.85f)).clickable { showAvatarPreview = false },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.9f).aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    // --- 2. DIALOG CHỈNH SỬA ---
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Chỉnh sửa thông tin", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(BgGray)
                            .border(1.dp, Color.LightGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageSource = tempAvatarUri ?: if (!avatarUri.isNullOrEmpty()) avatarUri else null

                        if (imageSource != null) {
                            AsyncImage(
                                model = imageSource,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PhotoCamera, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Thay đổi ảnh đại diện")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Tên hiển thị") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val filePath = tempAvatarUri?.let { uri ->
                            createFileFromUri(context, uri)?.absolutePath
                        }
                        onUpdateClick(editedName, filePath)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
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

private fun createFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_profile_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.use { input -> outputStream.use { output -> input.copyTo(output) } }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}