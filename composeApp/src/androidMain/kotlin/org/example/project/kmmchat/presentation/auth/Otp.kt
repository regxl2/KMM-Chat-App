package org.example.project.kmmchat.presentation.auth

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.kmmchat.presentation.common.CircularIndicatorBox


@Composable
fun Otp(
    modifier: Modifier = Modifier,
    title: String,
    email: String,
    otpUiState: OtpUi,
    navigateBack: () -> Unit,
    onChangeOtp: (String) -> Unit,
    onSubmit: () -> Unit,
    resendOtp: () -> Unit
) {
    val otpLabel = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.DarkGray)) {
            append("Enter the OTP Sent to ")
        }
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(email)
        }
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "Cursor Animation")

    val cursorAnimation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Cursor Animation"
    )

    CircularIndicatorBox(isLoading = otpUiState.isLoading) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = otpLabel,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(64.dp))
            BasicTextField(
                value = otpUiState.otp,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center
                ),
                onValueChange = onChangeOtp,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                maxLines = 1
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(4) { index ->
                        val number =
                            if (index < otpUiState.otp.length) otpUiState.otp[index].toString() else ""
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                                .width(56.dp)
                                .height(56.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        if (index == otpUiState.otp.length) {
                                            drawLine(
                                                strokeWidth = Stroke.DefaultMiter,
                                                alpha = cursorAnimation,
                                                color = Color.Black,
                                                start = Offset(
                                                    size.width.div(2), size.height.div(9.75f)
                                                ),
                                                end = Offset(size.width.div(2), size.height.div(1.25f))
                                            )
                                        }
                                    },
                                text = number,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.displaySmall
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(2.dp))
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(color = if (number == "") Color.LightGray else MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Don't receive the OTP?", style = MaterialTheme.typography.bodyLarge)
                Text(
                    modifier = Modifier.clickable(onClick = resendOtp),
                    text = "RESEND OTP",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            otpUiState.error?.let {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(modifier = Modifier.align(Alignment.Center), text = it, color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubmit,
                enabled = !otpUiState.isLoading
            ) {
                Text(text = "Verify OTP", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(onClick = navigateBack),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    text = "Back",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOtp() {
    Otp(
        title = "OTP For Account Verification",
        email = "example@gmail.com",
        otpUiState = OtpUi("1234", false, false, null),
        navigateBack = {},
        onChangeOtp = {},
        onSubmit = {},
        resendOtp = {}
    )
}