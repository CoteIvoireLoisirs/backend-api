package com.erastedev.ciexplore.v1.domain.ports.out.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class RequestLoggingService {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingService.class);
    private static final String LOG_DIRECTORY = "logs/requests";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private final ObjectMapper objectMapper;

    public RequestLoggingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        File directory = new File(LOG_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Logs the given request in a JSON file in the "logs/requests" directory.
     * The log file will be named "requests-<year>-<month>.json".
     * The log data will contain the timestamp of the request, the HTTP method,
     * the request URL, the IP address of the client, and the headers of the request.
     * If the log file already exists and has a size greater than 5MB, a new log
     * file will be created with a number appended to the name. If an error occurs
     * while writing the log to the file, an error will be logged.
     *
     * @param request the request to log
     */
    public void logRequest(HttpServletRequest request) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        logData.put("method", request.getMethod());
        logData.put("url", request.getRequestURL().toString());
        logData.put("ip", request.getRemoteAddr());
        logData.put("headers", request.getHeaderNames());

        String fileName = getLogFileName();
        try {
            writeLogToFile(fileName, logData);
        } catch (IOException e) {
            logger.error("Failed to write log to file", e);
        }
    }

    private String getLogFileName() {
        String month = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return LOG_DIRECTORY + "/requests-" + month + ".json";
    }

    /**
     * Writes the given log data to a file, adding a new line and appending a comma if the file already exists.
     * If the file already exists and its size exceeds the maximum allowed, a new file is created with a number
     * appended to the name.
     *
     * @param fileName the name of the file to write the log data to
     * @param logData  the log data to write
     * @throws IOException if the file cannot be written
     */
    private void writeLogToFile(String fileName, Map<String, Object> logData) throws IOException {
        File file = new File(fileName);
        if (file.exists() && file.length() > MAX_FILE_SIZE) {
            int index = 1;
            while (file.exists() && file.length() > MAX_FILE_SIZE) {
                file = new File(fileName.replace(".json", "-" + index++ + ".json"));
            }
        }

        String jsonLog = objectMapper.writeValueAsString(logData);

        if (!file.exists()) {
            Files.write(file.toPath(), ("[" + jsonLog).getBytes(), StandardOpenOption.CREATE);
        } else {
            Files.write(file.toPath(), ("," + jsonLog).getBytes(), StandardOpenOption.APPEND);
        }
    }

    /**
     * This method is scheduled to run on the first day of each month at 00:00.
     * It appends a closing bracket to the JSON log file for the previous month,
     * effectively closing the array of log entries.
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void closeJsonFile() {
        String fileName = getLogFileName();
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write("]");
        } catch (IOException e) {
            logger.error("Failed to close JSON log file", e);
        }
    }
}
