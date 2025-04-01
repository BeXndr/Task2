package com.example.task2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@RestController
public class Task2Application {

    public static void main(String[] args) {
        SpringApplication.run(Task2Application.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @RestController
    @RequestMapping("/api/tarefas")
    public class TarefaController {

        @GetMapping
        public String listarTarefas() {
            // Create an instance of Task2Application to use its consultar method
            Task2Application application = new Task2Application();

            // Get the list of tasks
            ArrayList<Tarefa> tarefas = application.consultar();

            // If no tasks found, return a message
            if (tarefas.isEmpty()) {
                return "Nenhuma tarefa encontrada.";
            }

            // Convert tasks to a formatted string
            String tarefasFormatadas = tarefas.stream()
                    .map(tarefa -> String.format(
                    "ID: %d<br>"
                    + "Descrição: %s<br>"
                    + "Data Criação: %s<br>"
                    + "Data Prevista: %s<br>"
                    + "Data Encerramento: %s<br>"
                    + "Situação: %s<br><hr>",
                    tarefa.getId(),
                    tarefa.getDescricao(),
                    tarefa.getData_criacao(),
                    tarefa.getData_prevista(),
                    tarefa.getData_encerramento(),
                    tarefa.getSituacao()
            ))
                    .collect(Collectors.joining());

            return tarefasFormatadas;
        }
    }

    public ArrayList<Tarefa> consultar() {
        ArrayList<Tarefa> tarefa = new ArrayList<>();
        Tarefa retorno;

        try {
            Statement st = ConexaoBD.getInstance().getConnection().createStatement();

            String sql = " select * from tarefa; ";

            System.out.println("SQL: " + sql);

            ResultSet resultado = st.executeQuery(sql);

            while (resultado.next()) {
                retorno = new Tarefa();
                retorno.setId(resultado.getInt("id"));
                retorno.setDescricao(resultado.getString("descricao"));
                retorno.setData_criacao(resultado.getString("data_criacao"));
                retorno.setData_prevista(resultado.getString("data_prevista"));
                retorno.setData_encerramento(resultado.getString("data_encerramento"));
                retorno.setSituacao(resultado.getString("situacao"));

                tarefa.add(retorno);
            }

        } catch (Exception e) {
            System.out.println("Erro ao consultar tarefas: " + e);
        }

        return tarefa;
    }

}