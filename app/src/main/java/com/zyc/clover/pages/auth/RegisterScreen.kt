package com.zyc.clover.pages.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.zyc.clover.R
import com.zyc.clover.components.element.components.button.FormButton
import com.zyc.clover.components.element.components.input.FormInput
import com.zyc.clover.components.element.components.input.NoBorderFormInput
import com.zyc.clover.route.LocalNavController
import com.zyc.clover.route.LoginRoute
import com.zyc.clover.route.RegisterRoute
import com.zyc.clover.ui.theme.CloverAppTheme

import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen( ) {
    val navController = LocalNavController.current
    val authView = viewModel<AuthViewModel>()

    val userName by authView.phone.collectAsState()
    val password by authView.password.collectAsState()
    val confirmPassword by authView.confirmPassword.collectAsState()



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "注册")
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
                        content = {
                            NoBorderFormInput(
                                modifier = Modifier.fillMaxWidth(),
                                value = userName,
                                placeholder = "请输入手机号",
                                onValueChange = { it ->
                                    authView.setPhone(it)
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
                            Spacer(modifier = Modifier.height(16.dp))

                            NoBorderFormInput(
                                modifier = Modifier.fillMaxWidth(),
                                value = confirmPassword,
                                placeholder = "确认密码",
                                onValueChange = { it ->
                                    authView.setConfirmPassword(it)
                                },
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            FormButton(
                                text = "注册",
                                onClick = {
                                    authView.registerSubmit()
                                }
                            )
                            Row(
                                content = {
                                    Text(
                                        text = "已有账号？",
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        modifier = Modifier
                                            .debounceClick {
                                                navController.navigate(LoginRoute){
                                                    popUpTo(RegisterRoute) { inclusive = true }
                                                }
                                            },
                                        text = "前往登录!",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
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
