package com.project.dechelper.toolCalling;

import com.project.dechelper.model.DocumentDTO;
import com.project.dechelper.services.DocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DataModifyToolsTest {
    @InjectMocks
    private DataModifyTools dataModifyTools;
    @Mock
    private DocumentServiceImpl documentService;

    private final DocumentDTO DOCUMENTDTO_1 = new DocumentDTO("1","test_one",  new LinkedHashMap<>(Map.of("Subject","Subject1", "Type", "Type1")));

    @Test
    public void addDocumentInStore_Test(){
        String resposne = dataModifyTools.addDocumentInStore(DOCUMENTDTO_1);
        verify(documentService).saveDoc(DOCUMENTDTO_1);
        assertTrue(resposne.contains("successfully"));
    }

    @Test
    public void updateDocumentInStore_Test(){
        String resposne = dataModifyTools.updateDocumentInStore(DOCUMENTDTO_1);
        verify(documentService).updateDoc(DOCUMENTDTO_1);
        assertTrue(resposne.contains("successfully"));
    }

    @Test
    public void deleteDocumentInStore_Test(){
        String resposne = dataModifyTools.deleteDocumentInStore("1");
        verify(documentService).deleteDocById("1");
        assertTrue(resposne.contains("successfully"));
    }
}
