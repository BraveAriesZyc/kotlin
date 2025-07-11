package com.zyc.clover.pages.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage

import com.zyc.clover.R
import com.zyc.clover.components.element.components.button.FormButton
import com.zyc.clover.components.element.components.input.FormInput
import com.zyc.clover.components.element.components.input.NoBorderFormInput
import com.zyc.clover.route.LocalNavController
import com.zyc.clover.route.LoginRoute
import com.zyc.clover.route.RegisterRoute

import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen( ) {
    val navController = LocalNavController.current
    val authView = viewModel<AuthViewModel>()

    val userName by authView.userName.collectAsState()
    val password by authView.password.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "登录")
                }
            )

        },

        content = {
            Column(
                modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = 16.dp),
                content = {

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            AsyncImage(
                                model = "https://zhoudaxian.oss-rg-china-mainland.aliyuncs.com/avatar.jpg",
                                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(50.dp)),
                                contentDescription = "logo",
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            NoBorderFormInput(
                                modifier = Modifier.fillMaxWidth(),
                                value = userName,
                                placeholder = "请输入用户名",
                                onValueChange = { it ->
                                    authView.setUserName(it)
                                },
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            NoBorderFormInput(
                                modifier = Modifier.fillMaxWidth(),
                                value = password,
                                placeholder = "请输入密码",
                                onValueChange = { it ->
                                    authView.setPassword(it)
                                },
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            FormButton(
                                text = "登录",
                                onClick = {
                                    authView.loginSubmit()
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                content = {
                                    Row(content = {
                                        Text(
                                            modifier = Modifier,
                                            text = "忘记密码?",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.primary,

                                            )
                                    })
                                    Row(content = {
                                        Text(
                                            text = "没有账号？",
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            modifier = Modifier.debounceClick {
                                                navController.navigate(RegisterRoute) {
                                                    popUpTo(LoginRoute) { inclusive = true }
                                                }
                                            },
                                            text = "前往注册!",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.primary,

                                            )
                                    })
                                }
                            )
                        }
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(
                                modifier = Modifier.padding(16.dp).padding(bottom = 50.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                content = {
                                    Row {
                                        Text(
                                            text = "—— 其他方式登录 ——",
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row {
                                        Image(
                                            modifier = Modifier.size(35.dp),
                                            painter = painterResource(id = R.drawable.weixin),
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Image(
                                            modifier = Modifier.size(35.dp),
                                            painter = painterResource(id = R.drawable.qq),
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Image(
                                            modifier = Modifier.size(35.dp),
                                            painter = painterResource(id = R.drawable.weibo),
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}
