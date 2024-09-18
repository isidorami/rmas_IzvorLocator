package com.example.izvorlocator.components

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.izvorlocator.ui.theme.Primary
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    label: String = "Unesi datum",
    initialDate: Long? = null,
    onDateSelected: (Long) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    val context = LocalContext.current

    // Format date to display in the TextField
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val dateText = selectedDate?.let { dateFormat.format(Date(it)) } ?: ""

    // Create and show the date picker dialog
    val datePickerDialog = rememberDatePickerDialog(context) { date ->
        selectedDate = date
        onDateSelected(date)
    }

    // TextField za prikaz i izbor datuma
    Column {
        TextField(
            modifier = Modifier
                .width(150.dp)
                .clickable { datePickerDialog.show() }, // Show the date picker dialog
            value = dateText, // Display formatted date
            onValueChange = {},
            label = { Text(label) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Primary,
                focusedLabelColor = Primary,
                unfocusedLabelColor = Color.Gray
            ),
            enabled = false, // Disable manual input
            maxLines = 1
        )
    }
}

@Composable
fun rememberDatePickerDialog(
    context: Context,
    onDateSelected: (Long) -> Unit
): DatePickerDialog {
    val calendar = Calendar.getInstance()
    return DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            // Return the selected date as a timestamp in milliseconds
            val selectedDateInMillis = selectedCalendar.timeInMillis
            onDateSelected(selectedDateInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}