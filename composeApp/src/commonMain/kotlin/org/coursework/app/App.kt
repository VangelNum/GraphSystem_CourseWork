package org.coursework.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin


val widthOfButtons = 250.dp
val heightOfButtons = 48.dp
val textStyle = TextStyle(
    color = Color.Black,
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal
)
val topPadding = 24.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
    var selectedFigureA by remember {
        mutableStateOf<Int?>(null)
    }
    var expandedFigureB by remember {
        mutableStateOf(false)
    }
    var selectedFigureB by remember {
        mutableStateOf<Int?>(null)
    }
    var selectedTmo by remember {
        mutableStateOf(tmoList[0])
    }
    var expandedTmo by remember {
        mutableStateOf(false)
    }
    Scaffold(
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
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Выбор операции", style = textStyle)
                            ExposedDropdownMenuBox(
                                expanded = expandedOperation,
                                onExpandedChange = {
                                    expandedOperation = !expandedOperation
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
                                        expandedOperation = false
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
                                                selectedOperation = operationWithName
                                                if (selectedOperation.operation == OperationType.DrawCubeSpline) {
                                                    currentFigure.isCubicSpline = true
                                                } else {
                                                    currentFigure.isCubicSpline = false
                                                    currentFigure.tempListForLinesCubicSpline.clear()
                                                }
                                                if (selectedOperation.operation == OperationType.WorkWithObject) {
                                                    position = Offset(0f, 0f)
                                                }
                                                expandedOperation = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Column {
                            AnimatedVisibility(selectedOperation.operation != OperationType.WorkWithObject) {
                                Column(

                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Цвет рисования", style = textStyle)

                                    ExposedDropdownMenuBox(
                                        expanded = expandedColor,
                                        onExpandedChange = {
                                            expandedColor = !expandedColor
                                        }
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
                                                .height(heightOfButtons)
                                        )

                                        ExposedDropdownMenu(
                                            expanded = expandedColor,
                                            onDismissRequest = {
                                                expandedColor = false
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
                                                        selectedColor = colorWithName
                                                        expandedColor = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject && selectedFigure != null) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Угол поворота", style = textStyle)
                                    OutlinedTextField(
                                        value = rotationAngle,
                                        onValueChange = { value ->
                                            rotationAngle =
                                                value.filter { it.isDigit() || it == '-' }
                                        },
                                        modifier = Modifier.width(widthOfButtons)
                                            .height(heightOfButtons),
                                        singleLine = true,
                                        trailingIcon = {
                                            IconButton(
                                                onClick = {
                                                    if (selectedOperation.operation == OperationType.WorkWithObject) {
                                                        selectedFigure?.let { item ->
                                                            item.rotationCenter = position
                                                            item.rotationAngle =
                                                                rotationAngle.toInt().toFloat()
                                                            item.offsetList = rotatePoints(
                                                                item.offsetList,
                                                                item.rotationCenter!!,
                                                                item.rotationAngle
                                                            )
                                                        }
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
                        }
                    }
                    AnimatedVisibility(selectedOperation.operation == OperationType.PastPrimitive) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Выбор примитива", style = textStyle)

                            ExposedDropdownMenuBox(
                                expanded = expandedPrimitive,
                                onExpandedChange = {
                                    expandedPrimitive = !expandedPrimitive
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
                                        expandedPrimitive = false
                                    }
                                ) {
                                    primitiveList.forEach { colorWithName ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    colorWithName.name
                                                )
                                            },
                                            onClick = {
                                                selectedPrimitive = colorWithName
                                                expandedPrimitive = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject && selectedFigure != null) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.padding(top = topPadding)) {
                                ElevatedButton(
                                    modifier = Modifier.width(widthOfButtons + 60.dp)
                                        .height(heightOfButtons),
                                    onClick = {
                                        if (selectedOperation.operation == OperationType.WorkWithObject) {
                                            selectedFigure?.let { figure ->
                                                val center = getCenterOfFigure(figure)
                                                figure.offsetList =
                                                    mirrorPoints(figure.offsetList, center)
                                            }
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
                            Box(modifier = Modifier.padding(top = topPadding)) {
                                ElevatedButton(
                                    modifier = Modifier.width(widthOfButtons + 60.dp)
                                        .height(heightOfButtons),
                                    onClick = {
                                        if (selectedOperation.operation == OperationType.WorkWithObject) {
                                            selectedFigure?.let { figure ->
                                                figure.offsetList = mirrorPointsVertical(
                                                    figure.offsetList,
                                                    position.x
                                                )
                                            }
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
                                                showCheckBoxForVerticalLine =
                                                    !showCheckBoxForVerticalLine
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
                    }
                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Объект A", style = textStyle)
                                ExposedDropdownMenuBox(
                                    expanded = expandedFigureA,
                                    onExpandedChange = {
                                        expandedFigureA = !expandedFigureA
                                    }
                                ) {
                                    OutlinedTextField(
                                        value = selectedFigureA?.let { number ->
                                            (number + 1).toString()
                                        } ?: "",
                                        onValueChange = {

                                        },
                                        readOnly = true,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFigureA)
                                        },
                                        modifier = Modifier.menuAnchor().width(widthOfButtons)
                                            .height(heightOfButtons)
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expandedFigureA,
                                        onDismissRequest = {
                                            expandedFigureA = false
                                        }
                                    ) {
                                        drawableItems.forEachIndexed { index, drawableItem ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        (index + 1).toString(),
                                                        style = textStyle
                                                    )
                                                },
                                                onClick = {
                                                    selectedFigureA = index
                                                    expandedFigureA = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Выбор ТМО", style = textStyle)
                                ExposedDropdownMenuBox(
                                    expanded = expandedTmo,
                                    onExpandedChange = {
                                        expandedTmo = !expandedTmo
                                    }
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
                                            expandedTmo = false
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
                                                    selectedTmo = tmoWithName
                                                    expandedTmo = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Объект B", style = textStyle)
                                ExposedDropdownMenuBox(
                                    expanded = expandedFigureB,
                                    onExpandedChange = {
                                        expandedFigureB = !expandedFigureB
                                    }
                                ) {
                                    OutlinedTextField(
                                        value = selectedFigureB?.let { number ->
                                            (number + 1).toString()
                                        } ?: "",
                                        onValueChange = {

                                        },
                                        readOnly = true,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFigureA)
                                        },
                                        modifier = Modifier.menuAnchor().width(widthOfButtons)
                                            .height(heightOfButtons)
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expandedFigureB,
                                        onDismissRequest = {
                                            expandedFigureB = false
                                        }
                                    ) {
                                        drawableItems.forEachIndexed { index, drawableItem ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        (index + 1).toString(),
                                                        style = textStyle
                                                    )
                                                },
                                                onClick = {
                                                    selectedFigureB = index
                                                    expandedFigureB = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Box(modifier = Modifier.padding(top = topPadding)) {
                                ElevatedButton(
                                    modifier = Modifier.width(widthOfButtons)
                                        .height(heightOfButtons),
                                    onClick = {
                                        computeTMO(
                                            selectedTmo.tmoType,
                                            selectedFigureA,
                                            selectedFigureB,
                                            drawableItems,
                                            onResult = {
                                                drawableItems = it
                                            }
                                        )
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


                    Spacer(modifier = Modifier.weight(1f))
                    AnimatedVisibility(selectedOperation.operation == OperationType.WorkWithObject && selectedFigure != null) {
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
                                        val boundingBox =
                                            calculateBoundingBox(drawableItem.offsetList)

                                        val scaleX = size.width / boundingBox.width
                                        val scaleY = size.height / boundingBox.height

                                        val translateX = -boundingBox.left * scaleX
                                        val translateY = -boundingBox.top * scaleY

                                        val transformedPoints = drawableItem.offsetList.map {
                                            Offset(
                                                it.x * scaleX + translateX,
                                                it.y * scaleY + translateY
                                            )
                                        }

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
                                        } else {
                                            fillPrimitive(
                                                selectedDrawable
                                            )
                                        }


                                        if (showNumberOfFigures) {
                                            drawFigureNumberForChoosenFigure(
                                                selectedFigure,
                                                textMeasurer,
                                                drawableItems
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Box(modifier = Modifier.padding(top = topPadding)) {
                            ElevatedButton(
                                modifier = Modifier.width(widthOfButtons).height(heightOfButtons),
                                onClick = {
                                    showNumberOfFigures = !showNumberOfFigures
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
                                        Text("Отобразить номера объектов", style = textStyle)
                                    }
                                    Icon(
                                        imageVector = if (showNumberOfFigures) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        Box(modifier = Modifier.padding(top = topPadding)) {
                            ElevatedButton(
                                modifier = Modifier.width(widthOfButtons).height(heightOfButtons),
                                onClick = {
                                    drawableItems.clear()
                                    currentFigure.offsetList.clear()
                                    currentFigure.tempListForLinesCubicSpline.clear()
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
                                    Text("Очистить поле", style = textStyle)
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
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
                    detectDragGestures { change, dragAmount ->
                        if (selectedOperation.operation == OperationType.WorkWithObject && !showCheckBoxForVerticalLine) {
                            position = change.position
                            if (selectedFigure == null) {
                                selectedFigure = getSelectedFigure(position, drawableItems)
                            }
                            selectedFigure?.let { draggedFigure ->
                                draggedFigure.offsetList = draggedFigure.offsetList.map {
                                    Offset(it.x + dragAmount.x, it.y + dragAmount.y)
                                }
                            }
                        }
                    }
                }
                .onPointerEvent(PointerEventType.Press) { event ->
                    position = event.changes.first().position
                    if (!showCheckBoxForVerticalLine) {
                        selectedFigure = getSelectedFigure(position, drawableItems)
                    }
                    when (selectedOperation.operation) {
                        OperationType.DrawPrimitive -> {
                            if (event.buttons.isPrimaryPressed) {
                                currentFigure.offsetList.add(position)
                            }
                            if (event.buttons.isSecondaryPressed) {
                                if (currentFigure.offsetList.isNotEmpty() && currentFigure.offsetList.size > 1) {
                                    val drawableItem = DrawableItem(
                                        currentFigure.offsetList.toList(),
                                        selectedColor.color
                                    )
                                    drawableItems.add(drawableItem)
                                    currentFigure.offsetList.clear()
                                }
                            }
                        }

                        OperationType.PastPrimitive -> {
                            if (event.buttons.isPrimaryPressed) {
                                when (selectedPrimitive.type) {
                                    TypeOfPrimitive.FLAG -> {
                                        val flag = flag(position.x, position.y)
                                        drawableItems.add(
                                            DrawableItem(
                                                flag,
                                                selectedColor.color
                                            )
                                        )
                                    }

                                    TypeOfPrimitive.RECTANGULARTRIANGLE -> {
                                        val triangle = rectangularTriangle(position.x, position.y)
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
                            if (event.buttons.isPrimaryPressed) {
                                if (currentFigure.tempListForLinesCubicSpline.size == 4) {
                                    currentFigure.tempListForLinesCubicSpline.clear()
                                }
                                if (event.buttons.isPrimaryPressed) {
                                    currentFigure.tempListForLinesCubicSpline.add(position)
                                    if (currentFigure.tempListForLinesCubicSpline.size == 4) {
                                        val splinePoints =
                                            cubicSplinePath(currentFigure.tempListForLinesCubicSpline)
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

fun computeTMO(
    tmoType: TmoType,
    figureAIndex: Int?,
    figureBIndex: Int?,
    drawableItems: SnapshotStateList<DrawableItem>,
    onResult: (SnapshotStateList<DrawableItem>) -> Unit
) {
    if (figureAIndex == null || figureBIndex == null) {
        return
    }

    val figureA = drawableItems.getOrNull(figureAIndex)
    val figureB = drawableItems.getOrNull(figureBIndex)

    if (figureA == null || figureB == null) {
        return
    }

    figureA.tmoWasMake = false
    figureB.tmoWasMake = false

    val newFigureLines = mutableListOf<Line>()

    val SetQ = when (tmoType) {
        TmoType.MERGE -> shortArrayOf(1, 3)
        TmoType.DIFFERENCE -> shortArrayOf(2, 2)
    }

    val YallMin: Int = getMinimumYBothFigure(figureA.lines, figureB.lines)
    val YallMax: Int = getMaximumYBothFigure(figureA.lines, figureB.lines)
    for (j in YallMin until YallMax) {
        val XaLine = filterLinesByY(figureA.lines, j)
        val XbLine = filterLinesByY(figureB.lines, j)
        val M = buildMList(XaLine, XbLine)
        M.sortBy { it.x }
        var Q = 0
        var Qnew: Int
        val Xrl = mutableListOf<Int>()
        val Xrr = mutableListOf<Int>()

        M.forEach { mItem ->
            val x = mItem.x
            Qnew = Q + mItem.dQ

            if ((Q < SetQ[0] || Q > SetQ[1]) && (Qnew >= SetQ[0] && Qnew <= SetQ[1])) {
                Xrl.add(x)
            }

            if ((Q >= SetQ[0] && Q <= SetQ[1]) && (Qnew < SetQ[0] || Qnew > SetQ[1])) {
                Xrr.add(x)
            }

            Q = Qnew
        }
        for (i in Xrl.indices) {
            newFigureLines.add(Line(Xrl[i], Xrr[i], j))
        }
    }
    val newDrawableItem = DrawableItem(
        offsetList = calculateOffsets(newFigureLines),
        color = figureA.color,
        lines = newFigureLines,
        tmoWasMake = true
    )
    drawableItems.remove(figureA)
    drawableItems.remove(figureB)
    drawableItems.add(newDrawableItem)
    onResult(drawableItems)
}

fun calculateOffsets(newFigureLines: MutableList<Line>): List<Offset> {
    val resultOffsets = mutableListOf<Offset>()

    for (line in newFigureLines) {
        resultOffsets.add(Offset(line.xl.toFloat(), line.y.toFloat()))
        resultOffsets.add(Offset(line.xr.toFloat(), line.y.toFloat()))
    }

    return resultOffsets
}


fun buildMList(xaLines: List<Line>, xbLines: List<Line>): MutableList<BL> {
    val M = mutableListOf<BL>()

    xaLines.forEach { xaLine ->
        M.add(BL(xaLine.xl, 2))
        M.add(BL(xaLine.xr, -2))
    }

    xbLines.forEach { xbLine ->
        M.add(BL(xbLine.xl, 1))
        M.add(BL(xbLine.xr, -1))
    }

    return M
}

fun getMinimumYBothFigure(lines1: List<Line>, lines2: List<Line>): Int {
    var minY = Int.MAX_VALUE

    for (line in lines1) {
        if (line.y < minY) {
            minY = line.y
        }
    }

    for (line in lines2) {
        if (line.y < minY) {
            minY = line.y
        }
    }

    return minY
}

fun getMaximumYBothFigure(lines1: List<Line>, lines2: List<Line>): Int {
    var maxY = Int.MIN_VALUE

    for (line in lines1) {
        if (line.y > maxY) {
            maxY = line.y
        }
    }

    for (line in lines2) {
        if (line.y > maxY) {
            maxY = line.y
        }
    }

    return maxY
}

fun filterLinesByY(lines: List<Line>, y: Int): List<Line> {
    val filteredLines = mutableListOf<Line>()

    for (line in lines) {
        if (line.y == y) {
            filteredLines.add(line)
        }
    }

    return filteredLines
}

fun getMinimumY(offsetList: List<Offset>): Int {
    return offsetList.minOf { it.y }.toInt()
}

fun getMaximumY(offsetList: List<Offset>): Int {
    return offsetList.maxOf { it.y }.toInt()
}

fun DrawScope.fillPrimitive(drawableItem: DrawableItem) {
    if (drawableItem.offsetList.size == 2) {
        drawLine(
            color = drawableItem.color,
            strokeWidth = 2f,
            start = drawableItem.offsetList[0],
            end = drawableItem.offsetList[1]
        )
        return
    }
    if (drawableItem.tmoWasMake == true) {
        val offsets = drawableItem.offsetList
        offsets.chunked(2) { (xl, xr) ->
            drawLine(
                color = drawableItem.color,
                strokeWidth = 2f,
                start = xl,
                end = xr
            )
        }
    } else {
        drawableItem.lines.clear()

        val offsetList = drawableItem.offsetList
        val minY = getMinimumY(offsetList)
        val maxY = getMaximumY(offsetList)
        val xb = mutableListOf<Float>()

        for (y in minY..maxY) {
            xb.clear()

            for (i in offsetList.indices) {
                val k = if (i < offsetList.size - 1) i + 1 else 0

                if ((offsetList[i].y > y && offsetList[k].y <= y) || (offsetList[k].y > y && offsetList[i].y <= y)) {
                    val x =
                        offsetList[i].x + (y - offsetList[i].y) / (offsetList[k].y - offsetList[i].y) * (offsetList[k].x - offsetList[i].x)
                    xb.add(x)
                }
            }

            xb.sort()

            // Add Offset values in the correct order
            for (i in xb.indices step 2) {
                val xl = xb[i].toInt()
                val xr = if (i + 1 < xb.size) xb[i + 1].toInt() else xl
                drawLine(
                    color = drawableItem.color,
                    strokeWidth = 2f,
                    start = Offset(xl.toFloat(), y.toFloat()),
                    end = Offset(xr.toFloat(), y.toFloat())
                )
                drawableItem.lines.add(Line(xl = xl, xr = xr, y = y))
            }
        }
    }
}


fun calculateBoundingBox(offsetList: List<Offset>): Rect {
    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    for (offset in offsetList) {
        minX = min(minX, offset.x)
        minY = min(minY, offset.y)
        maxX = max(maxX, offset.x)
        maxY = max(maxY, offset.y)
    }

    return Rect(minX, minY, maxX, maxY)
}


fun DrawScope.drawHelperForVerticalMirror(
    position: Offset,
    pointAlpha: Float,
    selectedFigure: DrawableItem?,
    showCheckBoxForVerticalLine: Boolean
) {
    if (showCheckBoxForVerticalLine) {
        selectedFigure?.let { figure ->
            val axisX = position.x
            drawLine(
                color = Color.Cyan.copy(alpha = pointAlpha),
                start = Offset(axisX, figure.offsetList.minOf { it.y }),
                end = Offset(axisX, figure.offsetList.maxOf { it.y }),
                strokeWidth = 2f
            )
        }
    } else {
        drawCircle(
            color = Color.Red.copy(alpha = pointAlpha),
            center = position,
            radius = 4f
        )
    }
}

fun DrawScope.drawPathAndPointsForCurrentFigure(currentFigure: CurrentFigure, color: Color) {
    drawPath(
        path = Path().apply {
            currentFigure.offsetList.forEachIndexed { index, offset ->
                if (index == 0) {
                    moveTo(offset.x, offset.y)
                } else {
                    lineTo(offset.x, offset.y)
                }
            }
        },
        color = color,
        style = Stroke(width = 4f)
    )
    currentFigure.offsetList.forEach { offset ->
        drawCircle(
            color = Color.Magenta,
            center = offset,
            radius = 4f
        )
    }
}

fun DrawScope.drawTempLinesAndPointForCubicSpline(
    currentFigure: CurrentFigure
) {
    currentFigure.tempListForLinesCubicSpline.forEachIndexed { index, offset ->
        if (index < currentFigure.tempListForLinesCubicSpline.size - 1 && index % 2 == 0) {
            drawLine(
                color = Color.Cyan,
                start = currentFigure.tempListForLinesCubicSpline[index],
                end = currentFigure.tempListForLinesCubicSpline[index + 1],
                strokeWidth = 2f
            )
        }
        drawCircle(
            color = Color.Magenta,
            center = offset,
            radius = 4f
        )
    }
}

fun DrawScope.drawPathForCubicSpline(
    drawableItem: DrawableItem
) {
    drawPath(
        path = Path().apply {
            drawableItem.offsetList.forEachIndexed { index, offset ->
                if (index == 0) {
                    moveTo(offset.x, offset.y)
                } else {
                    lineTo(offset.x, offset.y)
                }
            }
        },
        color = drawableItem.color,
        style = Stroke(width = 2f)
    )
}

fun DrawScope.drawFigureNumbers(drawableItems: List<DrawableItem>, textMeasurer: TextMeasurer) {
    drawableItems.forEachIndexed { index, drawableItem ->
        val text = (index + 1).toString()
        val center = getCenterOfFigure(drawableItem)
        try {
            drawText(
                textMeasurer = textMeasurer, text = text, topLeft = center, style = TextStyle(
                    color = Color.Magenta,
                    fontSize = 24.sp
                )
            )
        } catch (e: Exception) {
            println("Experimental API ${e.message}")
        }
    }
}

fun DrawScope.drawFigureNumberForChoosenFigure(
    selectedFigure: DrawableItem?,
    textMeasurer: TextMeasurer,
    drawableItems: List<DrawableItem>
) {
    selectedFigure?.let {
        val text = "${drawableItems.indexOf(selectedFigure) + 1}"

        drawText(
            textMeasurer = textMeasurer,
            text = text,
            topLeft = center,
            style = TextStyle(
                color = Color.Red,
                fontSize = 12.sp
            )
        )
    }
}

fun mirrorPointsVertical(points: List<Offset>, verticalAxisX: Float): List<Offset> {
    return points.map { Offset(2 * verticalAxisX - it.x, it.y) }
}

fun mirrorPoints(points: List<Offset>, center: Offset): List<Offset> {
    return points.map { Offset(2 * center.x - it.x, it.y) }
}

fun getSelectedFigure(position: Offset, figures: List<DrawableItem>): DrawableItem? {
    return figures.lastOrNull { isPointInside(position, it.offsetList) }
}

fun getCenterOfFigure(figure: DrawableItem): Offset {
    var sumX = 0f
    var sumY = 0f

    for (offset in figure.offsetList) {
        sumX += offset.x
        sumY += offset.y
    }

    val centerX = sumX / figure.offsetList.size
    val centerY = sumY / figure.offsetList.size

    return Offset(centerX, centerY)
}


fun isPointInside(point: Offset, offsetList: List<Offset>): Boolean {
    val x = point.x
    val y = point.y

    var inside = false
    for (i in offsetList.indices) {
        val x1 = offsetList[i].x
        val y1 = offsetList[i].y
        val x2 = offsetList[(i + 1) % offsetList.size].x
        val y2 = offsetList[(i + 1) % offsetList.size].y

        val intersect = ((y1 > y) != (y2 > y)) &&
                (x < (x2 - x1) * (y - y1) / (y2 - y1) + x1)

        if (intersect) {
            inside = !inside
        }
    }

    return inside
}

fun rotatePoints(points: List<Offset>, center: Offset, angleDegrees: Float): List<Offset> {
    val resultPoints = mutableListOf<Offset>()

    val angleRadians = angleDegrees / 180 * PI
    val cosTheta = cos(angleRadians)
    val sinTheta = sin(angleRadians)

    for (point in points) {
        val translatedX = point.x - center.x
        val translatedY = point.y - center.y

        val rotatedX = translatedX * cosTheta - translatedY * sinTheta
        val rotatedY = translatedX * sinTheta + translatedY * cosTheta

        resultPoints.add(Offset(rotatedX.toFloat() + center.x, rotatedY.toFloat() + center.y))
    }

    return resultPoints
}


fun cubicSplinePath(
    selectedPoints: List<Offset>
): List<Offset> {
    val resultPoints = mutableListOf<Offset>()
    if (selectedPoints.size >= 4) {
        val pv1 = Offset(
            4 * (selectedPoints[1].x - selectedPoints[0].x),
            4 * (selectedPoints[1].y - selectedPoints[0].y)
        )
        val pv2 = Offset(
            4 * (selectedPoints[3].x - selectedPoints[2].x),
            4 * (selectedPoints[3].y - selectedPoints[2].y)
        )

        val l = arrayOf(Offset(0f, 0f), Offset(0f, 0f), Offset(0f, 0f), Offset(0f, 0f))

        l[0] = Offset(
            2 * selectedPoints[0].x - 2 * selectedPoints[2].x + pv1.x + pv2.x,
            2 * selectedPoints[0].y - 2 * selectedPoints[2].y + pv1.y + pv2.y
        )

        l[1] = Offset(
            -3 * selectedPoints[0].x + 3 * selectedPoints[2].x - 2 * pv1.x - pv2.x,
            -3 * selectedPoints[0].y + 3 * selectedPoints[2].y - 2 * pv1.y - pv2.y
        )

        l[2] = pv1
        l[3] = Offset(selectedPoints[0].x, selectedPoints[0].y)

        var t = 0.0
        val dt = 0.04

        var pt: Offset
        var ppred = l[3]

        while (t < 1 + dt / 2) {
            val xt = ((l[0].x * t + l[1].x) * t + l[2].x) * t + l[3].x
            val yt = ((l[0].y * t + l[1].y) * t + l[2].y) * t + l[3].y
            pt = Offset(xt.toFloat(), yt.toFloat())

            resultPoints.add(pt)

            ppred = pt
            t += dt
        }
    }
    return resultPoints
}

@Composable
fun getOperationList(): List<OperationWithName> {
    return listOf(
        OperationWithName(OperationType.DrawPrimitive, "Рисование примитивов"),
        OperationWithName(OperationType.PastPrimitive, "Вставка примитивов"),
        OperationWithName(OperationType.DrawCubeSpline, "Рисование кубического сплайна"),
        OperationWithName(OperationType.WorkWithObject, "Работа с объектами"),
    )
}

@Composable
fun getColorList(): List<ColorWithName> {
    return listOf(
        ColorWithName(Color.Black, "Черный"),
        ColorWithName(Color.Red, "Красный"),
        ColorWithName(Color.Green, "Зелёный"),
        ColorWithName(Color.Yellow, "Жёлтый"),
        ColorWithName(Color.Blue, "Синий"),
    )
}

@Composable
fun getPrimitiveList(): List<PrimitiveTypeWithName> {
    return listOf(
        PrimitiveTypeWithName(TypeOfPrimitive.FLAG, "Флаг"),
        PrimitiveTypeWithName(TypeOfPrimitive.RECTANGULARTRIANGLE, "Прямоугольный треугольник")
    )
}

@Composable
fun getTmoList(): List<TmoWithName> {
    return listOf(
        TmoWithName(TmoType.MERGE, "Объединение"),
        TmoWithName(TmoType.DIFFERENCE, "Разность")
    )
}

internal expect fun openUrl(url: String?)