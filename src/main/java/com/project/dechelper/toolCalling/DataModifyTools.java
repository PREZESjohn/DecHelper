package com.project.dechelper.toolCalling;

import com.project.dechelper.model.DocumentDTO;
import com.project.dechelper.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class DataModifyTools {


    private final DocumentService documentService;

    Logger log = Logger.getLogger(this.getClass().getName());

    @Tool(description = "Add document/information to vector store")
    public String addDocumentInStore(@ToolParam(description = "Data transfer object for Document") DocumentDTO documentDTO) {
        documentService.saveDoc(documentDTO);
        log.info("Potentionaly saved doc: "+ documentDTO);
        return "Document added successfully";
    }

    @Tool(description = "Update document/information in vector store")
    public String updateDocumentInStore(@ToolParam(description = "Data transfer object for Document") DocumentDTO documentDTO) {
        documentService.updateDoc(documentDTO);
        log.info("Potentionaly updated doc: "+ documentDTO);
        return "Document updated successfully";
    }

    @Tool(description = "Delete document/information in vector store")
    public String deleteDocumentInStore(String documentId) {
        documentService.deleteDocById(documentId);
        log.info("Potentionaly deleted doc of id: "+ documentId);
        return "Document deleted successfully";
    }
}
