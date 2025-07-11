let documents = [];

document.getElementById('document-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const id = document.getElementById('doc-id').value || generateUUID();
    const text = document.getElementById('doc-text').value;
    let metadata;
    try {
        metadata = JSON.parse(document.getElementById('doc-metadata').value || '{}');
    } catch (err) {
        alert('Invalid JSON in metadata');
        return;
    }

    const existingDocIndex = documents.findIndex(doc => doc.id === id);
    if (existingDocIndex >= 0) {
        documents[existingDocIndex] = { id, text, metadata };
    } else {
        documents.push({ id, text, metadata });
    }

    renderDocuments();
    document.getElementById('document-form').reset();
    document.getElementById('doc-id').value = '';
});

document.getElementById('clear-form').addEventListener('click', function() {
    document.getElementById('document-form').reset();
    document.getElementById('doc-id').value = '';
});

function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

function renderDocuments() {
    const tableBody = document.getElementById('document-table');
    tableBody.innerHTML = '';
    documents.forEach(doc => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="px-6 py-4 whitespace-nowrap">${doc.id}</td>
            <td class="px-6 py-4">${doc.text.substring(0, 50)}${doc.text.length > 50 ? '...' : ''}</td>
            <td class="px-6 py-4">${JSON.stringify(doc.metadata)}</td>
            <td class="px-6 py-4 whitespace-nowrap">
                <button onclick="editDocument('${doc.id}')" class="text-blue-500 hover:underline mr-2">Edit</button>
                <button onclick="deleteDocument('${doc.id}')" class="text-red-500 hover:underline">Delete</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function editDocument(id) {
    const doc = documents.find(doc => doc.id === id);
    if (doc) {
        document.getElementById('doc-id').value = doc.id;
        document.getElementById('doc-text').value = doc.text;
        document.getElementById('doc-metadata').value = JSON.stringify(doc.metadata, null, 2);
    }
}

function deleteDocument(id) {
    if (confirm('Are you sure you want to delete this document?')) {
        documents = documents.filter(doc => doc.id !== id);
        renderDocuments();
    }
}

renderDocuments();