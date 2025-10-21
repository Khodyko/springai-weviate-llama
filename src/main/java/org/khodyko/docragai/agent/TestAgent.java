package org.khodyko.docragai.agent;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TestAgent {

    @Tool(description = "connect with secret word", name = "secretConnector")
    public String getConnectedWord(@ToolParam(description = "word to connect with") String word) {
        return word+"IsidaInformatica";
    }
}
