import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mealmates.ui.theme.MealMatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "Error"
) {
    MealMatesTheme {
        Column {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                Text(label)
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ),
                value = value,
                onValueChange = onValueChange,
                maxLines = 1,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun MultilineTextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    lines: Int,
    isError: Boolean = false,
    errorMessage: String = "Error"
) {
    MealMatesTheme {
        Column(modifier = Modifier.padding(5.dp)) {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.tertiary)
            } else {
                Text(label, color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(placeholder) },
                maxLines = lines,
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )
        }
    }
}
