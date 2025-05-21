const chatMessages = document.getElementById('chat-messages');
const chatForm = document.getElementById('chat-form');
const messageInput = document.getElementById('message-input');

chatForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const message = messageInput.value;
    messageInput.value = '';

    chatMessages.innerHTML += `<div class="bg-blue-100 p-2 rounded"><strong>You:</strong> ${message}</div>`;

    const aiMessageContainer = document.createElement('div');
    aiMessageContainer.className = 'bg-green-100 p-2 rounded';
    aiMessageContainer.innerHTML = '<strong>AI:</strong> ';
    const aiMessageContent = document.createElement('span');
    aiMessageContainer.appendChild(aiMessageContent);
    chatMessages.appendChild(aiMessageContainer);

    try {
        const response = await fetchStreamWithRetry(`api/v1/ai/stream?message=${encodeURIComponent(message)}`);
        const reader = response.body.getReader();
        const decoder = new TextDecoder();

        while (true) {
            const {value, done} = await reader.read();
            if (done) break;
            const decodedChunk = decoder.decode(value, {stream: true});
            aiMessageContent.textContent += decodedChunk;
        }
    } catch (error) {
        console.error('Error:', error);
        aiMessageContent.textContent += 'Error occurred while fetching the response.';
    }

    chatMessages.scrollTop = chatMessages.scrollHeight;
});

async function fetchStreamWithRetry(url, retries = 3) {
    for (let i = 0; i < retries; i++) {
        try {
            const response = await fetch(url);
            if (response.ok) return response;
        } catch (error) {
            if (i === retries - 1) throw error;
        }
    }
}
