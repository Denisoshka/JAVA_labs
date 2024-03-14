module ru.nsu.zhdanov.subtask5.subtask5 {
  requires javafx.controls;
  requires javafx.fxml;


  opens ru.nsu.zhdanov.subtask5.subtask5 to javafx.fxml;
  exports ru.nsu.zhdanov.subtask5.subtask5;
}