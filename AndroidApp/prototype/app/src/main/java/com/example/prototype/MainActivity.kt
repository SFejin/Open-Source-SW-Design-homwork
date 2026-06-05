package com.example.smartmedication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartMedicationApp()
        }
    }
}

enum class Screen {
    Start, Login, Register, Main,
    Search, Info, Reviews, ReviewForm,
    Schedule, Notification, RecordInput, RecordList,
    Profile, MyReviews,
    AdminMain, AdminUsers, AdminReviews, AdminMedication
}

@Composable
fun SmartMedicationApp() {
    var screen by remember { mutableStateOf(Screen.Start) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(false) }

    fun goHome() {
        screen = if (isAdmin) Screen.AdminMain else Screen.Main
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF5F7F6)
        ) {
            when (screen) {
                Screen.Start -> StartScreen(
                    onLogin = { screen = Screen.Login },
                    onRegister = { screen = Screen.Register },
                    onGuest = {
                        isLoggedIn = false
                        isAdmin = false
                        screen = Screen.Main
                    },
                    onAdmin = {
                        isLoggedIn = true
                        isAdmin = true
                        screen = Screen.AdminMain
                    }
                )

                Screen.Login -> LoginScreen(
                    onLogin = {
                        isLoggedIn = true
                        isAdmin = false
                        screen = Screen.Main
                    },
                    onRegister = { screen = Screen.Register },
                    onBack = { screen = Screen.Start }
                )

                Screen.Register -> RegisterScreen(
                    onRegister = { screen = Screen.Login },
                    onBack = { screen = Screen.Start }
                )

                Screen.Main -> MainScreen(
                    isLoggedIn = isLoggedIn,
                    onSearch = { screen = Screen.Search },
                    onSchedule = { screen = Screen.Schedule },
                    onNotification = { screen = Screen.Notification },
                    onRecordList = { screen = Screen.RecordList },
                    onProfile = { screen = if (isLoggedIn) Screen.Profile else Screen.Login },
                    onMyReviews = { screen = if (isLoggedIn) Screen.MyReviews else Screen.Login },
                    onLogout = {
                        isLoggedIn = false
                        isAdmin = false
                        screen = Screen.Start
                    }
                )

                Screen.Search -> SearchScreen(onBack = ::goHome, onItemClick = { screen = Screen.Info })
                Screen.Info -> InfoScreen(
                    onBack = ::goHome,
                    onReviews = { screen = Screen.Reviews },
                    onReviewWrite = { screen = if (isLoggedIn) Screen.ReviewForm else Screen.Login }
                )
                Screen.Reviews -> ReviewsScreen(
                    onBack = ::goHome,
                    onWrite = { screen = if (isLoggedIn) Screen.ReviewForm else Screen.Login }
                )
                Screen.ReviewForm -> ReviewFormScreen(onBack = { screen = Screen.Reviews }, onSave = { screen = Screen.Reviews })

                Screen.Schedule -> ScheduleScreen(onBack = ::goHome, onSave = { screen = Screen.Notification })
                Screen.Notification -> NotificationScreen(onBack = ::goHome, onRecord = { screen = Screen.RecordInput })
                Screen.RecordInput -> RecordInputScreen(onBack = ::goHome, onSave = { screen = Screen.RecordList })
                Screen.RecordList -> RecordListScreen(onBack = ::goHome)

                Screen.Profile -> ProfileScreen(
                    onBack = ::goHome,
                    onLogout = {
                        isLoggedIn = false
                        isAdmin = false
                        screen = Screen.Start
                    }
                )
                Screen.MyReviews -> MyReviewsScreen(
                    onBack = ::goHome,
                    onEdit = { screen = Screen.ReviewForm }
                )

                Screen.AdminMain -> AdminMainScreen(
                    onUsers = { screen = Screen.AdminUsers },
                    onReviews = { screen = Screen.AdminReviews },
                    onMedication = { screen = Screen.AdminMedication },
                    onLogout = {
                        isLoggedIn = false
                        isAdmin = false
                        screen = Screen.Start
                    }
                )
                Screen.AdminUsers -> AdminListScreen(
                    title = "사용자 관리",
                    items = listOf("user01@example.com / 활성", "user02@example.com / 정지", "user03@example.com / 활성"),
                    buttons = listOf("정지", "해제"),
                    onBack = ::goHome
                )
                Screen.AdminReviews -> AdminListScreen(
                    title = "리뷰 관리",
                    items = listOf("부적절한 표현 포함 리뷰", "허위 정보 의심 리뷰", "신고된 리뷰"),
                    buttons = listOf("숨김", "삭제"),
                    onBack = ::goHome
                )
                Screen.AdminMedication -> AdminListScreen(
                    title = "약/영양제 정보 관리",
                    items = listOf("타이레놀정", "비타민 C 1000", "오메가3"),
                    buttons = listOf("수정", "삭제"),
                    extraTopButton = "새 정보 추가",
                    onBack = ::goHome
                )
            }
        }
    }
}

@Composable
fun Page(title: String, onBack: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8EEE9), contentColor = Color.Black)
                ) { Text("←") }
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))
        content()
    }
}

@Composable
fun AppCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

@Composable
fun AppButton(text: String, onClick: () -> Unit, danger: Boolean = false, secondary: Boolean = false) {
    val bg = when {
        danger -> Color(0xFFD9534F)
        secondary -> Color(0xFFE8EEE9)
        else -> Color(0xFF2E7D32)
    }
    val fg = if (secondary) Color.Black else Color.White

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bg, contentColor = fg),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AppInput(label: String) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
fun StartScreen(onLogin: () -> Unit, onRegister: () -> Unit, onGuest: () -> Unit, onAdmin: () -> Unit) {
    Page(title = "Smart Medication") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(Color(0xFF2E7D32), RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("💊", fontSize = 56.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("약과 영양제를 안전하게 관리하는 복약 관리 시스템", color = Color.Gray)
        Spacer(modifier = Modifier.height(20.dp))
        AppCard {
            AppButton("로그인", onLogin)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("회원가입", onRegister, secondary = true)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("비회원으로 시작", onGuest, secondary = true)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("관리자 화면 보기", onAdmin, secondary = true)
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit, onRegister: () -> Unit, onBack: () -> Unit) {
    Page(title = "로그인", onBack = onBack) {
        AppCard {
            AppInput("이메일")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("비밀번호")
            Spacer(modifier = Modifier.height(16.dp))
            AppButton("로그인", onLogin)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("회원가입", onRegister, secondary = true)
        }
    }
}

@Composable
fun RegisterScreen(onRegister: () -> Unit, onBack: () -> Unit) {
    Page(title = "회원가입", onBack = onBack) {
        AppCard {
            AppInput("이름")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("이메일")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("비밀번호")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("비밀번호 확인")
            Spacer(modifier = Modifier.height(16.dp))
            AppButton("회원가입", onRegister)
        }
    }
}

@Composable
fun MainScreen(
    isLoggedIn: Boolean,
    onSearch: () -> Unit,
    onSchedule: () -> Unit,
    onNotification: () -> Unit,
    onRecordList: () -> Unit,
    onProfile: () -> Unit,
    onMyReviews: () -> Unit,
    onLogout: () -> Unit
) {
    Page(title = "메인 화면") {
        Text(if (isLoggedIn) "로그인 상태" else "비회원 상태", color = Color.Gray)
        Spacer(modifier = Modifier.height(14.dp))
        MenuButton("🔍", "약/영양제 검색", onSearch)
        MenuButton("📅", "복약 일정 등록", onSchedule)
        MenuButton("🔔", "복용 알림 확인", onNotification)
        MenuButton("📋", "복용 기록 조회", onRecordList)
        MenuButton("👤", "사용자 정보 관리", onProfile)
        MenuButton("⭐", "내 리뷰 관리", onMyReviews)
        if (isLoggedIn) {
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("로그아웃", onLogout, danger = true)
        }
    }
}

@Composable
fun MenuButton(icon: String, text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SearchScreen(onBack: () -> Unit, onItemClick: () -> Unit) {
    Page(title = "약/영양제 검색", onBack = onBack) {
        AppCard {
            AppInput("예: 타이레놀, 비타민C")
            Spacer(modifier = Modifier.height(14.dp))
            listOf("타이레놀정", "비타민 C 1000", "오메가3").forEach {
                ListItemCard(title = it, subtitle = "상세 정보 보기", onClick = onItemClick)
            }
        }
    }
}

@Composable
fun InfoScreen(onBack: () -> Unit, onReviews: () -> Unit, onReviewWrite: () -> Unit) {
    Page(title = "약/영양제 정보 조회", onBack = onBack) {
        AppCard {
            Text("타이레놀정", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(14.dp))
            InfoText("효능", "두통, 발열, 근육통 완화에 사용됩니다.")
            InfoText("복용 방법", "성인 기준 1회 1정, 필요 시 복용합니다.")
            InfoText("주의사항", "간 질환이 있거나 다른 해열진통제와 병용 시 주의가 필요합니다.")
            Spacer(modifier = Modifier.height(14.dp))
            AppButton("리뷰 보기", onReviews, secondary = true)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("리뷰 작성", onReviewWrite)
        }
    }
}

@Composable
fun ReviewsScreen(onBack: () -> Unit, onWrite: () -> Unit) {
    Page(title = "리뷰 조회", onBack = onBack) {
        listOf("효과가 빨랐어요.", "복용 방법이 간단해서 좋았습니다.", "공복 복용은 피하는 게 좋을 것 같아요.").forEachIndexed { index, text ->
            ListItemCard(title = "사용자 ${index + 1} ★★★★★", subtitle = text, onClick = {})
        }
        AppButton("리뷰 작성", onWrite)
    }
}

@Composable
fun ReviewFormScreen(onBack: () -> Unit, onSave: () -> Unit) {
    Page(title = "리뷰 작성 / 수정", onBack = onBack) {
        AppCard {
            AppInput("별점")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("리뷰 내용")
            Spacer(modifier = Modifier.height(16.dp))
            AppButton("저장", onSave)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("취소", onBack, secondary = true)
        }
    }
}

@Composable
fun ScheduleScreen(onBack: () -> Unit, onSave: () -> Unit) {
    Page(title = "복약 일정 등록", onBack = onBack) {
        AppCard {
            AppInput("약/영양제 이름")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("복용 시간 예: 08:00")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("복용 주기 예: 매일")
            Spacer(modifier = Modifier.height(16.dp))
            AppButton("일정 등록", onSave)
            Spacer(modifier = Modifier.height(8.dp))
            Text("로그인 없이 사용자 기기에 저장됩니다.", color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
fun NotificationScreen(onBack: () -> Unit, onRecord: () -> Unit) {
    Page(title = "복용 알림 확인", onBack = onBack) {
        AppCard {
            Text("🔔", fontSize = 54.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("복용 시간입니다", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("타이레놀정 · 오전 8:00", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            AppButton("복용 기록 입력", onRecord)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("나중에 알림", {}, secondary = true)
        }
    }
}

@Composable
fun RecordInputScreen(onBack: () -> Unit, onSave: () -> Unit) {
    Page(title = "복용 기록 입력", onBack = onBack) {
        AppCard {
            InfoText("약 이름", "타이레놀정")
            InfoText("날짜/시간", "2026-05-02 08:00")
            Spacer(modifier = Modifier.height(14.dp))
            AppButton("복용 완료", onSave)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("미복용", onSave, secondary = true)
        }
    }
}

@Composable
fun RecordListScreen(onBack: () -> Unit) {
    Page(title = "복용 기록 조회", onBack = onBack) {
        listOf("타이레놀정 / 복용 완료", "비타민 C / 복용 완료", "오메가3 / 미복용").forEach {
            ListItemCard(title = "2026-05-02", subtitle = it, onClick = {})
        }
    }
}

@Composable
fun ProfileScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    Page(title = "사용자 정보 관리", onBack = onBack) {
        AppCard {
            AppInput("이름")
            Spacer(modifier = Modifier.height(10.dp))
            AppInput("이메일")
            Spacer(modifier = Modifier.height(16.dp))
            AppButton("수정 저장", {})
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("비밀번호 변경", {}, secondary = true)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("로그아웃", onLogout, danger = true)
        }
    }
}

@Composable
fun MyReviewsScreen(onBack: () -> Unit, onEdit: () -> Unit) {
    Page(title = "내 리뷰 관리", onBack = onBack) {
        AppCard {
            Text("타이레놀정", fontWeight = FontWeight.Bold)
            Text("효과가 빨랐어요.", color = Color.Gray)
            Spacer(modifier = Modifier.height(14.dp))
            AppButton("수정", onEdit, secondary = true)
            Spacer(modifier = Modifier.height(10.dp))
            AppButton("삭제", {}, danger = true)
        }
    }
}

@Composable
fun AdminMainScreen(onUsers: () -> Unit, onReviews: () -> Unit, onMedication: () -> Unit, onLogout: () -> Unit) {
    Page(title = "관리자 메인") {
        MenuButton("👤", "사용자 관리", onUsers)
        MenuButton("⭐", "리뷰 관리", onReviews)
        MenuButton("💊", "약/영양제 정보 관리", onMedication)
        Spacer(modifier = Modifier.height(10.dp))
        AppButton("로그아웃", onLogout, danger = true)
    }
}

@Composable
fun AdminListScreen(
    title: String,
    items: List<String>,
    buttons: List<String>,
    onBack: () -> Unit,
    extraTopButton: String? = null
) {
    Page(title = title, onBack = onBack) {
        if (extraTopButton != null) {
            AppButton(extraTopButton, {})
            Spacer(modifier = Modifier.height(12.dp))
        }
        items.forEach { item ->
            AppCard {
                Text(item, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                buttons.forEach { button ->
                    AppButton(button, {}, secondary = button != "삭제")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ListItemCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, color = Color.Gray)
        }
    }
}

@Composable
fun InfoText(label: String, text: String) {
    Text(label, fontWeight = FontWeight.Bold, color = Color.Gray)
    Text(text)
    Spacer(modifier = Modifier.height(10.dp))
}
