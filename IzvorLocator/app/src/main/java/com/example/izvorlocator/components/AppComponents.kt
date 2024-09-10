package com.example.izvorlocator.components

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.izvorlocator.R
import com.example.izvorlocator.ui.theme.Accent
import com.example.izvorlocator.ui.theme.GrayColor
import com.example.izvorlocator.ui.theme.Primary
import com.example.izvorlocator.ui.theme.Secondary
import com.example.izvorlocator.ui.theme.componentShapes

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
        color = colorResource(id = R.color.colorText),
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
            color = colorResource(id = R.color.colorText),
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
        , color = colorResource(id = R.color.colorText)
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
            .background(colorResource(id = R.color.colorBackground))
            .clip(componentShapes.small)
        ,
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.colorPrimary),
            focusedLabelColor = colorResource(id = R.color.colorPrimary),
            cursorColor = colorResource(id = R.color.colorPrimary),
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
                brush = Brush.horizontalGradient(listOf(Accent, Secondary)),
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
        /*Text(
            modifier = Modifier.padding(8.dp),
            text = "ili",
            fontSize = 14.sp,
            color = TextColor)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp)
         */
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
        modifier = modifier.fillMaxSize()
            .clickable{false},
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