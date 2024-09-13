package com.example.izvorlocator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.izvorlocator.R
import com.example.izvorlocator.ui.theme.Background
import com.example.izvorlocator.ui.theme.Primary
import com.example.izvorlocator.ui.theme.componentShapes


@Composable
fun RadioButtonComponent(
    labelValue: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(Background)
            .clip(componentShapes.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.height(40.dp),
            selected = isSelected,
            onClick = onOptionSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(id = R.color.colorPrimary),
                unselectedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = labelValue,
            color = Color.Black,
            modifier = Modifier.wrapContentWidth()
        )
    }
}
@Composable
fun RadioButtonGroup(
    label: String,
    options: List<String>,
    selectedOptionIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .clip(componentShapes.small)
            .padding(6.dp)
    ) {
        Text(
            text = label,
            color = Primary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Background)
                .clip(componentShapes.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEachIndexed { index, option ->
                RadioButtonComponent(
                    labelValue = option,
                    isSelected = index == selectedOptionIndex,
                    onOptionSelected = { onOptionSelected(index) }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}