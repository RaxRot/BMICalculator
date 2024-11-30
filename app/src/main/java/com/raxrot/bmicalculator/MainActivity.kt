package com.raxrot.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raxrot.bmicalculator.ui.theme.BMICalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                // A surface container using the 'background' color from the theme
                    BmiCalculatorPage()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiCalculatorPage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Bmi Calculator") }
                )
            }
        ) {
            var weight = remember { mutableStateOf("") }

           Column(modifier = Modifier.padding(it)) {
                EditNumberTextField(value =weight.value
                    , label = "Weight (in kg)", keyBoardOptions =KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ) ,
                    onValueChanged ={weight.value=it} )

           }
        }
    }
}

@Composable
fun EditNumberTextField(value:String,label:String,keyBoardOptions:KeyboardOptions,
                        onValueChanged:(String)->Unit) {

    TextField(value = value,
        onValueChange ={onValueChanged},
        keyboardOptions = keyBoardOptions,
        label = { Text(text = label)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp) )
}



@Preview(showBackground = true)
@Composable
fun BmiCalculatorPagePreview() {
    BMICalculatorTheme {
       BmiCalculatorPage()
    }
}
