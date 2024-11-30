package com.raxrot.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            var height = remember { mutableStateOf("") }
            var bmi = remember { mutableStateOf("") }
            var status = remember { mutableStateOf("") }

           Column(modifier = Modifier.padding(it)) {
                EditNumberTextField(value =weight.value
                    ,label = "Weight (in kg)", keyBoardOptions =KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ) ,
                    onValueChanged ={weight.value=it} )

               EditNumberTextField(value =height.value
                   ,label = "Height (in meters)", keyBoardOptions =KeyboardOptions.Default.copy(
                       keyboardType = KeyboardType.Number,
                       imeAction = ImeAction.Done
                   ) ,
                   onValueChanged ={height.value=it} )

               Button(modifier = Modifier
                   .align(Alignment.CenterHorizontally)
                   .padding(top = 30.dp)
                   .height(60.dp)
                   .width(200.dp),
                   onClick = {
                       bmi.value = calculateBmi(weight = weight.value.toDoubleOrNull() ?: 0.0,
                           height = height.value.toDoubleOrNull() ?: 0.0)
                       status.value = getStatus(bmi.value.toDoubleOrNull() ?: 0.0)
                   }) {

                   Text(text = "Calculate", fontSize = 25.sp)
               }
               BmiResult(bmi = bmi.value, status = status.value)
           }
        }
    }
}

fun calculateBmi(weight:Double,height:Double):String{
    if (weight <= 0 || height <= 0) return "Invalid input"

    val bmi=weight/(height*height)
    return String.format("%.1f",bmi)
}

fun getStatus(bmi: Double): String {
    return when (bmi) {
        in Double.NEGATIVE_INFINITY..15.9 -> BMIStatus.SEVERE_UNDERWEIGHT
        in 16.0..18.5 -> BMIStatus.UNDERWEIGHT_DEFICIT
        in 18.5..25.0 -> BMIStatus.NORMAL_WEIGHT
        in 25.0..30.0 -> BMIStatus.OVERWEIGHT_PRE_OBESITY
        in 30.0..35.0 -> BMIStatus.OBESITY_CLASS_1
        in 35.0..40.0 -> BMIStatus.OBESITY_CLASS_2
        in 40.0..Double.POSITIVE_INFINITY -> BMIStatus.OBESITY_CLASS_3
        else -> "Invalid BMI"
    }
}
@Composable
fun EditNumberTextField(value:String,label:String,keyBoardOptions:KeyboardOptions,
                        onValueChanged:(String)->Unit) {

    TextField(value = value,
        onValueChange ={onValueChanged(it)},
        keyboardOptions = keyBoardOptions,
        label = { Text(text = label)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 25.dp) )
}

@Composable
fun BmiResult(bmi:String,status:String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text ="BMI : $bmi",
            modifier = Modifier
                .padding(vertical = 25.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold )
    }

    for (key in statusMap.keys){

        val fontWeight=if (status==key){
            FontWeight.Bold
        }else{
            FontWeight.Normal
        }

        val backgroundColor = if (status==key) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.background

        Row (modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth().background(backgroundColor)){
            Text(text = key, modifier = Modifier.weight(1f),fontWeight=fontWeight)
            Text(text = statusMap[key]!!,fontWeight=fontWeight)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun BmiCalculatorPagePreview() {
    BMICalculatorTheme {
       BmiCalculatorPage()
    }
}

object BMIStatus{
    const val SEVERE_UNDERWEIGHT="Severe underweight"
    const val UNDERWEIGHT_DEFICIT="Underweight (deficit)"
    const val NORMAL_WEIGHT="Normal weight"
    const val OVERWEIGHT_PRE_OBESITY="Overweight (pre-obesity)"
    const val  OBESITY_CLASS_1="Obesity class 1"
    const val  OBESITY_CLASS_2="Obesity class 2"
    const val  OBESITY_CLASS_3="Obesity class 3"
}

val statusMap = mapOf(
    BMIStatus.SEVERE_UNDERWEIGHT to "16.0 or less",
    BMIStatus.UNDERWEIGHT_DEFICIT to "16.0 - 18.5",
    BMIStatus.NORMAL_WEIGHT to "18.5 - 25.0",
    BMIStatus.OVERWEIGHT_PRE_OBESITY to "25.0 - 30.0",
    BMIStatus.OBESITY_CLASS_1 to "30.0 - 35.0",
    BMIStatus.OBESITY_CLASS_2 to "35.0 - 40.0",
    BMIStatus.OBESITY_CLASS_3 to "40.0 and above"
)