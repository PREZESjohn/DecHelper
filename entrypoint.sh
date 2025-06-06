#!/bin/bash

# Start Ollama in the background.
/bin/ollama serve &
# Record Process ID.
pid=$!

# Pause for Ollama to start.
sleep 5

echo "🔴 Retrieve GEMMA3:4b-it-qat model..."
ollama pull gemma3:4b-it-qat
echo "🟢 Done!"

# Wait for Ollama process to finish.
wait $pid