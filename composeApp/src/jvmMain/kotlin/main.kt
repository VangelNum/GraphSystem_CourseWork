import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.coursework.app.App
import java.awt.Dimension

fun main() = application {
    Window(
        title = "GraphSystem_CourseWork",
        state = rememberWindowState(width = 1920.dp, height = 800.dp, position = WindowPosition(0.dp, 0.dp), isMinimized = true),
        onCloseRequest = ::exitApplication,

        ) {
        window.minimumSize = Dimension(500, 500)
        App()
    }
}