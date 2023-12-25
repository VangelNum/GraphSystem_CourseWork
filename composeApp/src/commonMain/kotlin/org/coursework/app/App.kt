@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package org.coursework.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.indexOfFirstPressed
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.coursework.app.feature_core.data.ColorWithName
import org.coursework.app.feature_current_figure.data.CurrentFigure
import org.coursework.app.feature_core.data.DrawableItem
import org.coursework.app.feature_cubic_spline.operations.cubicSplinePathCalculate
import org.coursework.app.feature_cubic_spline.presentation.drawPathForCubicSpline
import org.coursework.app.feature_cubic_spline.presentation.drawTempLinesAndPointForCubicSpline
import org.coursework.app.feature_default_primitive.data.PrimitiveTypeWithName
import org.coursework.app.feature_default_primitive.data.TypeOfPrimitive
import org.coursework.app.feature_default_primitive.operations.calculateFlagPoints
import org.coursework.app.feature_default_primitive.operations.calculateRectangularTrianglePoints
import org.coursework.app.feature_operations.data.OperationType
import org.coursework.app.feature_operations.data.OperationWithName
import org.coursework.app.feature_selected_figure.operations.calculateBoundingBox
import org.coursework.app.feature_selected_figure.presentation.drawFigureNumberForChosenFigure
import org.coursework.app.feature_operations.presentation.fillPrimitive
import org.coursework.app.feature_operations.operations.getCenterOfFigure
import org.coursework.app.feature_core.getColorList
import org.coursework.app.feature_operations.operations.getOperationList
import org.coursework.app.feature_default_primitive.operations.getPrimitiveList
import org.coursework.app.feature_selected_figure.operations.getSelectedFigure
import org.coursework.app.feature_tmo.operations.getTmoList
import org.coursework.app.feature_operations.operations.mirrorPointsSelectedCenter
import org.coursework.app.feature_operations.operations.mirrorPointsVertical
import org.coursework.app.feature_operations.operations.rotateFigure
import org.coursework.app.feature_operations.presentation.drawFigureNumbers
import org.coursework.app.feature_operations.presentation.drawHelperForVerticalMirror
import org.coursework.app.feature_current_figure.presentation.drawPathAndPointsForCurrentFigure
import org.coursework.app.feature_tmo.data.TmoWithName
import org.coursework.app.feature_tmo.operations.calculateTmo


val widthOfButtons = 250.dp
val heightOfButtons = 48.dp
val textStyle = TextStyle(
    color = Color.Black,
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal
)
val topPadding = 24.dp

@Composable
internal fun App() {
    val operationList = getOperationList()
    val colorList = getColorList()
    val primitiveList = getPrimitiveList()
    val tmoList = getTmoList()
    var selectedOperation by remember {
        mutableStateOf(operationList[0])
    }
    var expandedOperation by remember {
        mutableStateOf(false)
    }
    var selectedColor by remember {
        mutableStateOf(colorList[0])
    }
    var expandedColor by remember {
        mutableStateOf(false)
    }

    var selectedPrimitive by remember {
        mutableStateOf(primitiveList[0])
    }
    var expandedPrimitive by remember {
        mutableStateOf(false)
    }
    val currentFigure by remember {
        mutableStateOf(
            CurrentFigure(
                offsetList = mutableStateListOf(),
                isCubicSpline = false,
                tempListForLinesCubicSpline = mutableStateListOf()
            )
        )
    }
    var drawableItems = mutableStateListOf<DrawableItem>()

    var rotationAngle by remember { mutableStateOf("90") }

    var position by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    val pointAlpha = remember { Animatable(initialValue = 1f) }
    val scope = rememberCoroutineScope()
    var selectedFigure by remember {
        mutableStateOf<DrawableItem?>(null)
    }
    val textMeasurer = rememberTextMeasurer()
    var showNumberOfFigures by remember {
        mutableStateOf(false)
    }
    var showCheckBoxForVerticalLine by remember {
        mutableStateOf(false)
    }
    var expandedFigureA by remember {
        mutableStateOf(false)
    }
    var selectedFigureAIndex by remember {
        mutableStateOf<Int?>(null)
    }
    var expandedFigureB by remember {
        mutableStateOf(false)
    }
    var selectedFigureBIndex by remember {
        mutableStateOf<Int?>(null)
    }
    var selectedTmo by remember {
        mutableStateOf(tmoList[0])
    }
    var expandedTmo by remember {
        mutableStateOf(false)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(180.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize().padding(8.dp).horizontalScroll(
                        rememberScrollState()
                    )
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ChooseOperationSection(
                            expandedOperation = expandedOperation,
                            onExpandedOperationChange = {
                                expandedOperation = it
                            },
                            selectedOperation = selectedOperation,
                            onSelectedOperationChange = {
                                selectedOperation = it
                            },
                            onClickWorkWithObject = {
                                position = Offset(0f, 0f)
                            },
                            operationList = operationList,
                            onClickDrawCubicSpline = {
                                currentFigure.isCubicSpline = true
                            },
                            onClickAnotherOperation = {
                                currentFigure.isCubicSpline = false
                                currentFigure.tempListForLinesCubicSpline.clear()
                            }
                        )
                        Column {
                            AnimatedVisibility(selectedOperation.operation != OperationType.WorkWithObject) {
                                ChooseColorSection(
                                    expandedColor = expandedColor,
                                    onExpandedColorChange = {
                                        expandedColor = it
                                    },
                                    selectedColor = selectedColor,
                                    onColorSelectedChange = {
                                        selectedColor = it
                                    },
                                    colorList = colorList
                                )
                            }
                            AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject && selectedFigure != null) {
                                ChooseRotationSection(
                                    rotationAngle = rotationAngle,
                                    onRotationAngleChange = {
                                        rotationAngle = it
                                    },
                                    selectedFigure = selectedFigure,
                                    position = position
                                )
                            }
                        }
                    }
                    AnimatedVisibility(selectedOperation.operation == OperationType.PastPrimitive) {
                        ChoosePrimitiveSection(
                            expandedPrimitive = expandedPrimitive,
                            onExpandedPrimitiveChange = {
                                expandedPrimitive = it
                            },
                            selectedPrimitive = selectedPrimitive,
                            onSelectedPrimitiveChange = {
                                selectedPrimitive = it
                            },
                            primitiveList = primitiveList
                        )
                    }
                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject && selectedFigure != null) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MirrorCenterFigureSection(
                                selectedFigure
                            )
                            MirrorVerticalFigureSection(
                                selectedFigure,
                                position,
                                showCheckBoxForVerticalLine,
                                onShowCheckBoxChange = {
                                    showCheckBoxForVerticalLine = !it
                                }
                            )
                        }
                    }
                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ChooseFigureASection(
                                expandedFigureA = expandedFigureA,
                                onExpandedFigureA = {
                                    expandedFigureA = it
                                },
                                selectedFigureAIndex = selectedFigureAIndex,
                                onSelectedFigureAIndex = {
                                    selectedFigureAIndex = it
                                },
                                drawableItems = drawableItems
                            )
                            ChooseTmoSection(
                                expandedTmo,
                                onExpandedTmoChange = {
                                    expandedTmo = it
                                },
                                selectedTmo = selectedTmo,
                                onSelectedTmoChange = {
                                    selectedTmo = it
                                },
                                tmoList = tmoList
                            )
                        }
                    }

                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ChooseFigureBSection(
                                expandedFigureB = expandedFigureB,
                                onExpandedFigureB = {
                                    expandedFigureB = it
                                },
                                selectedFigureBIndex = selectedFigureBIndex,
                                onSelectedFigureBIndex = {
                                    selectedFigureBIndex = it
                                },
                                drawableItems = drawableItems
                            )
                            ApplyTmoSection(
                                selectedFigureAIndex = selectedFigureAIndex,
                                selectedFigureBIndex = selectedFigureBIndex,
                                drawableItems = drawableItems,
                                onDrawableItemsChange = {
                                    drawableItems = it
                                },
                                selectedTmo = selectedTmo,
                                scope = scope,
                                snackbarHostState = snackbarHostState,
                                onSelectedFigureAIndexChange = {
                                    selectedFigureAIndex = null
                                },
                                onSelectedFigureBIndexChange = {
                                    selectedFigureBIndex = null
                                }
                            )

                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject && selectedFigure != null) {
                        ChoosenFigureSection(
                            selectedFigure = selectedFigure,
                            drawableItems = drawableItems,
                            showNumberOfFigures = showNumberOfFigures,
                            textMeasurer = textMeasurer,
                            onIconClick = {
                                drawableItems.remove(selectedFigure)
                                selectedFigure = null
                            }
                        )
                    }

                    Column(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        ShowNumberOfFigureSection(
                            showNumberOfFigures = showNumberOfFigures,
                            onClick = {
                                showNumberOfFigures = !showNumberOfFigures
                            }
                        )

                        ClearPictureBoxSection(
                            onClick = {
                                drawableItems.clear()
                                currentFigure.offsetList.clear()
                                currentFigure.tempListForLinesCubicSpline.clear()
                                selectedFigureAIndex = null
                                selectedFigureBIndex = null
                                selectedFigure = null
                            },
                        )

                    }
                }
            }
        }
    ) { paddingValues ->
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            if (selectedOperation.operation == OperationType.DrawPrimitive) {
                                if (currentFigure.offsetList.isNotEmpty() && currentFigure.offsetList.size > 1) {
                                    val drawableItem = DrawableItem(
                                        currentFigure.offsetList.toMutableList(),
                                        selectedColor.color
                                    )
                                    drawableItems.add(drawableItem)
                                    currentFigure.offsetList.clear()
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press) {
                                position = event.changes.first().position
                                if (!showCheckBoxForVerticalLine) {
                                    selectedFigure = getSelectedFigure(position, drawableItems)
                                }
                                when (selectedOperation.operation) {
                                    OperationType.DrawPrimitive -> {
                                        if (event.buttons.isPrimaryPressed || event.buttons.indexOfFirstPressed() == -1) {
                                            currentFigure.offsetList.add(position)
                                        }
                                        if (event.buttons.isSecondaryPressed) {
                                            if (currentFigure.offsetList.isNotEmpty() && currentFigure.offsetList.size > 1) {
                                                val drawableItem = DrawableItem(
                                                    currentFigure.offsetList.toMutableList(),
                                                    selectedColor.color
                                                )
                                                drawableItems.add(drawableItem)
                                                currentFigure.offsetList.clear()
                                            }
                                        }
                                    }

                                    OperationType.PastPrimitive -> {
                                        if (event.buttons.isPrimaryPressed || event.buttons.indexOfFirstPressed() == -1) {
                                            when (selectedPrimitive.type) {
                                                TypeOfPrimitive.FLAG -> {
                                                    val flag = calculateFlagPoints(position.x, position.y)
                                                    drawableItems.add(
                                                        DrawableItem(
                                                            flag,
                                                            selectedColor.color
                                                        )
                                                    )
                                                }

                                                TypeOfPrimitive.RECTANGULARTRIANGLE -> {
                                                    val triangle = calculateRectangularTrianglePoints(position.x, position.y)
                                                    drawableItems.add(
                                                        DrawableItem(
                                                            triangle,
                                                            selectedColor.color
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    OperationType.DrawCubeSpline -> {
                                        if (event.buttons.isPrimaryPressed || event.buttons.indexOfFirstPressed() == -1) {
                                            if (currentFigure.tempListForLinesCubicSpline.size == 4) {
                                                currentFigure.tempListForLinesCubicSpline.clear()
                                            }
                                            currentFigure.tempListForLinesCubicSpline.add(
                                                position
                                            )
                                            if (currentFigure.tempListForLinesCubicSpline.size == 4) {
                                                val splinePoints = cubicSplinePathCalculate(currentFigure.tempListForLinesCubicSpline)
                                                drawableItems.add(
                                                    DrawableItem(
                                                        splinePoints,
                                                        selectedColor.color,
                                                        isCubicSpline = true
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    OperationType.WorkWithObject -> {
                                        scope.launch {
                                            pointAlpha.snapTo(1f)
                                            pointAlpha.animateTo(
                                                0f,
                                                animationSpec = tween(durationMillis = 2000)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (selectedOperation.operation == OperationType.WorkWithObject && !showCheckBoxForVerticalLine) {
                            position = change.position
                            if (selectedFigure == null) {
                                selectedFigure = getSelectedFigure(position, drawableItems)
                            }
                            selectedFigure?.let { draggedFigure ->
                                // Обновление позиции фигуры на основе dragAmount
                                draggedFigure.offsetList = draggedFigure.offsetList.map { it + dragAmount }.toMutableList()

                                selectedFigure = draggedFigure
                            }
                        }
                    }
                }

        ) {
            for (drawableItem in drawableItems) {
                if (!drawableItem.isCubicSpline) {
                    fillPrimitive(drawableItem)
                } else {
                    drawPathForCubicSpline(drawableItem)
                }
            }
            if (currentFigure.isCubicSpline && currentFigure.tempListForLinesCubicSpline.isNotEmpty()) {
                drawTempLinesAndPointForCubicSpline(
                    currentFigure = currentFigure
                )
            }
            if (!currentFigure.isCubicSpline && currentFigure.offsetList.isNotEmpty()) {
                drawPathAndPointsForCurrentFigure(
                    currentFigure,
                    selectedColor.color
                )
            }

            if (selectedOperation.operation == OperationType.WorkWithObject) {
                drawHelperForVerticalMirror(
                    position = position,
                    pointAlpha = pointAlpha.value,
                    selectedFigure = selectedFigure,
                    showCheckBoxForVerticalLine = showCheckBoxForVerticalLine
                )
            }
            if (showNumberOfFigures) {
                drawFigureNumbers(drawableItems, textMeasurer)
            }
        }
    }
}

@Composable
fun ClearPictureBoxSection(onClick: () -> Unit) {
    Box(modifier = Modifier.padding(top = topPadding).padding(top = 8.dp)) {
        ElevatedButton(
            modifier = Modifier.width(widthOfButtons).height(heightOfButtons),
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Очистить поле", style = textStyle)
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ShowNumberOfFigureSection(
    showNumberOfFigures: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.padding(top = topPadding)) {
        ElevatedButton(
            modifier = Modifier.width(widthOfButtons).height(heightOfButtons),
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Отобразить номера объектов", style = textStyle)
                }
                Icon(
                    imageVector = if (showNumberOfFigures) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ChoosenFigureSection(
    selectedFigure: DrawableItem?,
    drawableItems: SnapshotStateList<DrawableItem>,
    showNumberOfFigures: Boolean,
    textMeasurer: TextMeasurer,
    onIconClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Выбранная фигура")
        Box(
            modifier = Modifier.size(120.dp)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(5))
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize().padding(10.dp)
            ) {
                selectedFigure?.let { drawableItem ->
                    val boundingBox = calculateBoundingBox(drawableItem.offsetList)

                    val scaleX = size.width / boundingBox.width
                    val scaleY = size.height / boundingBox.height

                    val translateX = -boundingBox.left * scaleX
                    val translateY = -boundingBox.top * scaleY

                    val transformedPoints = drawableItem.offsetList.map {
                        Offset(
                            it.x * scaleX + translateX,
                            it.y * scaleY + translateY
                        )
                    }.toMutableList()

                    val selectedDrawable = DrawableItem(
                        transformedPoints,
                        drawableItem.color
                    )
                    if (drawableItem.tmoWasMake == true) {
                        transformedPoints.chunked(2) { (xl, xr) ->
                            drawLine(
                                color = drawableItem.color,
                                strokeWidth = 2f,
                                start = xl,
                                end = xr
                            )
                        }
                    } else if (drawableItem.isCubicSpline) {
                        drawPathForCubicSpline(selectedDrawable)
                    } else {
                        fillPrimitive(
                            selectedDrawable
                        )
                    }


                    if (showNumberOfFigures) {
                        drawFigureNumberForChosenFigure(
                            selectedFigure,
                            textMeasurer,
                            drawableItems
                        )
                    }
                }
            }
            IconButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = onIconClick
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = Color.Blue
                )
            }
        }
    }
}

@Composable
fun ApplyTmoSection(
    selectedFigureAIndex: Int?,
    selectedFigureBIndex: Int?,
    drawableItems: SnapshotStateList<DrawableItem>,
    onDrawableItemsChange: (SnapshotStateList<DrawableItem>) -> Unit,
    selectedTmo: TmoWithName,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onSelectedFigureAIndexChange: (Unit?) -> Unit,
    onSelectedFigureBIndexChange: (Unit?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.padding(top = topPadding)) {
            ElevatedButton(
                modifier = Modifier.width(widthOfButtons)
                    .height(heightOfButtons),
                onClick = {
                    if (selectedFigureAIndex == null || selectedFigureBIndex == null) {
                        return@ElevatedButton
                    }

                    val selectedFigureA = drawableItems.getOrNull(selectedFigureAIndex)
                    val selectedFigureB = drawableItems.getOrNull(selectedFigureBIndex)

                    if (selectedFigureA == null || selectedFigureB == null) {
                        return@ElevatedButton
                    }

                    if (selectedFigureA.isCubicSpline || selectedFigureB.isCubicSpline || selectedFigureA.offsetList.size < 3 || selectedFigureB.offsetList.size < 3) {
                        scope.launch {
                            snackbarHostState.showSnackbar("ТМО нельзя применить к кубическому сплайну или к отрезку")
                        }
                        return@ElevatedButton
                    }
                    if (selectedFigureA == selectedFigureB) {
                        scope.launch {
                            snackbarHostState.showSnackbar("ТМО нельзя применить к одной фигуре")
                        }
                        return@ElevatedButton
                    }

                    calculateTmo(
                        selectedTmo.tmoType,
                        selectedFigureA,
                        selectedFigureB,
                        drawableItems,
                        onResult = {
                            onDrawableItemsChange(it)
                        }
                    )
                    onSelectedFigureAIndexChange(null)
                    onSelectedFigureBIndexChange(null)
                },
                shape = MaterialTheme.shapes.small,
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text(
                    "Применить ТМО",
                    style = textStyle
                )
            }
        }
    }
}

@Composable
fun ChooseTmoSection(
    expandedTmo: Boolean,
    onExpandedTmoChange: (Boolean) -> Unit,
    selectedTmo: TmoWithName,
    onSelectedTmoChange: (TmoWithName) -> Unit,
    tmoList: List<TmoWithName>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Выбор ТМО", style = textStyle)
        ExposedDropdownMenuBox(
            expanded = expandedTmo,
            onExpandedChange = onExpandedTmoChange
        ) {
            OutlinedTextField(
                value = selectedTmo.name,
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTmo)
                },
                modifier = Modifier.menuAnchor().width(widthOfButtons)
                    .height(heightOfButtons),
                textStyle = textStyle
            )

            ExposedDropdownMenu(
                expanded = expandedTmo,
                onDismissRequest = {
                    onExpandedTmoChange(false)
                }
            ) {
                tmoList.forEach { tmoWithName ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                tmoWithName.name
                            )
                        },
                        onClick = {
                            onSelectedTmoChange(tmoWithName)
                            onExpandedTmoChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChooseFigureASection(
    expandedFigureA: Boolean,
    onExpandedFigureA: (Boolean) -> Unit,
    selectedFigureAIndex: Int?,
    onSelectedFigureAIndex: (Int) -> Unit,
    drawableItems: List<DrawableItem>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Объект A", style = textStyle)
        ExposedDropdownMenuBox(
            expanded = expandedFigureA,
            onExpandedChange = onExpandedFigureA
        ) {
            OutlinedTextField(
                value = selectedFigureAIndex?.let { number ->
                    (number + 1).toString()
                } ?: "",
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFigureA)
                },
                modifier = Modifier.menuAnchor().width(widthOfButtons)
                    .height(heightOfButtons),
                textStyle = textStyle
            )

            ExposedDropdownMenu(
                expanded = expandedFigureA,
                onDismissRequest = {
                    onExpandedFigureA(false)
                }
            ) {
                drawableItems.forEachIndexed { index, _ ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                (index + 1).toString(),
                                style = textStyle
                            )
                        },
                        onClick = {
                            onSelectedFigureAIndex(index)
                            onExpandedFigureA(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChooseFigureBSection(
    expandedFigureB: Boolean,
    onExpandedFigureB: (Boolean) -> Unit,
    selectedFigureBIndex: Int?,
    onSelectedFigureBIndex: (Int) -> Unit,
    drawableItems: List<DrawableItem>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Объект B", style = textStyle)
        ExposedDropdownMenuBox(
            expanded = expandedFigureB,
            onExpandedChange = onExpandedFigureB
        ) {
            OutlinedTextField(
                value = selectedFigureBIndex?.let { number ->
                    (number + 1).toString()
                } ?: "",
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFigureB)
                },
                modifier = Modifier.menuAnchor().width(widthOfButtons)
                    .height(heightOfButtons),
                textStyle = textStyle
            )

            ExposedDropdownMenu(
                expanded = expandedFigureB,
                onDismissRequest = {
                    onExpandedFigureB(false)
                }
            ) {
                drawableItems.forEachIndexed { index, _ ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                (index + 1).toString(),
                                style = textStyle
                            )
                        },
                        onClick = {
                            onSelectedFigureBIndex(index)
                            onExpandedFigureB(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MirrorVerticalFigureSection(
    selectedFigure: DrawableItem?,
    position: Offset,
    showCheckBoxForVerticalLine: Boolean,
    onShowCheckBoxChange: (Boolean) -> Unit
) {
    Box(modifier = Modifier.padding(top = topPadding)) {
        ElevatedButton(
            modifier = Modifier.width(widthOfButtons + 60.dp)
                .height(heightOfButtons),
            onClick = {
                selectedFigure?.let { figure ->
                    figure.offsetList = mirrorPointsVertical(
                        figure.offsetList,
                        position.x
                    )
                }
            },
            shape = MaterialTheme.shapes.small,
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Зеркальное отражение относительно вертикальной прямой",
                        style = textStyle
                    )
                }
                IconButton(
                    onClick = {
                        onShowCheckBoxChange(showCheckBoxForVerticalLine)
                    }
                ) {
                    Icon(
                        imageVector = if (showCheckBoxForVerticalLine) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun MirrorCenterFigureSection(
    selectedFigure: DrawableItem?
) {
    Box(modifier = Modifier.padding(top = topPadding)) {
        ElevatedButton(
            modifier = Modifier.width(widthOfButtons + 60.dp)
                .height(heightOfButtons),
            onClick = {
                selectedFigure?.let { figure ->
                    val center = getCenterOfFigure(figure)
                    figure.offsetList = mirrorPointsSelectedCenter(figure.offsetList, center)
                }
            },
            shape = MaterialTheme.shapes.small,
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text(
                "Зеркальное отражение относительно центра фигуры",
                style = textStyle
            )
        }
    }
}

@Composable
fun ChoosePrimitiveSection(
    expandedPrimitive: Boolean,
    onExpandedPrimitiveChange: (Boolean) -> Unit,
    selectedPrimitive: PrimitiveTypeWithName,
    onSelectedPrimitiveChange: (PrimitiveTypeWithName) -> Unit,
    primitiveList: List<PrimitiveTypeWithName>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Выбор примитива", style = textStyle)
        ExposedDropdownMenuBox(
            expanded = expandedPrimitive,
            onExpandedChange = {
                onExpandedPrimitiveChange(it)
            }
        ) {
            OutlinedTextField(
                value = selectedPrimitive.name,
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrimitive)
                },
                modifier = Modifier.menuAnchor().width(widthOfButtons)
                    .height(heightOfButtons),
                textStyle = textStyle
            )

            ExposedDropdownMenu(
                expanded = expandedPrimitive,
                onDismissRequest = {
                    onExpandedPrimitiveChange(false)
                }
            ) {
                primitiveList.forEach { primitive ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                primitive.name
                            )
                        },
                        onClick = {
                            onSelectedPrimitiveChange(primitive)
                            onExpandedPrimitiveChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChooseRotationSection(
    rotationAngle: String,
    onRotationAngleChange: (String) -> Unit,
    selectedFigure: DrawableItem?,
    position: Offset
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Угол поворота", style = textStyle)
        OutlinedTextField(
            value = rotationAngle,
            onValueChange = { value ->
                onRotationAngleChange(value.filter { it.isDigit() || it == '-' })
            },
            textStyle = textStyle,
            modifier = Modifier.width(widthOfButtons)
                .height(heightOfButtons),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        selectedFigure?.let { item ->
                            item.rotationCenter = position
                            item.rotationAngle =
                                rotationAngle.toInt().toFloat()
                            item.offsetList = rotateFigure(
                                item.offsetList,
                                item.rotationCenter!!,
                                item.rotationAngle
                            )
                        }
                    }
                ) {
                    Icon(
                        tint = Color.Green,
                        imageVector = Icons.Outlined.Done,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Composable
fun ChooseColorSection(
    expandedColor: Boolean,
    onExpandedColorChange: (Boolean) -> Unit,
    selectedColor: ColorWithName,
    onColorSelectedChange: (ColorWithName) -> Unit,
    colorList: List<ColorWithName>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Цвет рисования", style = textStyle)

        ExposedDropdownMenuBox(
            expanded = expandedColor,
            onExpandedChange = onExpandedColorChange
        ) {
            OutlinedTextField(
                value = selectedColor.name,
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedColor)
                },
                modifier = Modifier.menuAnchor().width(widthOfButtons)
                    .height(heightOfButtons),
                textStyle = textStyle
            )

            ExposedDropdownMenu(
                expanded = expandedColor,
                onDismissRequest = {
                    onExpandedColorChange(false)
                }
            ) {
                colorList.forEach { colorWithName ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                colorWithName.name
                            )
                        },
                        onClick = {
                            onColorSelectedChange(colorWithName)
                            onExpandedColorChange(false)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseOperationSection(
    expandedOperation: Boolean,
    onExpandedOperationChange: (Boolean) -> Unit,
    selectedOperation: OperationWithName,
    onSelectedOperationChange: (OperationWithName) -> Unit,
    onClickWorkWithObject: () -> Unit,
    operationList: List<OperationWithName>,
    onClickDrawCubicSpline: () -> Unit,
    onClickAnotherOperation: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Выбор операции", style = textStyle)
        ExposedDropdownMenuBox(
            expanded = expandedOperation,
            onExpandedChange = {
                onExpandedOperationChange(it)
            }
        ) {
            OutlinedTextField(
                value = selectedOperation.name,
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOperation)
                },
                modifier = Modifier.menuAnchor().width(widthOfButtons)
                    .height(heightOfButtons),
                textStyle = textStyle
            )

            ExposedDropdownMenu(
                expanded = expandedOperation,
                onDismissRequest = {
                    onExpandedOperationChange(false)
                }
            ) {
                operationList.forEach { operationWithName ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                operationWithName.name
                            )
                        },
                        onClick = {
                            onSelectedOperationChange(operationWithName)
                            if (selectedOperation.operation == OperationType.DrawCubeSpline) {
                                onClickDrawCubicSpline()
                            } else {
                                onClickAnotherOperation()
                            }
                            if (selectedOperation.operation == OperationType.WorkWithObject) {
                                onClickWorkWithObject()
                            }
                            onExpandedOperationChange(false)
                        }
                    )
                }
            }
        }
    }
}

internal expect fun openUrl(url: String?)
internal expect fun getPlatformName(): String