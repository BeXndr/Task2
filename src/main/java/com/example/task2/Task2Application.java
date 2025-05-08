package com.example.task2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class Task2Application {

    private String user = "teste";
    private String passwd = "teste123";

    public static void main(String[] args) {
        SpringApplication.run(Task2Application.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/tarefas")
    public String listarTarefas(HttpServletRequest request, @RequestHeader("user") String user, @RequestHeader("passwd") String passwd) {
        if (!"Autenticação bem-sucedida.".equals(autenticar(user, passwd))) {
            return "Autenticação falhou. Acesso negado.";
        } else {
            // Get the list of tasks
            ArrayList<Tarefa> tarefas = new Tarefa().consultar();

            // If no tasks found, return a message
            if (tarefas.isEmpty()) {
                return "Nenhuma tarefa encontrada.";
            }

            StringBuilder response = new StringBuilder();
            for (Tarefa tarefa : tarefas) {
                response.append("Evento ID: ").append(tarefa.getId()).append("\n");
                response.append("Descricao: ").append(tarefa.getDescricao()).append("\n");
                response.append("Data criação: ").append(tarefa.getData_criacao()).append("\n");
                response.append("Data prevista: ").append(tarefa.getData_prevista()).append("\n");
                response.append("Data Encerramento: ").append(tarefa.getData_encerramento()).append("\n");
                response.append("Situação: ").append(tarefa.getSituacao()).append("\n\n");
            }

            StringBuffer url = request.getRequestURL();
            System.out.println("URL atual: " + url.toString());

            return response.toString();
        }
    }
    
    @GetMapping("/tarefasPorData")
    public String listarTarefasPorData(HttpServletRequest request, 
            @RequestParam("dataMin") String dataMin, 
            @RequestParam("dataMax") String dataMax, 
            @RequestHeader("user") String user, 
            @RequestHeader("passwd") String passwd) {
        if (!"Autenticação bem-sucedida.".equals(autenticar(user, passwd))) {
            return "Autenticação falhou. Acesso negado.";
        } else {
            // Get the list of tasks
            ArrayList<Tarefa> tarefas = new Tarefa().consultarPorData(dataMin, dataMax);

            // If no tasks found, return a message
            if (tarefas.isEmpty()) {
                return "Nenhuma tarefa encontrada.";
            }

            StringBuilder response = new StringBuilder();
            for (Tarefa tarefa : tarefas) {
                response.append("Evento ID: ").append(tarefa.getId()).append("\n");
                response.append("Descricao: ").append(tarefa.getDescricao()).append("\n");
                response.append("Data criação: ").append(tarefa.getData_criacao()).append("\n");
                response.append("Data prevista: ").append(tarefa.getData_prevista()).append("\n");
                response.append("Data Encerramento: ").append(tarefa.getData_encerramento()).append("\n");
                response.append("Situação: ").append(tarefa.getSituacao()).append("\n\n");
            }

            StringBuffer url = request.getRequestURL();
            System.out.println("URL atual: " + url.toString());

            return response.toString();
        }
    }

    @PostMapping("/inscricao")
    public String registrarTarefa(HttpServletRequest request, @RequestBody Tarefa tarefa, @RequestHeader("user") String user, @RequestHeader("passwd") String passwd
    ) {
        if (!"Autenticação bem-sucedida.".equals(autenticar(user, passwd))) {
            return "Autenticação falhou. Acesso negado.";
        } else {
            Tarefa tarefas = new Tarefa(
                    0, // id é definido automaticamente
                    tarefa.getDescricao(),
                    tarefa.getData_criacao(),
                    tarefa.getData_prevista(),
                    tarefa.getData_encerramento(),
                    tarefa.getSituacao()
            );

            boolean sucesso = new Tarefa().salvar(tarefas);

            if (sucesso) {
                Tarefa imprime = new Tarefa().consultarUltima();
                new EmailSender().mandarEmail(
                        "be.schneidr@gmail.com",
                        "Inscrição de tarefa",
                        "Inscrição da(o) " + imprime.getDescricao() + "!");

                StringBuffer url = request.getRequestURL();
                System.out.println("URL atual: " + url.toString());
                return "Tarefa registrada com sucesso.";
            } else {
                return "Falha ao registrar tarefa.";
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public String deletarTarefa(HttpServletRequest request, @PathVariable("id") int id, @RequestHeader("user") String user, @RequestHeader("passwd") String passwd
    ) {
        if (!"Autenticação bem-sucedida.".equals(autenticar(user, passwd))) {
            return "Autenticação falhou. Acesso negado.";
        } else {
            //Tarefa imprime = new Tarefa().consultarPorId(id);
            boolean sucesso = new Tarefa().deletarTarefa(id);

            if (sucesso) {
                //new EmailSender().mandarEmail(
                //        "be.schneidr@gmail.com",
                //        "Tarefa excluída",
                //        "Tarefa referente a " + imprime.getDescricao() + "!");

                StringBuffer url = request.getRequestURL();
                System.out.println("URL atual: " + url.toString());
                return "Tarefa excluída com sucesso.";
            } else {
                return "Falha ao excluir tarefa.";
            }

        }
    }

    @PostMapping("/emails")
    public String enviarEmail(HttpServletRequest request, @RequestBody Email emailDTO, @RequestHeader("user") String user, @RequestHeader("passwd") String passwd
    ) {
        if (!"Autenticação bem-sucedida.".equals(autenticar(user, passwd))) {
            return "Autenticação falhou. Acesso negado.";
        } else {
            try {
                // Aqui você pode adicionar a lógica para enviar o e-mail
                System.out.println("Enviando e-mail para: " + emailDTO.getAddress()
                        + ", Assunto: " + emailDTO.getTitle());

                // Lógica para enviar o e-mail usando o serviço de e-mail
                new EmailSender().mandarEmail(emailDTO.getAddress(), emailDTO.getTitle(), emailDTO.getText());

                StringBuffer url = request.getRequestURL();
                System.out.println("URL atual: " + url.toString());

                return "E-mail enviado com sucesso.";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao enviar e-mail: " + e.getMessage());
                return "erro";
            }
        }
    }

    @PostMapping("/auth")
    public String autenticar(@RequestHeader("user") String user, @RequestHeader("passwd") String passwd
    ) {
        try {
            // Verifica se o usuário e a senha fornecidos correspondem aos valores armazenados
            if (user.equals(this.user) && passwd.equals(this.passwd)) {
                System.out.println("Autenticação bem-sucedida para o usuário: " + user);
                return "Autenticação bem-sucedida.";
            } else {
                System.out.println("Autenticação falhou para o usuário: " + user);
                return "Autenticação falhou.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao autenticar: " + e.getMessage());
            return "erro";
        }
    }
}
