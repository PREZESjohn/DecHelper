package com.project.dechelper.toolCalling;

import com.project.dechelper.model.DocumentDTO;
import com.project.dechelper.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class DataModifyTools {


    private final DocumentService documentService;

    Logger log = Logger.getLogger(this.getClass().getName());

    @Tool(description = "Add document to vector store")
    public String addDocumentInStore(@ToolParam(description = "Data transfer object for Document") DocumentDTO documentDTO) {
        Document dc = new Document(documentDTO.getText(),documentDTO.getMetadata());
        documentService.saveDoc(dc);
        log.info("Potentionaly saved doc: "+ dc);
        return "Document added successfully";
    }

    @Tool(description = "Update document in vector store")
    public String updateDocumentInStore(@ToolParam(description = "Data transfer object for Document") DocumentDTO documentDTO) {
        Document dc = new Document(documentDTO.getId(),documentDTO.getText(), documentDTO.getMetadata());
        documentService.updateDoc(dc);
        log.info("Potentionaly updated doc: "+ dc);
        return "Document updated successfully";
    }

    @Tool(description = "Delete document in vector store")
    public String deleteDocumentInStore(String documentId) {
        documentService.deleteDocById(documentId);
        log.info("Potentionaly deleted doc of id: "+ documentId);
        return "Document deleted successfully";
    }
}
