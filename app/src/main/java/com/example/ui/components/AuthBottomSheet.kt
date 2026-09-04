package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InsaneRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthBottomSheet(
    isOpen: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSignIn: (email: String, pass: String) -> Unit,
    onSignUp: (name: String, handle: String, email: String, pass: String) -> Unit,
    onClearError: () -> Unit
) {
    if (!isOpen) return

    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusManager = LocalFocusManager.current

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Sign In, 1: Create Account

    // Sign In form fields
    var signInEmail by remember { mutableStateOf("") }
    var signInPassword by remember { mutableStateOf("") }
    var signInPasswordVisible by remember { mutableStateOf(false) }

    // Sign Up form fields
    var signUpName by remember { mutableStateOf("") }
    var signUpHandle by remember { mutableStateOf("") }
    var signUpEmail by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var signUpConfirmPassword by remember { mutableStateOf("") }
    var signUpPasswordVisible by remember { mutableStateOf(false) }
    var localValidationError by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("auth_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Branding Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp, 24.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(InsaneRed, Color(0xFFCC0000))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "InsaneTube Play",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INSANETUBE",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = if (selectedTab == 0) "Sign in to INSANETUBE" else "Create your INSANETUBE channel",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (selectedTab == 0)
                    "Access your subscriptions, liked videos, watch history, and playlists"
                else
                    "Publish videos, leave comments, and build your subscriber community",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Tab Selector (Sign In vs Create Account)
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                divider = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        onClearError()
                        localValidationError = null
                    },
                    text = {
                        Text(
                            text = "Sign In",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onClearError()
                        localValidationError = null
                    },
                    text = {
                        Text(
                            text = "Create Account",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                )
            }

            // Error Banner (from server or local validation)
            val activeError = errorMessage ?: localValidationError
            if (activeError != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeError,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (selectedTab == 0) {
                // SIGN IN TAB (MANUAL CREDENTIALS ENTRY)
                Text(
                    text = "Enter your Gmail or handle and password to sign in.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                // Email / Handle input
                OutlinedTextField(
                    value = signInEmail,
                    onValueChange = {
                        signInEmail = it
                        localValidationError = null
                        onClearError()
                    },
                    label = { Text("Gmail or @handle") },
                    placeholder = { Text("insaneprinceyt@gmail.com") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signin_email_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Password input
                OutlinedTextField(
                    value = signInPassword,
                    onValueChange = {
                        signInPassword = it
                        localValidationError = null
                        onClearError()
                    },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { signInPasswordVisible = !signInPasswordVisible }) {
                            Icon(
                                imageVector = if (signInPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (signInPasswordVisible) "Hide password" else "Show password",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    visualTransformation = if (signInPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (signInEmail.isBlank()) {
                                localValidationError = "Please enter your Gmail or handle"
                            } else if (signInPassword.isBlank()) {
                                localValidationError = "Please enter your password"
                            } else {
                                localValidationError = null
                                onSignIn(signInEmail.trim(), signInPassword.trim())
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signin_password_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Sign In Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (signInEmail.isBlank()) {
                            localValidationError = "Please enter your Gmail or handle"
                        } else if (signInPassword.isBlank()) {
                            localValidationError = "Please enter your password"
                        } else {
                            localValidationError = null
                            onSignIn(signInEmail.trim(), signInPassword.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("signin_submit_button")
                ) {
                    Text(
                        text = "Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

            } else {
                // CREATE ACCOUNT (SIGN UP) TAB
                // Full Name
                OutlinedTextField(
                    value = signUpName,
                    onValueChange = {
                        signUpName = it
                        localValidationError = null
                    },
                    label = { Text("Channel or Full Name") },
                    placeholder = { Text("e.g. Maya Tech") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = FocusDirection.Down.let { ImeAction.Next }),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Handle (@username)
                OutlinedTextField(
                    value = signUpHandle,
                    onValueChange = {
                        signUpHandle = if (it.startsWith("@")) it else "@$it"
                        localValidationError = null
                    },
                    label = { Text("Handle (@username)") },
                    placeholder = { Text("@mayatech") },
                    leadingIcon = {
                        Icon(Icons.Default.AlternateEmail, contentDescription = null)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_handle_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Email
                OutlinedTextField(
                    value = signUpEmail,
                    onValueChange = {
                        signUpEmail = it
                        localValidationError = null
                    },
                    label = { Text("Email address") },
                    placeholder = { Text("maya@example.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_email_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Password
                OutlinedTextField(
                    value = signUpPassword,
                    onValueChange = {
                        signUpPassword = it
                        localValidationError = null
                    },
                    label = { Text("Password (min 4 chars)") },
                    placeholder = { Text("••••••••") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { signUpPasswordVisible = !signUpPasswordVisible }) {
                            Icon(
                                imageVector = if (signUpPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    visualTransformation = if (signUpPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_password_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Confirm Password
                OutlinedTextField(
                    value = signUpConfirmPassword,
                    onValueChange = {
                        signUpConfirmPassword = it
                        localValidationError = null
                    },
                    label = { Text("Confirm password") },
                    placeholder = { Text("••••••••") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    visualTransformation = if (signUpPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_confirm_password_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Sign Up
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        when {
                            signUpName.isBlank() -> localValidationError = "Please enter your name"
                            signUpHandle.isBlank() || signUpHandle == "@" -> localValidationError = "Please enter a channel handle"
                            signUpEmail.isBlank() || !signUpEmail.contains("@") -> localValidationError = "Please enter a valid email address"
                            signUpPassword.length < 4 -> localValidationError = "Password must be at least 4 characters"
                            signUpPassword != signUpConfirmPassword -> localValidationError = "Passwords do not match"
                            else -> {
                                localValidationError = null
                                onSignUp(signUpName, signUpHandle, signUpEmail, signUpPassword)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InsaneRed),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("signup_submit_button")
                ) {
                    Text(
                        text = "Create Account & Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Terms info
            Text(
                text = "By continuing, you agree to INSANETUBE's Terms of Service and Privacy Policy.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DemoAccountPill(
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = modifier.height(38.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
