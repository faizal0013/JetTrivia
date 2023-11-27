package com.demo.jettrivia.components

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.jettrivia.models.QuestionItem
import com.demo.jettrivia.screens.QuestionViewModel
import com.demo.jettrivia.utils.AppColors


@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }

    if(viewModel.data.value.loading == true) {

        Surface(
            modifier = Modifier.size(20.dp)
        ) {
            CircularProgressIndicator()
        }

    }else {

        val question = try {
            questions?.get(questionIndex.value)
        }catch (ex: Exception){
            null
        }

        if(questions != null){
            QuestionDisplay(
                question = question!!,
                questionIndex = questionIndex,
                viewModel = viewModel,
                onNextClick = {
                    questionIndex.value = questionIndex.value + 1
                }
            )
        }

    }
}


@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextClick: (Int) -> Unit
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    val choicesState = remember(question) {
        question.choices.toMutableStateList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val currentAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (index: Int) -> Unit = remember(question) {
        {
            answerState.value = it
            currentAnswerState.value = choicesState[it] == question.answer
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.mDarkPurple) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            if(questionIndex.value >= 3){
                // Score Meter
                ShowProgress(score = questionIndex.value)
            }


            QuestionTracker(counter = questionIndex.value, outOf = viewModel.getTotalQuestionCount())

            DrawDottedLine(pathEffect = pathEffect)

            Column(
                modifier = Modifier.padding(12.dp),
            ){
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f)
                        .fillMaxWidth(),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite
                    )

                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush
                                    .linearGradient(
                                        listOf(AppColors.mOffDarkPurple, AppColors.mDarkPurple)
                                    ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomStartPercent = 50,
                                    bottomEndPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value === index),
                            onClick = { updateAnswer(index) },
                            modifier = Modifier
                                .padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                if (currentAnswerState.value === true && index === answerState.value) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                },
                            )
                        )

                        val annotatedString = buildAnnotatedString{
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (currentAnswerState.value === true && index === answerState.value) {
                                        Color.Green
                                    } else if(currentAnswerState.value === false && index === answerState.value) {
                                        Color.Red
                                    } else {
                                        AppColors.mOffWhite
                                     },
                                    fontSize = 17.sp,
                                )
                            ){
                                append(answerText)
                            }
                        }

                        Text(
                            text = annotatedString,
                            modifier = Modifier.padding(6.dp),
                        )

                    }
                }

                Button(
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        AppColors.mLightBlue
                    ),
                    onClick = {
                              onNextClick(questionIndex.value)
                    },
                    enabled = currentAnswerState.value != null
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                        )
                }
            }
        }

    }
}


@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(
                style = SpanStyle(
                    color = AppColors.mLightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp)
            ){
                append("Question $counter/")
                withStyle(style = SpanStyle(
                          color = AppColors.mLightGray,
                          fontWeight = FontWeight.Light,
                          fontSize = 14.sp)){
                        append("$outOf")
                    }
                }
            }
        },
        modifier = Modifier.padding(20.dp)
    )
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    ){
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width,  y = 0f),
            pathEffect = pathEffect
        )
    }
}

@Preview
@Composable
fun ShowProgress(score: Int = 12) {

    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075),Color(0xFFFBE6BE5)))

    val progressFactor by remember(score) {
        mutableStateOf(score * 0.005f)
    }

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(AppColors.mLightPurple, AppColors.mLightPurple)
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(
                    fraction = progressFactor
                )
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {
            Text(
                text = (score * 10).toString(),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}
