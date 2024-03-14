module ru.nsu.zhdanov.subtask5.subtask5 {
  requires javafx.controls;
  requires javafx.fxml;
  requires static lombok;
  requires org.slf4j;


  opens ru.nsu.zhdanov.subtask5.subtask5 to javafx.fxml;
  exports ru.nsu.zhdanov.subtask5.subtask5;
  exports ru.nsu.zhdanov.subtask5.subtask5.view;
  opens ru.nsu.zhdanov.subtask5.subtask5.view to javafx.fxml;
}