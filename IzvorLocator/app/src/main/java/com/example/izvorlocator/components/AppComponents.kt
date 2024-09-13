package com.example.izvorlocator.components

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.*
import coil.compose.rememberImagePainter
import com.example.izvorlocator.R
import com.example.izvorlocator.ui.theme.*

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontStyle = FontStyle.Normal
        ),
        color = TextColor,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ProfileInfoRow(icon: ImageVector, description: String, fontSize: TextUnit = 18.sp) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = description,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            ),
            color = TextColor,
            textAlign = TextAlign.Start
        )
    }
}
@Composable
fun HeadingTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
        , color = TextColor
        , textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    labelValue: String,
    imageVector: ImageVector,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
)
{
    val textValue = remember{ mutableStateOf("") }

    OutlinedTextField(
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .clip(componentShapes.small)
        ,
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "", modifier = Modifier.size(24.dp))
        },
        isError = !errorStatus
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField2Component(
    labelValue: String,
    onTextChanged: (String) -> Unit,
    isError: Boolean,
    value: String
)
{
    OutlinedTextField(
        value = value,
        onValueChange = {
            onTextChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .clip(componentShapes.small)
        ,
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        isError = isError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    imageVector: ImageVector,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
){
    val password = remember{ mutableStateOf("") }
    val passwordVisible = remember{ mutableStateOf(false) }

    OutlinedTextField(
        value = password.value,
        onValueChange = {
            password.value = it
            onTextChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.colorBackground))
            .clip(componentShapes.small)
        ,
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.colorPrimary),
            focusedLabelColor = colorResource(id = R.color.colorPrimary),
            cursorColor = colorResource(id = R.color.colorPrimary),
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        singleLine = true,
        maxLines = 1,
        leadingIcon = {
            Icon(imageVector = imageVector,
                contentDescription = "",
                modifier = Modifier.size(26.dp))
        },
        trailingIcon = {
            val iconImage = if(passwordVisible.value){
                Icon(painter = painterResource(id = R.drawable.visibility),
                    contentDescription = "Show password",
                    modifier = Modifier.size(26.dp))
            }
            else{
                Icon(painter = painterResource(id = R.drawable.visibility_off),
                    contentDescription = "Hide password",
                    modifier = Modifier.size(26.dp))
            }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                iconImage
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus
    )
}


@Composable
fun ButtonComponent(
    value: String,
    onButtonClicked: () -> Unit,
    isEnabled: Boolean = false
){
    var list = listOf(Accent, Secondary)
    if(value == stringResource(R.string.delete)){
        list = listOf(Color.Red,Color(0xFFFF6F6F))
    }

    Button(onClick = {
        onButtonClicked.invoke()
    },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        enabled = isEnabled
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                brush = Brush.horizontalGradient(list),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun SizedButtonComponent(
    value: String,
    onButtonClicked: () -> Unit,
    width: Dp
){
    var list = listOf(Accent, Secondary)
    if(value == stringResource(R.string.obrisi_marker)){
        list = listOf(Color.Red,Color(0xFFFF6F6F))
    }
    Button(onClick = {
        onButtonClicked.invoke()
    },
        modifier = Modifier
            .heightIn(48.dp)
            .widthIn(width),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
    ) {
        Box(modifier = Modifier
            .widthIn(width)
            .heightIn(48.dp)
            .background(
                brush = Brush.horizontalGradient(list),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DividerTextComponent(){
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp)
    }
}

@Composable
fun ClickableLoginTextComponent(question: String, text: String, onTextSelected: (String) -> Unit){
    val annotatedString = buildAnnotatedString {
        append(question)
        withStyle(style = SpanStyle(color = Primary)){
            pushStringAnnotation(tag = text, annotation = text)
            append(text)
        }
    }
    ClickableText(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(30.dp),
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if (span.item == text) {
                    onTextSelected(span.item)
                }
            }
    })
}
@Composable
fun UnderlinedTextComponent(value: String, onUnderlinedTextClicked: () -> Unit){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clickable(
                onClick = {
                    onUnderlinedTextClicked.invoke()
                }),
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        )
        , color = colorResource(id = R.color.colorGray)
        , textAlign = TextAlign.Center
        , textDecoration = TextDecoration.Underline
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(toolbarTitle: String, burgerButtonClicked : ()->Unit, logoutButtonClicked : ()->Unit){
    TopAppBar(
        title = {
            Text(text = toolbarTitle, color = Color.White)
        },
        navigationIcon = {
            IconButton(onClick = {
                burgerButtonClicked.invoke()
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.padding(start = 10.dp))
            }
        },
        actions = {
            IconButton(onClick = {
                logoutButtonClicked.invoke()
            }) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White, modifier = Modifier.padding(end = 10.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Secondary  // Pozadinska boja
        ))
}

@Composable
fun CustomIndeterminateProgress(modifier: Modifier)
{
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { false },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.size(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.size(48.dp),
                    color = Secondary
                )
            }
        }
    }
}

@Composable
fun ImageSwitcher(imageUris: List<Uri>) {
    var currentIndex by remember { mutableStateOf(0) }
    if (currentIndex >= imageUris.size) {
        currentIndex = 0
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
            IconButton(
                onClick = { if (currentIndex > 0) currentIndex-- },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(26.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = rememberImagePainter(
                    data = imageUris.getOrNull(currentIndex) ?: R.drawable.blank
                ),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RectangleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { if (currentIndex < imageUris.size - 1) currentIndex++ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "",
                    modifier = Modifier.size(26.dp),
                    tint = Color.Black
                )
            }
    }
}