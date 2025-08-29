package com.stockexchange.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalService {

    private static final String LOG_FILE = "orders.log";
    private final ObjectMapper objectMapper;

    public WalService() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        Path path = Path.of(LOG_FILE);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public void log(CreateOrderCommand command) {
        WalEntry entry = new WalEntry(Instant.now(), command);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(objectMapper.writeValueAsString(entry));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever no WAL", e);
        }
    }

    public List<CreateOrderCommand> readAll() {
        List<CreateOrderCommand> commands = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                WalEntry entry = objectMapper.readValue(line, WalEntry.class);
                commands.add(entry.command());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler do WAL", e);
        }

        return commands;
    }

    public record WalEntry(Instant loggedAt, CreateOrderCommand command) {}
}
